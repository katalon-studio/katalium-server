package com.katalon.kata.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.katalon.kata.entity.Session;
import com.katalon.kata.utils.FileUtil;
import com.katalon.kata.utils.JsonUtil;
import com.katalon.kata.utils.LockUtil;
import com.katalon.kata.utils.SchedulerUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.grid.internal.ExternalSessionKey;
import org.openqa.grid.internal.GridRegistry;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SessionManager {

  private static final Logger LOG = Log.getLogger(SessionManager.class);

  private static final ObjectMapper objectMapper = JsonUtil.getObjectMapper();

  private static final String APP_DATA_FOLDER = System.getProperty("user.home") + File.separator + "katalon-selenium";

  private static final int MAX_AGE_OF_SESSION_DATA = 2 * 30 * 24 * 60 * 60 * 1000;

  private GridRegistry registry;

  private Cache<String, Object> cache = CacheBuilder.newBuilder()
          .maximumSize(1000)
          .expireAfterAccess(1, TimeUnit.HOURS)
          .build();

  private SessionManager() {
    FileUtil.createFolder(APP_DATA_FOLDER);
    SchedulerUtil.schedule(this::cleanData, 24 * 60 * 60 * 1000);
  }

  public void createSession(String sessionId) {
    createSessionFolder(sessionId);
    createSessionFile(sessionId);
  }

  public Session getSession(String sessionId) throws IOException {
    String sessionFilePath = getSessionFilePath(sessionId);
    Session session = FileUtil.read(sessionFilePath, Session.class);
    return session;
  }

  public boolean isSessionExist(String sessionId) {
    String sessionFolderPath = getSessionFolderPath(sessionId);
    File sessionFolder = new File(sessionFolderPath);
    return sessionFolder.exists() && sessionFolder.isDirectory();
  }

  public String getSessionFolderPath(String sessionId) {
    return APP_DATA_FOLDER + File.separator + sessionId;
  }

  public String getSessionFilePath(String sessionId) {
    return getSessionFolderPath(sessionId) + File.separator + "session.json";
  }

  public String getSessionsFolderPath() {
    return APP_DATA_FOLDER;
  }

  public void enableTakeScreenshot(String sessionId) {
    if (StringUtils.isNotBlank(sessionId)) {
      cache.put("screenshot-" + sessionId, Boolean.TRUE);
    }
  }

  public void disableTakeScreenshot(String sessionId) {
    if (StringUtils.isNotBlank(sessionId)) {
      cache.put("screenshot-" + sessionId, Boolean.FALSE);
    }
  }

  public boolean isTakeScreenshotEnable(String sessionId) {
    return StringUtils.isBlank(sessionId) || cache.getIfPresent("screenshot-" + sessionId) != Boolean.FALSE;
  }

  private void createSessionFolder(String sessionId) {
    String sessionFolderPath = getSessionFolderPath(sessionId);
    FileUtil.createFolder(sessionFolderPath);
  }

  private void createSessionFile(String sessionId) {
    Map<String, Object> capabilities = registry.getSession(ExternalSessionKey.fromString(sessionId)).getRequestedCapabilities();
    String sessionFilePath = getSessionFilePath(sessionId);
    LockUtil.write(sessionFilePath, () -> {
      try {
        File sessionFile = new File(sessionFilePath);
        FileUtil.createFile(sessionFile);

        Session session = new Session();
        session.setId(sessionId);
        session.setScreenshots(new ArrayList<>());
        session.setCreatedAt(new Date());
        session.updateCapability(capabilities);
        objectMapper.writeValue(sessionFile, session);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
  }

  public void cleanData() {
    File appDataFolder = new File(APP_DATA_FOLDER);
    if (appDataFolder.exists() && appDataFolder.isDirectory()) {
      File[] sessionFolders = appDataFolder.listFiles();
      if (sessionFolders != null) {
        Date now = new Date();
        long max = now.getTime() - MAX_AGE_OF_SESSION_DATA;
        for (File sessionFolder : sessionFolders) {
          try {
            Path sessionFolderPath = sessionFolder.toPath();
            BasicFileAttributes attributes = Files.readAttributes(sessionFolderPath, BasicFileAttributes.class);
            long createTime = attributes.creationTime().toMillis();
            if (createTime <= max) {
              FileUtils.deleteDirectory(sessionFolderPath.toFile());
            }
          } catch (Exception ignored) {
            LOG.debug("Cannot delete session folder " + sessionFolder, ignored);
          }
        }
      }
    }
  }

  public void setRegistry(GridRegistry registry) {
    this.registry = registry;
  }

  public GridRegistry getRegistry() {
    return this.registry;
  }

  public static SessionManager getInstance(){
    return SessionManagerHolder.INSTANCE;
  }

  private static class SessionManagerHolder{

    private static final SessionManager INSTANCE = new SessionManager();

  }
}
