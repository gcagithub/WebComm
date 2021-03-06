package com.webComm.web;

import com.webComm.data.files.FilesSession;
import com.webComm.data.files.storage.FSStorage;
import com.webComm.hibernate.HibernateUtil;
import com.webComm.hibernate.WebCommSession;
import com.webComm.utils.ApplicationEnvironment;
import com.webComm.utils.Config;
import com.webComm.utils.HomeDir;

public class Application {

	public static void start(String homedir, String templatesdir) {
		// com.webComm.web.velocity.Factory.setPath(templatesdir);

		String catalinabase = System.getProperty("catalina.base");
		String logspath = catalinabase + HomeDir.getFileSeparator() + "logs"
				+ HomeDir.getFileSeparator();

		ApplicationEnvironment.setLogPath(logspath);
		ApplicationEnvironment.start(homedir, "app", false);

		FilesSession.setup(Config.get("filesystem.userfiles.storageinstance",
				FSStorage.class.getCanonicalName()));

		Config.save();
		
		initDefaultData();
	}

	private static void initDefaultData() {
//		HibernateUtil.registerSession(new DomainSession());
		HibernateUtil.registerSession(new WebCommSession());
	}

	public static void stop() {
		// com.webComm.web.velocity.Factory.stop();
		ApplicationEnvironment.stop(true);
	}
}
