package fr.formationacademy.scpiinvestplusapi.batch.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableScheduling
public class BatchJobScheduler {

    private final JobLauncher jobLauncher;
    private final Job scpiJob;

    @Scheduled(cron = "0 */1 * * * ?")
    //@Scheduled(cron = "0 0 2 * * ?")
    public void runJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(scpiJob, jobParameters);
            System.out.println("Job exécuté à 02:00");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}