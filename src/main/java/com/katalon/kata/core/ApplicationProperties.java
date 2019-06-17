package com.katalon.kata.core;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ApplicationProperties {

    private static final String CONFIG_FILE_PATH = System.getProperty("user.home") + File.separator + ".katalon" + File.separator +"framework.properties";

    private static final String KATALON_SERVER_URL_KEY = "KATALON_SERVER_URL";

    public static final String EMAIL_KEY = "KATALON_EMAIL";

    public static final String PASSWORD_KEY = "KATALON_API_KEY";

    private static final String DEFAULT_KATALON_SERVER_URL = "https://analytics.katalon.com";

    private static final Map<String, String> configurations = new HashMap<>();

    public ApplicationProperties() {
        loadConfigurations();
    }

    private void loadConfigurations() {
        Path path = Paths.get(CONFIG_FILE_PATH);
        File file = path.toFile();
        try (InputStream inputStream = new FileInputStream(file)) {
            Properties properties = new Properties();
            properties.load(inputStream);
            properties.forEach((key, value) -> configurations.put((String) key, (String) value));
        } catch (IOException ignored) {
            // Ignored
        }
    }

    public void storeConfigurations() {
        Path path = Paths.get(CONFIG_FILE_PATH);
        File file = path.toFile();

        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
        } catch (IOException e) {
            return;
        }

        try (OutputStream outputStream = new FileOutputStream(file)) {
            Properties properties = new Properties();
            configurations.forEach((key, value) -> properties.put(key, value));
            properties.store(outputStream, null);
        } catch (IOException ignored) {
            // Ignored
        }
    }

    private String getConfiguration(String key) {
        return getConfiguration(key, null);
    }

    private String getConfiguration(String key, String defaultValue) {
        String value = System.getenv(key);
        if (value == null) {
            value = configurations.get(key);
        }
        if (value == null) {
            value = defaultValue;
        }
//        if (value == null) {
//            String errorMessage = "Cannot get configuration for key: " + key;
//            throw new IllegalStateException(errorMessage);
//        }
        return value;
    }

    private void putConfiguration(String key, String value) {
        configurations.put(key, value);
    }

    public String getServerApiUrl() {
        return getConfiguration(KATALON_SERVER_URL_KEY, DEFAULT_KATALON_SERVER_URL);
    }

    public String getEmail() {
        return getConfiguration(EMAIL_KEY);
    }

    public String getPassword() {
        return getConfiguration(PASSWORD_KEY);
    }

    public void setEmail(String email) {
        putConfiguration(EMAIL_KEY, email);
    }

    public void setPassword(String password) {
        putConfiguration(PASSWORD_KEY, password);
    }
}
