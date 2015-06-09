package com.webComm.utils;

import java.io.File;

/**
 * Application home folder.
 */
public class HomeDir {
	private static String applicationFilesPrefix;
	private static String applicationDirName;
	private static final String fileSeparator = System
			.getProperty("file.separator");
	private static String sharedDir;
	private static boolean useUserHome;

	static {
		applicationFilesPrefix = "baukraftfile";
		applicationDirName = ".BauKrafthome";
		useUserHome = true;
		sharedDir = null;
	}

	/**
	 * Sets the application home.
	 * 
	 * @param homedir
	 */
	public static void setApplicationHome(String homedir) {
		applicationDirName = homedir;
	}

	/**
	 * Sets the application file prefix.
	 * 
	 * @param prefix
	 *            the new application file prefix
	 */
	public static void setApplicationFilePrefix(String prefix) {
		applicationFilesPrefix = prefix;
	}

	/**
	 * Gets the application file prefix.
	 * 
	 * @return the application file prefix
	 */
	public static String getApplicationFilePrefix() {
		return applicationFilesPrefix;
	}

	/**
	 * Gets the user home.
	 * 
	 * @return the user home
	 */
	private static String getUserHome() {
		return System.getProperty("user.home");
	}

	/**
	 * Gets the temp dir.
	 * 
	 * @return the temp dir
	 */
	public static String getTempDir() {
		return System.getProperty("java.io.tmpdir");
	}

	/**
	 * Gets the user application home.
	 * 
	 * @return the user application home
	 */
	public static String getUserApplicationHome() {
		String appHome;
		if (isUseUserHome()) {
			appHome = getUserHome() + getFileSeparator() + applicationDirName;
		} else {
			appHome = applicationDirName;
		}
		File fileAppHome = new File(appHome);
		if (!fileAppHome.exists()) {
			fileAppHome.mkdirs();
		}
		return appHome;

	}

	/**
	 * Gets the db path.
	 * 
	 * @return the db path
	 */
	public static String getDbPath() {
		return getDbPath(false);
	}

	/**
	 * Gets the db path.
	 * 
	 * @param useshared
	 *            the use shared folder
	 * @return the db path
	 */
	public static String getDbPath(boolean useshared) {
		String dbpath = null;
		if (useshared && sharedDir != null) {
			dbpath = getSharedDir() + getFileSeparator() + "db";
		} else {
			dbpath = getUserApplicationHome() + getFileSeparator() + "db";
		}
		File fileDbPath = new File(dbpath);
		if (!fileDbPath.exists()) {
			fileDbPath.mkdirs();
		}
		return dbpath;
	}

	/**
	 * Gets the data path.
	 * 
	 * @return the data path
	 */
	public static String getDataPath() {
		return getDataPath(false);
	}

	/**
	 * Gets the data path.
	 * 
	 * @param useshared
	 *            the useshared
	 * @return the data path
	 */
	public static String getDataPath(boolean useshared) {
		String datapath = null;
		if (useshared && sharedDir != null) {
			datapath = getSharedDir() + getFileSeparator() + "data";
		} else {
			datapath = getUserApplicationHome() + getFileSeparator() + "data";
		}
		File fileDataPath = new File(datapath);
		if (!fileDataPath.exists()) {
			fileDataPath.mkdirs();
		}
		return datapath;
	}

	/**
	 * Gets the view path.
	 * 
	 * @return the view path
	 */
	public static String getViewPath() {
		return getViewPath(false);
	}

	/**
	 * Gets the view path.
	 * 
	 * @param useshared
	 *            the useshared
	 * @return the view path
	 */
	public static String getViewPath(boolean useshared) {
		String viewpath = null;
		if (useshared && sharedDir != null) {
			viewpath = getSharedDir() + getFileSeparator() + "view";
		} else {
			viewpath = getUserApplicationHome() + getFileSeparator() + "view";
		}
		File fileViewPath = new File(viewpath);
		if (!fileViewPath.exists()) {
			fileViewPath.mkdirs();
		}
		return viewpath;
	}

