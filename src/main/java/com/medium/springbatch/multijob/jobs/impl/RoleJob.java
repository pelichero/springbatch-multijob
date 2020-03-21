package com.medium.springbatch.multijob.jobs.impl;

import com.medium.springbatch.multijob.jobs.AbstractStructuredJob;
import com.medium.springbatch.multijob.model.RoleCSV;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;


@Component
public class RoleJob extends AbstractStructuredJob<RoleCSV, RoleCSV> {

    public RoleJob(@Value("${job.configuration.role.linesToSkip}") Integer linesToSkip,
                   @Value("${job.configuration.role.chunkSize}") Integer chunkSize,
                   @Value("${job.configuration.role.tableName}") String tableName,
                   @Value("${job.configuration.role.jobName}") String jobName,
                   @Value("${job.configuration.role.csvFileLocation}") Resource csvFileLocation) {
	super(linesToSkip, chunkSize, tableName, jobName, csvFileLocation);
    }

}

