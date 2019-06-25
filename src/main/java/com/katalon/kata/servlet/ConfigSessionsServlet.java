package com.katalon.kata.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.katalon.kata.core.SessionManager;
import com.katalon.kata.entity.SessionConfiguration;
import com.katalon.kata.utils.JsonUtil;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ConfigSessionsServlet extends HttpServlet {

  private SessionManager sessionManager = SessionManager.getInstance();

  private ObjectMapper objectMapper = JsonUtil.getObjectMapper();

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
    process(req, resp);
  }

  protected  void process(HttpServletRequest req, HttpServletResponse resp) {
    SessionConfiguration configuration;

    try {
      String a = IOUtils.toString(req.getReader());
      configuration = objectMapper.readValue(a, SessionConfiguration.class);
    } catch (Exception e) {
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
      return;
    }

    try {
      process(configuration);
    } catch (Exception e) {
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  protected  void process(SessionConfiguration configuration) {
    String sessionId = configuration.getSessionId();
    Boolean disableScreenshot = configuration.getDisableScreenshot();

    if (disableScreenshot != null) {
      if (disableScreenshot) {
        sessionManager.disableTakeScreenshot(sessionId);
      } else {
        sessionManager.enableTakeScreenshot(sessionId);
      }
    }
  }
}
