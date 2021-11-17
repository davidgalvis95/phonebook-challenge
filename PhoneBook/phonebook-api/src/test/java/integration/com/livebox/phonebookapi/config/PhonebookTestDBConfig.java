package com.livebox.phonebookapi.config;

import org.testcontainers.containers.PostgreSQLContainer;


public class PhonebookTestDBConfig extends PostgreSQLContainer<PhonebookTestDBConfig> {
    private static final String IMAGE_VERSION = "postgres:latest";
    private static final String DATABASE_NAME = "phonebooktest";
    private static final String DATABASE_USER = "phonebooktestuser";
    private static final String DATABASE_PASSWORD = "phonebooktestpwd";

    private static PhonebookTestDBConfig container;

    private PhonebookTestDBConfig() {

        super(IMAGE_VERSION);
    }

    public static PhonebookTestDBConfig getInstance() {
        if (container == null) {
            container = new PhonebookTestDBConfig()
                    .withDatabaseName(DATABASE_NAME)
                    .withUsername(DATABASE_USER)
                    .withPassword(DATABASE_PASSWORD);
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("USERNAME", container.getUsername());
        System.setProperty("PASSWORD", container.getPassword());
    }

    @Override
    public void stop() {
        super.stop();
    }
}