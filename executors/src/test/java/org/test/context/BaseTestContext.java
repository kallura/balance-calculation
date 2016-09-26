package org.test.context;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.test.TaskExecutor;
import org.test.TaskExecutorImpl;
import org.test.TaskMonitorExecutor;
import org.test.TaskMonitorExecutorImpl;

@Configuration
public class BaseTestContext {

    @Bean
    TaskExecutor taskExecutor(){
        return new TaskExecutorImpl(1000, 1000, 10);
    }

    @Bean
    TaskMonitorExecutor TaskMonitorExecutor(){
        return new TaskMonitorExecutorImpl(1200, 10);
    }
}
