package fr.formationacademy.scpiinvestplusapi.configuration.batchConfig;

import fr.formationacademy.scpiinvestplusapi.batch.listener.BatchJobListener;
import fr.formationacademy.scpiinvestplusapi.batch.processor.EncodingCorrectionProcessor;
import fr.formationacademy.scpiinvestplusapi.batch.processor.ScpiItemProcessor;
import fr.formationacademy.scpiinvestplusapi.batch.reader.ScpiItemReader;
import fr.formationacademy.scpiinvestplusapi.dto.BatchDataDto;
import fr.formationacademy.scpiinvestplusapi.dto.ScpiDto;
import fr.formationacademy.scpiinvestplusapi.entity.Scpi;
import fr.formationacademy.scpiinvestplusapi.repository.ScpiRepository;
import fr.formationacademy.scpiinvestplusapi.service.BatchService;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;

import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@EnableBatchProcessing
@EnableScheduling
@RequiredArgsConstructor
public class BatchConfig {

    private static final Logger log = LoggerFactory.getLogger(BatchConfig.class);

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManagerFactory entityManagerFactory;
    private final BatchJobListener batchJobListener;
    private final ScpiRepository scpiRepository;
    private final ScpiItemReader scpiItemReader;

    @Bean
    public CompositeItemProcessor<ScpiDto, Scpi> processor(ScpiItemProcessor scpiItemProcessor, BatchService batchService) {
        CompositeItemProcessor<ScpiDto, Scpi> compositeProcessor = new CompositeItemProcessor<>();

        ItemProcessor<ScpiDto, Scpi> conversionProcessor = scpiRequest -> {
            BatchDataDto batchData = batchService.convertToBatchData(scpiRequest);
            return scpiItemProcessor.process(batchData);
        };

        EncodingCorrectionProcessor<ScpiDto> encodingProcessor = new EncodingCorrectionProcessor<>();
        compositeProcessor.setDelegates(Arrays.asList(encodingProcessor, conversionProcessor));

        return compositeProcessor;
    }

    @Bean
    public ItemWriter<Scpi> writer() {
        return items -> {
            log.info("Insertion/Mise à jour de {} SCPI.", items.size());
            JpaItemWriter<Scpi> jpaWriter = new JpaItemWriter<>();
            jpaWriter.setEntityManagerFactory(entityManagerFactory);
            jpaWriter.write(items);
        };
    }

    @Bean
    public Tasklet deleteMissingScpiTasklet() {
        return (contribution, chunkContext) -> {
            Set<String> scpiNamesInCsv = new HashSet<>();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new ClassPathResource("scpi.csv").getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                boolean firstLine = true;
                while ((line = reader.readLine()) != null) {
                    if (firstLine) {
                        firstLine = false;
                        continue;
                    }
                    String[] fields = line.split(",");
                    if (fields.length > 0) {
                        scpiNamesInCsv.add(fields[0]);
                    }
                }
            }
            List<Scpi> scpisToDelete = scpiRepository.findAll().stream()
                    .filter(scpi -> !scpiNamesInCsv.contains(scpi.getName()))
                    .toList();

            if (!scpisToDelete.isEmpty()) {
                scpiRepository.deleteAll(scpisToDelete);
                log.info("{} SCPI supprimées car absentes du fichier CSV.", scpisToDelete.size());
            }

            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Step deleteStep() {
        return new StepBuilder("deleteStep", jobRepository)
                .tasklet(deleteMissingScpiTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Step importStep(ItemProcessor<ScpiDto, Scpi> processor, ItemWriter<Scpi> writer) {
        return new StepBuilder("importStep", jobRepository)
                .<ScpiDto, Scpi>chunk(10, transactionManager)
                .reader(scpiItemReader.reader())
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .skip(Exception.class)
                .skipLimit(100)
                .build();
    }

    @Bean
    public Job importScpiJob(Step deleteStep, Step importStep) {
        return new JobBuilder("importScpiJob", jobRepository)
                .listener(batchJobListener)
                .incrementer(new RunIdIncrementer())
                .start(deleteStep)
                .next(importStep)
                .build();
    }
}
