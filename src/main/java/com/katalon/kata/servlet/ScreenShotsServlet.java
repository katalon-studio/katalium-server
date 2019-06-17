package com.katalon.kata.servlet;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.katalon.kata.core.SessionManager;
import com.katalon.kata.entity.Screenshot;
import com.katalon.kata.entity.Session;
import com.katalon.kata.utils.ImageUtil;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;

public class ScreenShotsServlet extends HttpServlet {

  private SessionManager sessionManager = SessionManager.getInstance();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    process(request, response);
  }

  protected void process(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String sessionId = request.getParameter("sessionId");
    Session session = sessionManager.getSession(sessionId);
    JsonArray array = new JsonArray();
    session.getScreenshots().stream()
        .sorted(Comparator.comparing(Screenshot::getCreatedAt))
        .forEach(screenshot -> {
          File screenshotFile = new File(screenshot.getPath());
          JsonObject jsonObject = new JsonObject();
          jsonObject.addProperty("sessionId", sessionId);
          jsonObject.addProperty("image", ImageUtil.imageToBase64(screenshotFile));
          jsonObject.addProperty("createdAt", screenshot.getCreatedAt().toString());
          array.add(jsonObject);
        });

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.setStatus(200);
    response.getWriter().append(array.toString());
  }
}
