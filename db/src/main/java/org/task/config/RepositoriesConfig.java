package org.task.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.task.repositories.*;
import org.task.repositories.initializers.DbInitializer;

@Configuration
@PropertySource("spring.datasource.properties")
public class RepositoriesConfig {

    @Bean
    BalanceRepository balanceRepository(){
        return new BalanceRepositoryImpl();
    }

    @Bean
    CustomerRepository customerRepository(){
        return new CustomerRepositoryImpl();
    }

    @Bean
    BalanceStatisticRepository balanceStatisticRepository(){
        return new BalanceStatisticRepositoryImpl();
    }

    @Bean
    @Profile("dev")
    DbInitializer dbInitializer(){
        return new DbInitializer();
    }
}
