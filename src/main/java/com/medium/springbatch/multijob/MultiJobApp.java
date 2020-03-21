package com.medium.springbatch.multijob;

import com.medium.springbatch.multijob.components.JobLauncherComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.core.env.ConfigurableEnvironment;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class MultiJobApp
		extends SpringBootServletInitializer {

    private static final Logger LOG = LoggerFactory.getLogger(MultiJobApp.class);

    @Autowired
    private ConfigurableEnvironment env;

    @Autowired
    private JobLauncherComponent launcherComponent;

    public static final String ALLOWED_JOB_LAUNCHER_PROFILE = "local";

    public static void main(String[] args) {
        SpringApplication.run(MultiJobApp.class, args);
    }

    @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
        LOG.info("####################APPLICATION STARTED#############################");
        LOG.info("");
        LOG.info("Timezone set to: {}", TimeZone.getDefault());
        LOG.info("");
        LOG.info("####################################################################");
    }

    @PostConstruct
    public void registerJobs() {
        launcherComponent.registerJobs();
    }

}