	/**
	 * Creates the default config.
	 * 
	 * @param fullfilename
	 *            the fullfilename
	 */
	private static void createDefaultConfig(String fullfilename) {
		Config.set("config.version", "1");
		// Config.save();

	}

	/**
	 * Gets the scripts path.
	 * 
	 * @return the scripts path
	 */
	public static String getScriptsPath() {
		return getScriptsPath(false);
	}

	/**
	 * Gets the scripts path.
	 * 
	 * @param useshared
	 *            the useshared
	 * @return the scripts path
	 */
	public static String getScriptsPath(boolean useshared) {
		String absolutescriptpath = null;
		if (useshared && sharedDir != null) {
			absolutescriptpath = getSharedDir() + getFileSeparator()
					+ "scripts" + getFileSeparator();
		} else {
			absolutescriptpath = getUserApplicationHome() + getFileSeparator()
					+ "scripts" + getFileSeparator();
		}
		String scriptpath = "scripts" + getFileSeparator();
		File fileScriptsPath = new File(absolutescriptpath);
		if (!fileScriptsPath.exists()) {
			fileScriptsPath.mkdirs();
		}
		return scriptpath;
	}

	/**
	 * Gets the config file name.
	 * 
	 * @return the config file name
	 */
	public static String getConfigFileName() {
		return getConfigFileName(true);
	}

	/**
	 * Gets the config file name.
	 * 
	 * @param checkfileexists
	 *            the checkfileexists
	 * 
	 * @return the config file name
	 */
	public static String getConfigFileName(boolean checkfileexists) {
		String cfgpath = applicationFilesPrefix + ".properties";

		if (checkfileexists) {
			if (!VFS.isFileExists(cfgpath)) {
				createDefaultConfig(cfgpath);
			}
		}
		return cfgpath;

	}

	/**
	 * Gets the log path.
	 * 
	 * @return the log path
	 */
	public static String getLogPath() {
		String logspath = getUserApplicationHome() + getFileSeparator()
				+ "logs" + getFileSeparator();
		File fileLogsPath = new File(logspath);
		if (!fileLogsPath.exists()) {
			fileLogsPath.mkdirs();
		}

		return logspath;
	}

	/**
	 * Gets the db storage.
	 * 
	 * @return the db storage
	 */
	public static String getDbStorage() {
		String fileSeparator = System.getProperty("file.separator");
		return getDbPath() + fileSeparator + "database";
	}

	/**
	 * Gets the db storage.
	 * 
	 * @param dbname
	 *            the dbname
	 * 
	 * @return the db storage
	 */
	public static String getDbStorage(String dbname) {
		String fileSeparator = System.getProperty("file.separator");
		return getDbPath() + fileSeparator + dbname + "_database";
	}

	/**
	 * Checks if is useUserHome.
	 * 
	 * @return the useUserHome
	 */
	public static boolean isUseUserHome() {
		return useUserHome;
	}

	/**
	 * Sets the useUserHome.
	 * 
	 * @param aFusehomedir
	 *            the useUserHome to set
	 */
	public static void setUseUserHome(boolean isUseUserHome) {
		useUserHome = isUseUserHome;
	}

	/**
	 * Gets the fileSeparator.
	 * 
	 * @return the fileSeparator
	 */
	public static String getFileSeparator() {
		return fileSeparator;
	}

	/**
	 * Sets the shared dir.
	 * 
	 * @param sharedDir
	 *            the sharedDir to set
	 */
	public static void setSharedDir(String sharedDir) {
		HomeDir.sharedDir = sharedDir;
	}

	/**
	 * Gets the shared dir.
	 * 
	 * @return the sharedDir
	 */
	public static String getSharedDir() {
		return sharedDir;
	}

}
