package com.medium.springbatch.multijob.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration
@Import(MultiJobAppConfig.class)
@ComponentScan(value = {
            "com.medium.springbatch.multijob.components"
            ,"com.medium.springbatch.multijob.util"
            ,"com.medium.springbatch.multijob.jobs" })
public class MultiJobAppConfigTest {

}
