package com.katalon.kata.utils;

import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;

import java.util.Timer;
import java.util.TimerTask;

public class SchedulerUtil {

  private static final Logger LOG = Log.getLogger(SchedulerUtil.class);

  public static void schedule(Runnable runnable, long period) {
    Timer timer = new Timer(true);
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        try {
          LOG.info("Start scheduler");
          runnable.run();
          LOG.info("End scheduler");
        } catch (Exception e) {
          LOG.debug("Error when executing scheduler", e);
        }
      }
    }, 0, period);
  }
}
