package com.katalon.kata.servlet;

import com.google.common.io.ByteStreams;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

public class KatalonConsole extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    process(request, response);
  }

  protected void process(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    String resource = request.getPathInfo();

    if (resource == null) {
      resource = "index.html";
    }

    if (resource.contains("resources")) {
      resource = resource.substring(resource.indexOf("resource"));
      resource = resource.replaceFirst("resources", "");
    }

    if (resource.startsWith("/")) {
      resource = resource.replaceFirst("/", "");
    }

    InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("static/" + resource);
    if (in == null) {
      resource = "index.html";
      in = Thread.currentThread().getContextClassLoader().getResourceAsStream("static/" + resource);
    }

    if(resource.endsWith(".js")) {
      response.setContentType("text/javascript");
      response.setCharacterEncoding("UTF-8");
    }

    if(resource.endsWith(".css")) {
      response.setContentType("text/css");
      response.setCharacterEncoding("UTF-8");
    }

    if(resource.endsWith(".html")) {
      response.setContentType("text/html");
      response.setCharacterEncoding("UTF-8");
    }

    if(resource.endsWith(".jpg")) {
      response.setContentType("image/jpeg");
    }

    try {
      ByteStreams.copy(in, response.getOutputStream());
    } finally {
      in.close();
      response.setHeader("Cache-Control", "no-cache");
      response.flushBuffer();
    }

  }
}
