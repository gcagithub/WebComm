package com.webComm.hibernate;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;

/**
 * HibernateUtil. Supports many sessions and reads settings from properties
 * files.
 */
public class HibernateUtil {

	/** The session factories. */
	private static HashMap<String, SessionDescriptor> sessions = new HashMap<String, SessionDescriptor>();

	/** The log. */
	private static Log log = LogFactory.getLog(HibernateUtil.class);

	/** The messenger. */
	private static IMessages messenger;

	/**
	 * Sets the messenger.
	 * 
	 * @param m
	 *            the new messenger
	 */
	public static void setMessenger(IMessages m) {
		messenger = m;
	}

	public static void registerSession(SessionDescriptor sessionDescriptor) {
		if (sessions.containsKey(sessionDescriptor))
			return;
		sessions.put(sessionDescriptor.getSessionName(), sessionDescriptor);
	}

	/**
	 * Gets the session factory.
	 * 
	 * @return the session factory
	 */
	public static SessionFactory getSessionFactory() {
		final String s = "Get default session factory";
		log.trace(s);
		if (messenger != null) {
			messenger.debug(s);
		}
		return getSessionFactory(SessionDescriptor.DEFAULT_SESSION_NAME);
	}

	public static SessionFactory getSessionFactory(String sessionfactoryname) {
		log.trace("Get " + sessionfactoryname + " session factory");

		SessionFactory sessionFactory = null;
		if (sessions.containsKey(sessionfactoryname)) {
			sessionFactory = (SessionFactory) sessions.get(sessionfactoryname)
					.getSessionFactory();
		}

		return sessionFactory;
	}

	/**
	 * Stops Hibernate.
	 */
	public static void stop(boolean deregisterDrivers) {
		Set<String> keys = sessions.keySet();
		for (String sessionName : keys) {
			SessionFactory factory = (SessionFactory) sessions.get(sessionName)
					.getSessionFactory();
			if (!factory.isClosed()) {
				factory.close();
			}
		}
		sessions.clear();

		if (deregisterDrivers) {
			Enumeration<Driver> drivers = DriverManager.getDrivers();
			while (drivers.hasMoreElements()) {
				Driver driver = drivers.nextElement();
				try {
					DriverManager.deregisterDriver(driver);
					log.info(String.format("deregistering jdbc driver: %s",
							driver));
				} catch (SQLException e) {
					log.error(String.format("Error deregistering driver %s",
							driver), e);
				}

			}
		}
	}
}
