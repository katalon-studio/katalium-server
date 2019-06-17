package com.katalon.kata.proxy;

import com.katalon.kata.core.SessionManager;
import com.katalon.kata.entity.Command;
import com.katalon.kata.entity.TakeScreenshotResult;
import com.katalon.kata.service.ScreenshotService;
import com.katalon.kata.service.SessionService;
import org.openqa.grid.common.RegistrationRequest;
import org.openqa.grid.internal.GridRegistry;
import org.openqa.grid.internal.TestSession;
import org.openqa.grid.selenium.proxy.DefaultRemoteProxy;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

public class KatalonProxy extends DefaultRemoteProxy {

  private static final Logger LOG = Log.getLogger(KatalonProxy.class);

  private SessionManager sessionManager = SessionManager.getInstance();

  private SessionService sessionService = SessionService.getInstance();

  private ScreenshotService screenshotService = ScreenshotService.getInstance();

  public KatalonProxy(RegistrationRequest request, GridRegistry registry) {
    super(request, registry);
    sessionManager.setRegistry(registry);
  }

  public void afterCommand(TestSession session, HttpServletRequest request, HttpServletResponse response) {
    super.afterCommand(session, request, response);
    commonAction(session, request);
  }

  private void commonAction(TestSession session, HttpServletRequest request) {
    try {
      String katalonHeader = request.getHeader("Katalon");

      if (!"Katalon".equals(katalonHeader)
          && session.getExternalKey() != null
          && isHandleRequest(request)) {
        String sessionId = session.getExternalKey().getKey();
        if (!sessionManager.isSessionExist(sessionId)) {
          sessionManager.createSession(sessionId);
        }
        if (sessionManager.isTakeScreenshotEnable(sessionId)) {
          takeScreenShot(sessionId, request);
        }
      }
    } catch (Exception e) {
      LOG.debug("Katalon internal action error", e);
    }
  }

  private TakeScreenshotResult takeScreenShot(String sessionId, HttpServletRequest request) throws IOException, URISyntaxException {
    TakeScreenshotResult screenshot = screenshotService.takeScreenshot(sessionId, request);
    sessionService.addScreenshot(sessionId, screenshot);
    return screenshot;
  }

  private static final Command[] ACCEPTED_COMMANDS = new Command[] {
      new Command("POST", "/session/:sessionId/url"),
      new Command("POST", "/session/:sessionId/forward"),
      new Command("POST", "/session/:sessionId/back"),
      new Command("POST", "/session/:sessionId/refresh"),
      new Command("POST", "/session/:sessionId/execute"),
      new Command("POST", "/session/:sessionId/execute_async"),
      new Command("POST", "/session/:sessionId/frame"),
      new Command("POST", "/session/:sessionId/frame/parent"),
      new Command("POST", "/session/:sessionId/window"),
      new Command("DELETE", "/session/:sessionId/window"),
      new Command("POST", "/session/:sessionId/window/:windowHandle/size"),
      new Command("POST", "/session/:sessionId/window/:windowHandle/position"),
      new Command("POST", "/session/:sessionId/window/:windowHandle/maximize"),
      new Command("POST", "/session/:sessionId/element/:id/click"),
      new Command("POST", "/session/:sessionId/element/:id/submit"),
      new Command("DELETE", "/session/:sessionId/session_storage"),
      new Command("DELETE", "/session/:sessionId/session_storage/key/:key"),
      new Command("POST", "/session/:sessionId/session_storage"),
      new Command("DELETE", "/session/:sessionId/local_storage"),
      new Command("DELETE", "/session/:sessionId/local_storage/key/:key"),
      new Command("POST", "/session/:sessionId/local_storage"),
      new Command("POST", "/session/:sessionId/location"),
      new Command("POST", "/session/:sessionId/doubleclick"),
      new Command("POST", "/session/:sessionId/touch/click"),
      new Command("POST", "/session/:sessionId/touch/down"),
      new Command("POST", "/session/:sessionId/touch/up"),
      new Command("POST", "/session/:sessionId/touch/move"),
      new Command("POST", "/session/:sessionId/touch/scroll"),
      new Command("POST", "/session/:sessionId/touch/scroll"),
      new Command("POST", "/session/:sessionId/touch/doubleclick"),
      new Command("POST", "/session/:sessionId/touch/longclick"),
      new Command("POST", "/session/:sessionId/touch/flick"),
      new Command("POST", "/session/:sessionId/touch/flick"),
      new Command("POST", "/session/:sessionId/moveto"),
      new Command("POST", "/session/:sessionId/click"),
      new Command("POST", "/session/:sessionId/buttondown"),
      new Command("POST", "/session/:sessionId/buttonup"),
      new Command("POST", "/session/:sessionId/alert_text"),
      new Command("POST", "/session/:sessionId/accept_alert"),
      new Command("POST", "/session/:sessionId/dismiss_alert"),
      new Command("POST", "/session/:sessionId/orientation"),
      new Command("POST", "/session/:sessionId/element/:id/value"),
      new Command("POST", "/session/:sessionId/keys"),
      new Command("POST", "/session/:sessionId/cookie"),
      new Command("DELETE", "/session/:sessionId/cookie"),
      new Command("DELETE", "/session/:sessionId/cookie/:name"),
      new Command("POST", "/session/:sessionId/ime/deactivate"),
      new Command("POST", "/session/:sessionId/ime/activate"),
  };

  private static final String ACCEPTED_REQUEST_REGREX = Arrays.stream(ACCEPTED_COMMANDS)
      .map(Command::getUrl)
      .reduce((a, b) -> a + "|" + b).orElse("");

  private boolean isHandleRequest(HttpServletRequest request) {
    String method = request.getMethod();
    String requestURI = request.getRequestURI();
    return ("POST".equals(method) || "DELETE".equals(method)) && requestURI.matches(ACCEPTED_REQUEST_REGREX);
  }

}
