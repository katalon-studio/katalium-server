package com.katalon.kata.core;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;

public class KataliumBanner {

  private static final Logger log = Log.getLogger(KataliumBanner.class);

  public KataliumBanner() {
  }

  public void printBanner() {
    try {
      InputStream inputStream = KataliumBanner.class.getClassLoader().getResourceAsStream("banner.txt");
      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
      StringBuilder banner = new StringBuilder();
      banner.append(System.lineSeparator());
      banner.append(reader.lines().collect(Collectors.joining(System.lineSeparator())));
      banner.append(System.lineSeparator());
      System.out.println(banner);
    } catch (Exception ignoredException) {
      // Ignored
    }
  }
}
