package com.medium.springbatch.multijob.components;

import com.medium.springbatch.multijob.jobs.interfaces.StructuredJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.medium.springbatch.multijob.util.MultiJobAppConstants.Configuration.JOB_LAUNCHER_QUALIFIER;


@Component
public class JobLauncherComponent {

    private static final Logger LOG = LoggerFactory.getLogger(JobLauncherComponent.class);

    @Autowired
    @Qualifier(JOB_LAUNCHER_QUALIFIER)
    private JobLauncher jobLauncher;

    @Autowired
    private BeanRegisterComponent beanRegisterComponent;

    @Autowired
    private List<StructuredJob<?,?>> structuredJobs;

    public List<org.springframework.batch.core.Job> registerAndLaunchJobs() {
        List<Job> jobs = structuredJobs.stream().map(beanRegisterComponent::registerJob).collect(Collectors.toList());
        jobs.forEach(this::launch);
        return jobs;
    }

    public List<org.springframework.batch.core.Job> registerJobs() {
        return structuredJobs.stream().map(beanRegisterComponent::registerJob).collect(Collectors.toList());
    }

    public void launchJob(String jobName) {

        StructuredJob<?, ?> structuredJob = structuredJobs
                                                .stream()
                                                .filter(j -> j.jobName().equals(jobName))
                                                .findAny()
                                                .orElseThrow(IllegalStateException::new);

        launch(structuredJob.job());
    }

    private void launch(org.springframework.batch.core.Job job) {
        try {
            JobParameters params = new JobParametersBuilder().addString(job.getName(), String.valueOf(System.currentTimeMillis())).toJobParameters();
            jobLauncher.run(job, params);
        } catch (Exception ex) {
            LOG.error(ex.getMessage());
        }
    }
}
