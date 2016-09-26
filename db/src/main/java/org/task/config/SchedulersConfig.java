package org.task.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.task.schedulers.ScheduledBalanceStatistic;

@Configuration
@EnableAsync
@EnableScheduling
public class SchedulersConfig {

    @Bean
    ScheduledBalanceStatistic scheduledBalanceStatistic(){
        return new ScheduledBalanceStatistic();
    }
}
