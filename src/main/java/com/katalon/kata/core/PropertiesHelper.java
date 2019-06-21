package com.katalon.kata.core;

import java.io.InputStream;
import java.util.Properties;

import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;

public class PropertiesHelper {

  private static final Logger log = Log.getLogger(ApplicationProperties.class);

  private static Properties properties = new Properties();

  private PropertiesHelper() {
    loadProperties();
  }

  private void loadProperties() {
    try (InputStream inputStream = PropertiesHelper.class.getClassLoader().getResourceAsStream("application.properties")) {
      properties.load(inputStream);
    } catch (Exception ex) {
      log.debug("Fail to load properties", ex);
    }
    properties.putAll(System.getProperties());
    properties.putAll(System.getenv());
  }

  public String get(String key) {
    String value = properties.getProperty(key);
    return value;
  }

  public static PropertiesHelper getInstance() {
    return PropertiesHelperHolder.INSTANCE;
  }

  private static class PropertiesHelperHolder {

    private static final PropertiesHelper INSTANCE = new PropertiesHelper();

  }
}
