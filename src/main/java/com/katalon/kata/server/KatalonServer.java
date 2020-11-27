package com.katalon.kata.server;

import com.katalon.kata.core.KataliumBanner;
import com.katalon.kata.core.PropertiesHelper;
import com.katalon.kata.utils.WebDriverUtil;
import org.openqa.grid.selenium.GridLauncherV3;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;

public class KatalonServer {

  private static final Logger LOG = Log.getLogger(KatalonServer.class);

  private static PropertiesHelper propertiesHelper = PropertiesHelper.getInstance();

  public static void main(String[] args) {

    LOG.info("Katalium Version: " + propertiesHelper.get("version"));
    new KataliumBanner().printBanner();

    WebDriverUtil.setup();
    GridLauncherV3.main(args);
  }
}
