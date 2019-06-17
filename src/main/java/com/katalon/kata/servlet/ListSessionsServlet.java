package com.katalon.kata.servlet;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.katalon.kata.core.SessionManager;
import com.katalon.kata.entity.Session;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

public class ListSessionsServlet extends HttpServlet {

  private SessionManager sessionManager = SessionManager.getInstance();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    process(request, response);
  }

  protected void process(HttpServletRequest request, HttpServletResponse response) throws IOException {
    File rootFolder = new File(sessionManager.getSessionsFolderPath());
    JsonArray array = new JsonArray();

    if (rootFolder.exists()) {
      Arrays.stream(rootFolder.list())
          .map(sessionId -> {
            try {
              return sessionManager.getSession(sessionId);
            } catch (Exception e) {
              return null;
            }
          })
          .filter(Objects::nonNull)
          .sorted(Comparator
              .comparing(Session::getCreatedAt)
              .reversed())
          .forEach(session -> {
              JsonObject jsonObject = new JsonObject();
              jsonObject.addProperty("id", session.getId());
              jsonObject.addProperty("browser", session.getBrowser());
              jsonObject.addProperty("version", session.getVersion());
              jsonObject.addProperty("platform", session.getPlatform());
              jsonObject.addProperty("createdAt", session.getCreatedAt().toString());
              array.add(jsonObject);
          });
    }

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.setStatus(200);
    response.getWriter().append(array.toString());
  }
}
