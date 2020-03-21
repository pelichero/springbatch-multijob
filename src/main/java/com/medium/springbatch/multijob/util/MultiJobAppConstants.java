package com.medium.springbatch.multijob.util;


public class MultiJobAppConstants {

    public static class SQL {

        public static final String SCHEMA_DROP_H2_SQL = "classpath:org/springframework/batch/core/schema-drop-h2.sql";
        public static final String SCHEMA_H2_SQL      = "classpath:org/springframework/batch/core/schema-h2.sql";
        public static final String STRUCTURE_SQL      = "classpath:/sql/structure.sql";
    }

    public static class Configuration {

        public static final int    BEAN_INJECTION_ORDER_FIRST                = 1;
        public static final String MULTI_JOB_APP_CONFIG_QUALIFIER            = "multiJob'ETLConfig";
        public static final String MULTI_JOB_APP_JOB_BEAN_REGISTER_QUALIFIER = "MultiJobJobBeanRegisterQualifier";
        public static final String ETL_DATA_SOURCE_QUALIFIER                 = "dataSource";
        public static final String ETL_JDBC_TEMPLATE_QUALIFIER               = "jdbcTemplate";
        public static final String ETL_TRANSACTION_MANAGER_QUALIFIER         = "dataSourceTransactionManager";
        public static final String JOB_LAUNCHER_QUALIFIER                    = "jobLauncher";
        public static final String SKIPPER_QUALIFIER                         = "fileVerificationSkipper";

        /**
         * Dynamically added on every job inner helper class
         * ex:
         * jobName : userJob
         * <p>
         * So on, all beans injected related with would be:
         * <p>
         * userJobReader
         * userJobProcessor
         * .
         * .
         * .
         */
        public static final String QUALIFIER_SUFFIX_READER             = "Reader";
        public static final String QUALIFIER_SUFFIX_PROCESSOR          = "Processor";
        public static final String QUALIFIER_SUFFIX_WRITER             = "Writer";
        public static final String QUALIFIER_SUFFIX_READER_LISTENER    = "ReaderListener";
        public static final String QUALIFIER_SUFFIX_PROCESSOR_LISTENER = "ProcessorListener";
        public static final String QUALIFIER_SUFFIX_WRITER_LISTENER    = "WriterListener";
        public static final String QUALIFIER_SUFFIX_STEP_LISTENER      = "StepListener";
    }

}
