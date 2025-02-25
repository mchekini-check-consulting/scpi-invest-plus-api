package fr.formationacademy.scpiinvestplusapi.configuration;

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
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;
import java.util.Optional;

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

    @Bean
    public CompositeItemProcessor<ScpiDto, Scpi> processor(ScpiItemProcessor scpiItemProcessor, BatchService batchService, ScpiRepository scpiRepository) {
        CompositeItemProcessor<ScpiDto, Scpi> compositeProcessor = new CompositeItemProcessor<>();

        ItemProcessor<ScpiDto, Scpi> conversionProcessor = scpiRequest -> {
            BatchDataDto batchData = batchService.convertToBatchData(scpiRequest);
            return scpiItemProcessor.process(batchData);
        };

        ItemProcessor<Scpi, Scpi> filterProcessor = scpi -> {
            if (scpi == null || scpi.getId() == null) {
                return scpi;
            }
            Optional<Scpi> existingScpiOpt = scpiRepository.findById(scpi.getId());

            if (existingScpiOpt.isPresent()) {
                Scpi existingScpi = existingScpiOpt.get();

                if (existingScpi.equals(scpi)) {
                    log.info("Aucune modification détectée pour SCPI ID: {} - Passage à l'étape writer annulé.", scpi.getId());
                    return null;
                }
            }
            return scpi;
        };
        EncodingCorrectionProcessor<ScpiDto> encodingProcessor = new EncodingCorrectionProcessor<>();
        compositeProcessor.setDelegates(Arrays.asList(encodingProcessor, conversionProcessor, filterProcessor));
        return compositeProcessor;
    }

    @Bean
    public ItemWriter<Scpi> writer(ScpiRepository scpiRepository) {
        return items -> {
            long count = scpiRepository.count();
            if (items.isEmpty() && count > 0) {
                log.info("Aucun nouveau SCPI à insérer - Le writer ne sera pas déclenché.");
                return;
            }

            log.info("Insertion de {} nouveaux SCPI.", items.size());
            JpaItemWriter<Scpi> jpaWriter = new JpaItemWriter<>();
            jpaWriter.setEntityManagerFactory(entityManagerFactory);
            jpaWriter.write(items);
        };
    }

    @Bean
    public Step step1(ScpiItemReader scpiItemReader,
                      @Qualifier("processor") ItemProcessor<ScpiDto, Scpi> processor,
                      ItemWriter<Scpi> writer) {
        return new StepBuilder("step1", jobRepository)
                .<ScpiDto, Scpi>chunk(10, transactionManager)
                .reader(scpiItemReader.reader())
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .skip(Exception.class)
                .skipLimit(10)
                .build();
    }

    @Bean
    public Job importScpiJob(Step step1) {
        return new JobBuilder("importScpiJob", jobRepository)
                .listener(batchJobListener)
                .incrementer(new RunIdIncrementer())
                .start(step1)
                .build();
    }

}
