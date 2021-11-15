package com.livebox.phonebookapi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Data
@Configuration
@ConfigurationProperties("spring.datasource")
public class PhoneBookDBConfig {

    private String url;

    private String username;

    private String password;

    private String driverClassName;

    @Bean
    public DriverManagerDataSource getDataSource() {
        DriverManagerDataSource dataSourceBuilder = new DriverManagerDataSource();
        dataSourceBuilder.setDriverClassName(getDriverClassName());
        dataSourceBuilder.setUrl(getUrl());
        dataSourceBuilder.setUsername(getUsername());
        dataSourceBuilder.setPassword(getPassword());
        return dataSourceBuilder;
    }
}
