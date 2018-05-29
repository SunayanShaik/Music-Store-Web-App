package cs636.music.config;

import java.util.Properties;
import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSourceFactory;
// create DataSource object outside of the web container
// for client-server executions of normally-web apps, i.e., these substitute for the 
// JNDI looked-up DataSource objects configured in tomcat's context.xml
// Uses cs636 environment variables
// Depends upon Apache jar files commons-dbcp2, commons-logging, and commons-pool
// These are in the top-level lib directory since they are needed only in
// the client-server execution. 

public class StandAloneDataSourceFactory {

	public static DataSource createDataSource(String dbName) throws Exception {
		switch (dbName) {

		case "hsql":
			try {
				Properties props = new Properties();
				props.put("driverClassName", "org.hsqldb.jdbcDriver");
				props.put("url", "jdbc:hsqldb:hsql://localhost/");
				props.put("username", "SA");
				props.put("password", "");
				DataSource dataSource = BasicDataSourceFactory.createDataSource(props);
				return dataSource;
			} catch (Exception ex) {
				System.out.println("Problem creating Datasource for HSQLDB: " + ex);
				throw (ex);
			}
		case "dbs3":
			try {
				Properties props = new Properties();
				props.put("driverClassName", "oracle.jdbc.OracleDriver");
				String url = "jdbc:oracle:thin:@" + System.getenv("ORACLE_SITE");
				props.put("url", url);
				props.put("username", System.getenv("ORACLE_USER"));
				props.put("password", System.getenv("ORACLE_PW"));
				System.out.println("trying to connect to Oracle, url = " + url);
				DataSource dataSource = BasicDataSourceFactory.createDataSource(props);
				return dataSource;
			} catch (Exception ex) {
				System.out.println("Problem creating Datasource for Oracle: " + ex);
				throw (ex);
			}
		case "mysql":
			try {
				Properties props = new Properties();
				props.put("driverClassName", "com.mysql.jdbc.Driver");
				String url = "jdbc:mysql://" + System.getenv("MYSQL_SITE") + "/" + System.getenv("MYSQL_USER") + "db";
				props.put("url", url);
				props.put("username", System.getenv("MYSQL_USER"));
				props.put("password", System.getenv("MYSQL_PW"));
				System.out.println("trying to connect to mysql, url = " + url);
				DataSource dataSource = BasicDataSourceFactory.createDataSource(props);
				return dataSource;
			} catch (Exception ex) {
				System.out.println("Problem creating Datasource for mysql: " + ex);
				throw (ex);
			}
		default:
			System.out.println("Can't match dbName " + dbName);
			throw new Exception("Bad dbName in StandAloneDataSourceFactory");
		}

	}
	
}