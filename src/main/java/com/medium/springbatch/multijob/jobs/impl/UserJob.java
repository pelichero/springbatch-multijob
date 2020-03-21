package com.medium.springbatch.multijob.jobs.impl;

import com.medium.springbatch.multijob.jobs.AbstractStructuredJob;
import com.medium.springbatch.multijob.model.UserCSV;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;


@Component
public class UserJob extends AbstractStructuredJob<UserCSV, UserCSV> {

    public UserJob(@Value("${job.configuration.user.linesToSkip}") Integer linesToSkip,
                   @Value("${job.configuration.user.chunkSize}") Integer chunkSize,
                   @Value("${job.configuration.user.tableName}") String tableName,
                   @Value("${job.configuration.user.jobName}") String jobName,
                   @Value("${job.configuration.user.csvFileLocation}") Resource csvFileLocation) {
        super(linesToSkip, chunkSize, tableName, jobName, csvFileLocation);
    }

}
