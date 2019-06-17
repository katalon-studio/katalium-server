package com.katalon.kata.entity;

public class Command {
  private String url;

  private String method;

  public Command(String method, String url) {
    this.url = "/wd/hub" + url
        .replace(":sessionId",".*")
        .replace(":windowHandle",".*")
        .replace(":key",".*")
        .replace(":name",".*")
        .replace(":id", ".*");
    this.method = method;
  }

  public String getUrl() {
    return url;
  }

  public String getMethod() {
    return method;
  }
}
