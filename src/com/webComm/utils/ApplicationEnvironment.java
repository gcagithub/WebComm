package com.webComm.utils;

import java.io.File;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

import com.webComm.hibernate.HibernateUtil;
import com.webComm.plugin.IPlugin;
import com.webComm.plugin.IPluginVersion;
import com.webComm.plugin.PluginFinder;
import com.webComm.utils.versions.Versions;

/**
 * The Application Environment.
 * 
 * @author alexript
 */
public class ApplicationEnvironment {

	private static final String PLUGIN_VERSION_INTERFACE = "com.webComm.plugin.IPluginVersion";
	private static String logpath;

	/** The log. */
	private static Log log = null;

	/** The plugin finder. */
	private static PluginFinder pluginFinder;

	private static Versions pluginversions;

	static {
		pluginversions = new Versions();
		pluginFinder = null;
	}

	/**
	 * Gets the plugins list.
	 * 
	 * @return the plugins list
	 */
	public static List<IPlugin> getPluginsList() {
		if (pluginFinder == null) {
			return null;
		}
		return pluginFinder.getPluginCollection();
	}

	/**
	 * Gets the plugin finder.
	 * 
	 * @return the plugin finder
	 */
	public static PluginFinder getPluginFinder() {
		return pluginFinder;
	}

	/**
	 * Start application.
	 * 
	 * @param homedir
	 *            application home dir
	 * @param appfileprefix
	 *            application private prefix
	 */
	public static void start(String homedir, String appfileprefix) {
		start(homedir, appfileprefix, true);
	}

	/**
	 * Start application.
	 * 
	 * @param homedir
	 *            application home dir
	 * @param appfileprefix
	 *            application private prefix
	 * @param fuseuserhomedir
	 *            use user's home dir for application's dir
	 */
	public static void start(String homedir, String appfileprefix,
			boolean fuseuserhomedir) {
		start(homedir, appfileprefix, fuseuserhomedir, null);
	}

	public static void setLogPath(String path) {
		logpath = path;
	}

	public static String getLogPath() {
		return logpath;
	}

	/**
	 * Start.
	 * 
	 * @param homedir
	 *            the homedir
	 * @param appfileprefix
	 *            the appfileprefix
	 * @param fuseuserhomedir
	 *            the fuseuserhomedir
	 * @param shareddir
	 *            the shareddir
	 */
	public static void start(String homedir, String appfileprefix,
			boolean fuseuserhomedir, String shareddir) {
		HomeDir.setApplicationHome(homedir);
		HomeDir.setApplicationFilePrefix(appfileprefix);
		HomeDir.setUseUserHome(fuseuserhomedir);
		HomeDir.setSharedDir(shareddir);

		VFS.init(HomeDir.getUserApplicationHome(),
				HomeDir.getApplicationFilePrefix());

		if (shareddir != null) {
			Config.initGlobalConfig(shareddir + HomeDir.getFileSeparator()
					+ appfileprefix + ".properties");
		}

		String logPath = getLogPath();
		if (logPath == null) {
			setLogPath(HomeDir.getLogPath());
		}

		Properties props = new Properties();
		props.put("log4j.defaultInitOverride", "false");
		props.put("log4j.debug", Config.get("log4j.debug", "false"));
		props.put("log4j.rootLogger", Config.get("log4j.loglevel", "error")
				+ ", " + Config.get("log4j.appender", "logfile"));
		props.put("log4j.appender.logfile", "org.apache.log4j.FileAppender");
		props.put("log4j.appender.logfile.layout",
				"org.apache.log4j.PatternLayout");
		props.put("log4j.appender.logfile.layout.ConversionPattern",
				Config.get("log4j.ConversionPattern", "%d [%t] %-5p %c - %m%n"));
		props.put("log4j.appender.logfile.File",
				getLogPath() + HomeDir.getApplicationFilePrefix() + ".log");

		props.put("log4j.appender.azure",
				"net.aws.windowsazure.AzureTableAppender");
		props.put("log4j.appender.azure.layout",
				"org.apache.log4j.PatternLayout");
		props.put("log4j.appender.azure.layout.ConversionPattern", Config.get(
				"log4j.azure.ConversionPattern",
				"%d{yyyy-MM-dd HH:mm:ss} %c{1} [%p] %m%n"));
		props.put(
				"log4j.appender.azure.connectionString",
				Config.get(
						"log4j.azure.connectionString",
						"DefaultEndpointsProtocol=https;AccountName=account-name;AccountKey=account-key"));

		PropertyConfigurator.configure(props);

		log = LogFactory.getLog(ApplicationEnvironment.class);

		log.trace("Start Protocol");

		log.trace("Save config");
		Config.save();

		loadPlugins();

	}

	/**
	 * Load plugins.
	 */
	@SuppressWarnings({ "rawtypes" })
	public static void loadPlugins() {
		String shareddir = HomeDir.getSharedDir();
		if (shareddir != null) {
			pluginFinder = new PluginFinder();
			try {
				String spldir = shareddir + HomeDir.getFileSeparator()
						+ "plugins";
				File pluginsdir = new File(spldir);
				if (!pluginsdir.exists()) {
					pluginsdir.mkdirs();
				}
				pluginFinder.search(spldir);
			} catch (Exception e) {
				log.error(e.getMessage());
				pluginFinder = null;
			}
		}

		if (pluginFinder != null) {
			List<IPlugin> pluginCollection = pluginFinder.getPluginCollection();
			for (IPlugin plugin : pluginCollection) {
				log.trace("Found plugin " + plugin.getName());
				Class[] interfaces = plugin.getClass().getInterfaces();
				String className = plugin.getClass().getName();
				String versionname = className;
				String jarname = pluginFinder.getPluginFile(versionname);
				int pluginversion = 0;
				for (Class c : interfaces) {
					if (c.getName().equals(PLUGIN_VERSION_INTERFACE)) {
						pluginversion = ((IPluginVersion) plugin).getVersion();
						log.trace("detected plugin version: " + pluginversion);
					}
				}
				pluginversions.addVersion(versionname, pluginversion, jarname);
				log.trace("add plugin version: " + versionname + ", "
						+ pluginversion + ", " + jarname);
				if (plugin.onLoad()) {
					plugin.initPlugin();
				}
			}
		}
	}

	public static Versions getPluginsVersions() {
		return pluginversions;
	}

	/**
	 * Stop plugins.
	 */
	public static void stopPlugins() {
		if (pluginFinder != null) {
			List<IPlugin> pluginCollection = pluginFinder.getPluginCollection();
			for (IPlugin plugin : pluginCollection) {
				log.trace("Stop plugin " + plugin.getName());
				if (plugin.stopPlugin()) {
					plugin.onUnload();
				}
			}

			pluginFinder = null;
		}
	}

	/**
	 * Stop application
	 */
	public static void stop(boolean deregisterDrivers) {
		stopPlugins();
		HibernateUtil.stop(deregisterDrivers);
		Config.save();
		VFS.stop();
	}

}
