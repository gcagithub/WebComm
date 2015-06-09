package com.webComm.hibernate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.MappingException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.webComm.utils.Config;
import com.webComm.utils.HomeDir;

public class SessionDescriptor {
	private String sessionName;
	public static final String DEFAULT_SESSION_NAME = "default";
	private List<Class<? extends HibernatedEntity>> mapClasses;
	private Log log;
	private Configuration cfg;

	private SessionFactory sessionFactory;

	public SessionDescriptor() {
		this(DEFAULT_SESSION_NAME);
	}

	public SessionDescriptor(String sessionName) {
		this.sessionName = sessionName;
		mapClasses = new ArrayList<Class<? extends HibernatedEntity>>();
		log = LogFactory.getLog(getClass());
	}

	public String getSessionName() {
		return sessionName;
	}

	public void registerMap(Class<? extends HibernatedEntity> map) {
		mapClasses.add(map);
	}

	public void registerMap(List<Class<? extends HibernatedEntity>> mapList) {
		mapClasses.addAll(mapList);
	}

	public void registerMap(Class<? extends HibernatedEntity>[] maps) {
		mapClasses.addAll(Arrays.asList(maps));
	}

	public List<Class<? extends HibernatedEntity>> getMaps() {
		return mapClasses;
	}

	@SuppressWarnings("deprecation")
	public SessionFactory getSessionFactory() {
		String name = getSessionName();
		if (sessionFactory == null) {

			if (name.equals(DEFAULT_SESSION_NAME)) {
				sessionFactory = new Configuration().configure()
						.buildSessionFactory();

			} else {
				initializeHibernate();
				addResources();
				sessionFactory = buildSession();
			}
			log.trace("sessionFactory [" + name + "]: " + sessionFactory);
			log.trace("buildSessionFactory finished");

		}
		return sessionFactory;
	}

	/**
	 * Read session configuration from file
	 * 
	 */
	private void initializeHibernate() {
		String name = getSessionName();
		String keyprefix = "hibernate." + name + ".";
		Properties props = new Properties();
		/** * Setting the hibernate properties **/
		props.put("hibernate.dialect", Config.get(keyprefix + "dialect",
				"org.hibernate.dialect.HSQLDialect"));
		props.put("hibernate.connection.driver_class", Config.get(keyprefix
				+ "connection.driver_class", "org.hsqldb.jdbcDriver"));
		props.put(
				"hibernate.connection.url",
				Config.get(keyprefix + "connection.url", "jdbc:hsqldb:file:"
						+ HomeDir.getDbStorage(name)
						+ ";shutdown=true;hsqldb.write_delay=false;"));
		props.put("hibernate.connection.username",
				Config.get(keyprefix + "connection.username", "SA"));
		props.put("hibernate.connection.password",
				Config.get(keyprefix + "connection.password", ""));
		props.put("hibernate.show_sql",
				Config.get(keyprefix + "show_sql", "false"));
		props.put("hibernate.format_sql",
				Config.get(keyprefix + "format_sql", "true"));
		props.put("hibernate.hbm2ddl.auto",
				Config.get(keyprefix + "hbm2ddl.auto", "update"));
		props.put("hibernate.connection.pool_size",
				Config.get(keyprefix + "connection.pool_size", "1"));
		props.put("hibernate.current_session_context_class", Config.get(
				keyprefix + "current_session_context_class", "thread"));
		props.put("hibernate.cache.provider_class","org.hibernate.cache.NoCacheProvider");
		props.put("hibernate.jdbc.factory_class","net.bull.javamelody.HibernateBatcherFactory"); // monitoring

		props.put("hibernate.c3p0.max_size", "20");
		props.put("hibernate.c3p0.max_statements", "100");
		props.put("hibernate.c3p0.min_size", "5");
		props.put("hibernate.c3p0.idle_test_period", "60");
		props.put("hibernate.c3p0.timeout", "100");
		props.put("hibernate.c3p0.validate", "true");
		props.put("hibernate.connection.provider_class",
				"org.hibernate.connection.C3P0ConnectionProvider");

		cfg = new Configuration();
		cfg.setProperties(props);

	}

	private void addResource(String filename) {
		if (filename != null && filename.length() > 1) {
			cfg.addResource(filename);
		}
	}

	private void addClassResource(String classname) {
		if (classname != null && !classname.isEmpty()) {
			try {
				cfg.addAnnotatedClass(Class.forName(classname));
			} catch (MappingException e) {
				log.error("Mapping exception");
				log.error(e.getMessage());
			} catch (ClassNotFoundException e) {
				log.error("Class not found");
				log.error(e.getMessage());
			}
		}
	}

	private void addResources() {
		String name = getSessionName();
		String path = Config.get("hibernate." + name + ".resources.path", "");
		String resourceslist = Config.get("hibernate." + name + ".resources",
				"");
		String[] resources = resourceslist.split(":");
		int len = resources.length;
		for (int i = 0; i < len; i++) {
			String resname = resources[i].trim();
			if (resname != null && !resname.isEmpty()) {
				addResource(path + "/" + resname);
			}
		}

		String classeslist = Config.get("hibernate." + name + ".classes", "");
		String[] classes = classeslist.split(":");
		len = classes.length;
		for (int i = 0; i < len; i++) {
			addClassResource(classes[i].trim());
		}

		for (Class<? extends HibernatedEntity> map : mapClasses) {
			cfg.addAnnotatedClass(map);
		}
	}

	@SuppressWarnings("deprecation")
	private SessionFactory buildSession() {
		Config.save();
		return cfg.buildSessionFactory();
	}

}
