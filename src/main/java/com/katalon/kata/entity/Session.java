package com.katalon.kata.entity;

import java.io.Serializable;
import java.util.*;

public class Session implements Serializable {

  private String id;

  private List<Screenshot> screenshots;

  private String browser;

  private String version;

  private String platform;

  private Date createdAt;

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void addScreenshot(Screenshot... screenshots) {
    if (this.screenshots == null) {
      this.screenshots = new ArrayList<>();
    }
    this.screenshots.addAll(Arrays.asList(screenshots));
  }

  public List<Screenshot> getScreenshots() {
    return screenshots;
  }

  public void setScreenshots(List<Screenshot> screenshots) {
    this.screenshots = screenshots;
  }

  public String getBrowser() {
    return browser;
  }

  public void setBrowser(String browser) {
    this.browser = browser;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getPlatform() {
    return platform;
  }

  public void setPlatform(String platform) {
    this.platform = platform;
  }

  public void updateCapability(Map<String, Object> capabilities) {
    Object browserName = capabilities.get("browserName");
    Object version = capabilities.get("version");
    Object platform = capabilities.get("platform");

    if (browserName != null) {
      this.browser = browserName.toString();
    }

    if (version != null) {
      this.version = version.toString();
    }

    if (platform != null) {
      this.platform = platform.toString();
    }
  }
}
