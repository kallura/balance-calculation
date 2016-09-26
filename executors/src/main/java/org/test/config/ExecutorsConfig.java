package org.test.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.test.TaskExecutor;
import org.test.TaskExecutorImpl;
import org.test.TaskMonitorExecutor;
import org.test.TaskMonitorExecutorImpl;

@Configuration
@PropertySource("classpath:system.properties")
public class ExecutorsConfig {

    @Bean
    TaskExecutor TaskExecutor(@Value("${default.timeout}") Integer defaultTimeout,
                              @Value("${executors.map.size}") Integer executorMapSize,
                              @Value("${thread.pool.size}") Integer threadPoolSize) {
        return new TaskExecutorImpl(defaultTimeout, executorMapSize, threadPoolSize);
    }

    @Bean
    TaskMonitorExecutor taskMonitorExecutor(@Value("${cancel.timeout}") Integer cancelTimeout,
                                            @Value("${thread.pool.size}") Integer threadPoolSize) {
        return new TaskMonitorExecutorImpl(cancelTimeout, threadPoolSize);
    }
}
