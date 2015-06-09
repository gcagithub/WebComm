package com.webComm.hibernate.storage;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.hibernate.cfg.Configuration;

import com.webComm.utils.Config;
import com.webComm.utils.HomeDir;


/**
 * The Class Description. Describe connection to database
 */
public class Description implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -257161567607487663L;

	/** The server url. */
	private String serverurl;

	/** The server login. */
	private String serverlogin;

	/** The server password. */
	private String serverpassword;

	/** The engine. */
	private String engine;

	/**
	 * Sets the serverurl.
	 * 
	 * @param serverurl
	 *            the serverurl to set
	 */
	public void setServerurl(String serverurl) {
		this.serverurl = serverurl;
	}

	/**
	 * Gets the serverurl.
	 * 
	 * @return the serverurl
	 */
	public String getServerurl() {
		return serverurl;
	}

	/**
	 * Sets the serverlogin.
	 * 
	 * @param serverlogin
	 *            the serverlogin to set
	 */
	public void setServerlogin(String serverlogin) {
		this.serverlogin = serverlogin;
	}

	/**
	 * Gets the serverlogin.
	 * 
	 * @return the serverlogin
	 */
	public String getServerlogin() {
		return serverlogin;
	}

	/**
	 * Sets the serverpassword.
	 * 
	 * @param serverpassword
	 *            the serverpassword to set
	 */
	public void setServerpassword(String serverpassword) {
		this.serverpassword = serverpassword;
	}

	/**
	 * Gets the serverpassword.
	 * 
	 * @return the serverpassword
	 */
	public String getServerpassword() {
		return serverpassword;
	}

	/**
	 * Sets the engine.
	 * 
	 * @param engine
	 *            the engine to set
	 */
	public void setEngine(String engine) {
		this.engine = engine;
	}

	/**
	 * Gets the engine.
	 * 
	 * @return the engine
	 */
	public String getEngine() {
		return engine;
	}

	private boolean checkConnection() {
		boolean result = true;
		EngineFactory ef = new EngineFactory(engine);
		try {
			Class.forName(ef.getDriver()).newInstance();
			Connection conn = DriverManager.getConnection(serverurl,
					serverlogin, serverpassword);

			conn.close();
		} catch (InstantiationException e) {
			result = false;
		} catch (IllegalAccessException e) {
			result = false;
		} catch (ClassNotFoundException e) {
			result = false;
		} catch (SQLException e) {
			result = false;
		}

		return result;
	}

	/**
	 * Gets the configuration.
	 * 
	 * @return the configuration
	 */
	public Configuration getConfiguration(String sessionname) {
		if (!checkConnection()) {
			setEngine("HSQLDB");
			setServerlogin("sa");
			setServerpassword("");
			setServerurl("jdbc:hsqldb:file:"
					+ HomeDir.getDbStorage(sessionname));

		}
		EngineFactory ef = new EngineFactory(engine);

		Configuration cfg;
		Properties props = new Properties();
		props.put("hibernate.dialect", ef.getDialect());
		props.put("hibernate.connection.driver_class", ef.getDriver());
		props.put("hibernate.connection.url", serverurl);
		props.put("hibernate.connection.username", serverlogin);
		props.put("hibernate.connection.password", serverpassword);
		props.put("hibernate.show_sql", Config.getGlobal("hibernate.show_sql",
				"false"));
		props.put("hibernate.format_sql", Config.getGlobal(
				"hibernate.format_sql", "false"));
		props.put("hibernate.hbm2ddl.auto", Config.getGlobal(
				"hibernate.hbm2ddl.auto", "update"));
		props.put("hibernate.connection.pool_size", Config.getGlobal(
				"hibernate.connection.pool_size", "1"));
		props.put("hibernate.current_session_context_class", Config.getGlobal(
				"hibernate.current_session_context_class", "thread"));
		props.put("hibernate.c3p0.max_size", Config.getGlobal(
				"hibernate.c3p0.max_size", "100"));
		props.put("hibernate.c3p0.max_statements", Config.getGlobal(
				"hibernate.c3p0.max_statements", "50"));
		props.put("hibernate.c3p0.min_size", Config.getGlobal(
				"hibernate.c3p0.min_size", "10"));
		props.put("hibernate.c3p0.timeout", Config.getGlobal(
				"hibernate.c3p0.timeout", "100"));
		props.put("hibernate.c3p0.validate", Config.getGlobal(
				"hibernate.c3p0.validate", "true"));
		props.put("hibernate.connection.provider_class", Config.getGlobal(
				"hibernate.connection.provider_class",
				"org.hibernate.connection.C3P0ConnectionProvider"));
		Config.save();
		cfg = new Configuration();
		cfg.setProperties(props);

		return cfg;
	}

}
