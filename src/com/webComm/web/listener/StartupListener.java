package com.webComm.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.webComm.web.Application;

public class StartupListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		Application.stop();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		String templatesdir = arg0.getServletContext().getRealPath("templates");
		String homedir = arg0.getServletContext().getRealPath("WEB-INF/home");
		Application.start(homedir, templatesdir);
	}
}
