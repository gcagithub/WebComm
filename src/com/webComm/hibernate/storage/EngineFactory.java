package com.webComm.hibernate.storage;

/**
 * A factory for creating Engine objects.
 * Object will factor driver and dialect for database engine
 */
public class EngineFactory {
	
	/** The myengine. */
	private String myengine;

	/**
	 * Instantiates a new engine factory.
	 * 
	 * @param engine
	 *            the engine
	 */
	public EngineFactory(String engine) {
		myengine = engine;
	}
	
	/**
	 * Gets the engines.
	 * 
	 * @return the engines
	 */
	public String[] getEngines() {
		String[] engines = {"MYSQL", "PSQL", "HSQLDB", "MSSQL"};
		return engines;
	}
	
	/**
	 * Gets the driver.
	 * 
	 * @return the driver
	 */
	public String getDriver() {
		if(myengine.equals("MYSQL")) {
			return "com.mysql.jdbc.Driver";
		} else if (myengine.equals("PSQL")) {
			return "org.postgresql.Driver";
		} else if (myengine.equals("MSSQL")) {
			return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		}
		
		return "org.hsqldb.jdbcDriver";
	}
	
	/**
	 * Gets the dialect.
	 * 
	 * @return the dialect
	 */
	public String getDialect() {
		if(myengine.equals("MYSQL")) {
			return "org.hibernate.dialect.MySQLDialect";
		} else if (myengine.equals("PSQL")) {
			return "org.hibernate.dialect.PostgreSQLDialect";
		} else if (myengine.equals("MSSQL")) {
			return "org.hibernate.dialect.SQLServerDialect";
		}
		
		return "org.hibernate.dialect.HSQLDialect";
	}
}
