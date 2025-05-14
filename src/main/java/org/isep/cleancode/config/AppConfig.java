package org.isep.cleancode.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AppConfig {
    private static final String DEFAULT_REPOSITORY = "IN_MEMORY";
    private final Properties properties = new Properties();

    public AppConfig(String configFilePath) {
        if (configFilePath != null) {
            try (FileInputStream in = new FileInputStream(configFilePath)) {
                properties.load(in);
            } catch (IOException e) {
                System.err.println("Error loading config file: " + e.getMessage());
            }
        }
    }

    public String getRepositoryType() {
        return properties.getProperty("repository.type", DEFAULT_REPOSITORY);
    }
}