package com.medium.springbatch.multijob.configuration;

import com.medium.springbatch.multijob.components.DefaultSkipPolicy;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

import static com.medium.springbatch.multijob.util.MultiJobAppConstants.Configuration.*;
import static com.medium.springbatch.multijob.util.MultiJobAppConstants.SQL.*;


@EnableBatchProcessing
@Order(BEAN_INJECTION_ORDER_FIRST)
@Configuration(MULTI_JOB_APP_CONFIG_QUALIFIER)
public class MultiJobAppConfig {

    @Primary
    @Bean(ETL_DATA_SOURCE_QUALIFIER)
    public DataSource dataSource() {
        EmbeddedDatabaseBuilder embeddedDatabaseBuilder = new EmbeddedDatabaseBuilder();

        return embeddedDatabaseBuilder
                .addScript(SCHEMA_DROP_H2_SQL)
                .addScript(SCHEMA_H2_SQL)
                .addScript(STRUCTURE_SQL)
                .setType(EmbeddedDatabaseType.H2)
                .build();
    }

    @Primary
    @Bean(ETL_TRANSACTION_MANAGER_QUALIFIER)
    public PlatformTransactionManager dataSourceTransactionManager(final DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Primary
    @Bean(ETL_JDBC_TEMPLATE_QUALIFIER)
    public JdbcTemplate jdbcTemplate(final DataSource dataSource) {
        final JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);
        return jdbcTemplate;
    }

    @Bean
    @Qualifier(SKIPPER_QUALIFIER)
    protected SkipPolicy defaultSkipPolicy() {
        return new DefaultSkipPolicy();
    }

}
