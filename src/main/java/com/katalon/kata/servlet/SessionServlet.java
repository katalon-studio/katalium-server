package com.katalon.kata.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.katalon.kata.utils.JsonUtil;
import com.katalon.kata.core.SessionManager;
import com.katalon.kata.entity.Session;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SessionServlet extends HttpServlet {

  private SessionManager sessionManager = SessionManager.getInstance();

  private ObjectMapper objectMapper = JsonUtil.getObjectMapper();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    process(request, response);
  }

  protected void process(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String sessionId = request.getPathInfo();
    if (sessionId.startsWith("/")) {
      sessionId = sessionId.replaceFirst("/", "");
    }

    Session session = sessionManager.getSession(sessionId);

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.setStatus(200);
    response.getWriter().append(objectMapper.writeValueAsString(session));
  }
}
