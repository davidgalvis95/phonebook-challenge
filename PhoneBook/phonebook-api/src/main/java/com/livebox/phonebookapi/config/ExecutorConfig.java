package com.livebox.phonebookapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class ExecutorConfig {

    @Bean
    public Executor taskExecutor()
    {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize( 25 );
        executor.setMaxPoolSize( 40 );
        executor.setQueueCapacity( 100 );
        executor.initialize();
        return executor;
    }
}
