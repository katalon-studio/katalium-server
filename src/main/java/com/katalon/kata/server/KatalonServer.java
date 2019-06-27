package com.katalon.kata.server;

import com.katalon.kata.core.ApplicationProperties;
import com.katalon.kata.core.KataliumBanner;
import com.katalon.kata.core.PropertiesHelper;
import com.katalon.kata.utils.KatalonUtil;
import com.katalon.kata.utils.WebDriverUtil;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.openqa.grid.selenium.GridLauncherV3;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;

public class KatalonServer {

  private static final Logger LOG = Log.getLogger(KatalonServer.class);

  private static PropertiesHelper propertiesHelper = PropertiesHelper.getInstance();

  public static void main(String[] args) {

    LOG.info("Katalium Version: " + propertiesHelper.get("version"));
    new KataliumBanner().printBanner();

    ApplicationProperties applicationProperties = new ApplicationProperties();
    String email = applicationProperties.getEmail();
    String password = applicationProperties.getPassword();
    if (email == null || password == null) {
      inputInfo(applicationProperties);
      email = applicationProperties.getEmail();
      password = applicationProperties.getPassword();
    }
    String serverApiUrl = applicationProperties.getServerApiUrl();

    try {
      String token = KatalonUtil.requestToken(serverApiUrl, email, password);
      if (token == null) {
        return;
      }
      applicationProperties.storeConfigurations();
    } catch (Exception e) {
      LOG.debug("Error when requesting token from KA", e);
    }

    WebDriverUtil.setup();
    GridLauncherV3.main(args);
  }

  private static void inputInfo(ApplicationProperties applicationProperties) {
    TextIO textIO = TextIoFactory.getTextIO();

    textIO.getTextTerminal().println("Please activate Katalon. Register for an account at www.katalon.com.");

    String email = textIO.newStringInputReader()
      .read(ApplicationProperties.EMAIL_KEY);

    String password = textIO.newStringInputReader()
      .withInputMasking(true)
      .read(ApplicationProperties.PASSWORD_KEY);

    textIO.getTextTerminal().abort();

    applicationProperties.setEmail(email);
    applicationProperties.setPassword(password);
  }
}
