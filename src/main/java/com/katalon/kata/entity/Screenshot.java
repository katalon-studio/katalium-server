package com.katalon.kata.entity;

import java.io.Serializable;
import java.util.Date;

public class Screenshot implements Serializable {

  private String name;

  private String path;

  private Date createdAt;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }
}
