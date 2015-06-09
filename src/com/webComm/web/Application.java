package com.webComm.web;

import com.webComm.data.Domain.DomainSession;
import com.webComm.data.files.FilesSession;
import com.webComm.data.files.storage.FSStorage;
import com.webComm.hibernate.HibernateUtil;
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

		HibernateUtil.registerSession(new DomainSession());

		initDefaultData();

		Config.save();

	}

	@SuppressWarnings("unchecked")
	private static void initDefaultData() {
		// UserController uc = new UserController();
		// List<User> users = (List<User>) uc.findAll(User.class);
		//
		// boolean createDefUser =
		// Config.get(UserController.DEFAULT_USER_CREATE,
		// false);
		// if (createDefUser && users.size() < 1) {
		// String defuserlogin =
		// Config.get(UserController.DEFAULT_LOGIN_PROP, "");
		// String defuserpassword =
		// Config.get(UserController.DEFAULT_PASSWORD_PROP, "");

		// Registrator registrator = new Registrator();
		// registrator.createNewUser(defuserlogin, defuserpassword, "180",
		// "80", "userName");

		// if (uc.createNewUser(defuserlogin, defuserpassword, true, null)
		// != 0) {
		// LogFactory.getLog(Application.class).error(
		// "Can't create default user");
		// }
		// }
	}

	public static void stop() {
		// com.webComm.web.velocity.Factory.stop();
		ApplicationEnvironment.stop(true);
	}
}
