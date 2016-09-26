package org.task.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.test.config.ExecutorsConfig;

@Configuration
@ComponentScan("org.task.rest")
@EnableAutoConfiguration
@Import({ExecutorsConfig.class,
        SchedulersConfig.class,
        RepositoriesConfig.class})
public class ApplicationConfig {
}
