@echo on

java -jar kata-server.jar -role hub -servlets com.katalon.kata.servlet.ListSessionsServlet,com.katalon.kata.servlet.KatalonConsole,com.katalon.kata.servlet.ScreenShotsServlet,com.katalon.kata.servlet.ConfigSessionsServlet,com.katalon.kata.servlet.SessionServlet
