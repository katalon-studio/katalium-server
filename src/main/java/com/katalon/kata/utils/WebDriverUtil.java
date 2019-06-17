package com.katalon.kata.utils;

import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;

import io.github.bonigarcia.wdm.WebDriverManager;

public class WebDriverUtil {

  private static final Logger LOG = Log.getLogger(WebDriverUtil.class);

  private static final String GECKO_RELEASES_JSON = "https://raw.githubusercontent.com/katalon-studio/katalon-studio/master/gecko-releases.json";

  public static void setup() {
    System.setProperty("wdm.geckoDriverUrl", GECKO_RELEASES_JSON);

    try {
      WebDriverManager.chromedriver().setup();
    } catch (Exception e) {
      LOG.debug("Error when setting up Chrome driver", e);
    }

    try {
      WebDriverManager.firefoxdriver().setup();
    } catch (Exception e) {
      LOG.debug("Error when setting up Firefox driver", e);
    }

    try {
      WebDriverManager.edgedriver().setup();
    } catch (Exception e) {
      LOG.debug("Error when setting up Edge driver", e);
    }

    try {
      WebDriverManager.iedriver().setup();
    } catch (Exception e) {
      LOG.debug("Error when setting up IE driver", e);
    }
  }
}
