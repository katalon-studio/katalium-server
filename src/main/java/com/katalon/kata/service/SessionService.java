package com.katalon.kata.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.katalon.kata.core.ExecutorService;
import com.katalon.kata.utils.JsonUtil;
import com.katalon.kata.core.SessionManager;
import com.katalon.kata.entity.Screenshot;
import com.katalon.kata.entity.Session;
import com.katalon.kata.entity.TakeScreenshotResult;
import com.katalon.kata.utils.LockUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;

public class SessionService {

  private ObjectMapper objectMapper = JsonUtil.getObjectMapper();

  private SessionManager sessionManager = SessionManager.getInstance();

  private ScreenshotService screenshotService = ScreenshotService.getInstance();

  private ExecutorService executorService = ExecutorService.getInstance();

  /**
   * Write screenshot to image file
   * Update session file
   *
   * @param sessionId
   * @param screenshotResult
   */
  public void addScreenshot(String sessionId, TakeScreenshotResult screenshotResult) {
    File screenshotFile = screenshotService.createEmptyScreenShotFile(sessionId);
    executorService.execute(() -> {
      try {
        screenshotService.write(screenshotFile, screenshotResult);
        Screenshot screenshot = new Screenshot();
        screenshot.setName(screenshotFile.getAbsolutePath());
        screenshot.setPath(screenshotFile.getAbsolutePath());
        screenshot.setCreatedAt(screenshotResult.getCreatedAt());
        addScreenshotToSessionFile(sessionId, screenshot);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
  }

  public void addScreenshotToSessionFile(String sessionId, Screenshot... screenshots) {
    String sessionFilePath = sessionManager.getSessionFilePath(sessionId);
    LockUtil.write(sessionFilePath, () -> {
      try {
        File sessionFile = new File(sessionFilePath);
        Session session = sessionManager.getSession(sessionId);
        session.addScreenshot(screenshots);
        objectMapper.writeValue(FileUtils.openOutputStream(sessionFile), session);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
  }

  public static SessionService getInstance(){
    return SessionServiceHolder.INSTANCE;
  }

  private static class SessionServiceHolder{

    private static final SessionService INSTANCE = new SessionService();

  }
}
