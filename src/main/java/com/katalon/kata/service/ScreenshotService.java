package com.katalon.kata.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.katalon.kata.core.SessionManager;
import com.katalon.kata.entity.TakeScreenshotResult;
import com.katalon.kata.utils.FileUtil;
import com.katalon.kata.utils.HttpUtil;
import com.katalon.kata.utils.ImageUtil;

import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class ScreenshotService {

  private SessionManager sessionManager = SessionManager.getInstance();

  private Cache<String, Object> cache = CacheBuilder.newBuilder()
      .expireAfterAccess(1, TimeUnit.HOURS)
      .build();

  public TakeScreenshotResult takeScreenshot(String sessionId, HttpServletRequest request) throws IOException, URISyntaxException {
    Map<String, String> headers = new HashMap<>();
    headers.put("Accept", "application/json");
    headers.put("Content-type", "application/json");
    TakeScreenshotResult screenshot = HttpUtil.get(
        "http://localhost:" + request.getServerPort() + "/wd/hub/session/" + sessionId + "/screenshot",
        headers,
        TakeScreenshotResult.class);
    if (screenshot.getSessionId() == null) {
      screenshot.setSessionId(sessionId);
    }
    screenshot.setCreatedAt(new Date());
    return screenshot;
  }

  public File write(File screenshotFile, TakeScreenshotResult screenshot) throws IOException {
    BufferedImage screenshotImage = ImageUtil.base64ToImage(screenshot.getValue());
    ImageUtil.write(screenshotFile, screenshotImage);
    return screenshotFile;
  }

  public File createEmptyScreenShotFile(String sessionId) {
    String sessionFolderPath = sessionManager.getSessionFolderPath(sessionId);
    File sessionFolder = new File(sessionFolderPath);
    File screenshotFile = new File(sessionFolder, generateScreenshotName(sessionId));
    while (!FileUtil.createFile(screenshotFile)) {
      screenshotFile = new File(sessionFolder, generateScreenshotName(sessionId));
    }
    return screenshotFile;
  }

  private String generateScreenshotName(String sessionId) {
    AtomicLong counter = (AtomicLong) cache.getIfPresent(sessionId);
    if (counter == null) {
      counter = createCounter(sessionId);
    }
    String fileName = counter.getAndIncrement() + ".png";
    return fileName;
  }

  private synchronized AtomicLong createCounter(String sessionId) {
    AtomicLong counter = (AtomicLong) cache.getIfPresent(sessionId);
    if (counter == null) {
      counter = new AtomicLong(0L);
      cache.put(sessionId, counter);
    }
    return counter;
  }

  public static ScreenshotService getInstance(){
    return ScreenshotServiceHolder.INSTANCE;
  }

  private static class ScreenshotServiceHolder{

    private static final ScreenshotService INSTANCE = new ScreenshotService();

  }
}
