package gui;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {

	private final String URL = "jdbc:sqlserver://${dbServer};databaseName=${dbName};user=${user};password={${pass}};encrypt=true;trustServerCertificate=true";

	private Connection connection = null;

	private String databaseName;
	private String serverName;
	private String username;
	private String password;

	public ConnectionManager(String propFile) {
		try {
			loadProperties(propFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean connect() {
		String fullURL = URL
				.replace("${dbServer}", this.serverName)
				.replace("${dbName}", this.databaseName)
				.replace("${user}", this.username)
				.replace("${pass}", this.password);
		try {
			this.connection = DriverManager.getConnection(fullURL);
		} catch (SQLException e) { 
			e.printStackTrace();
		}
		
		return this.connection != null;
	}
	
	public Connection getConnection() {
		return this.connection;
	}
	
	public void closeConnection() {
		try {
			if (this.connection != null && !this.connection.isClosed()) {
				this.connection.close();
			}
		} catch (SQLException e) {
			//Do nothing
		}
	}
	
	private void loadProperties(String propertiesFilePath) throws IOException {
		Properties prop = readPropertiesFile(propertiesFilePath);
	    this.serverName = prop.getProperty("serverName");
	    this.databaseName = prop.getProperty("databaseName");
	    this.username = prop.getProperty("serverUsername");
	    this.password = prop.getProperty("serverPassword");
	}
	
	private Properties readPropertiesFile(String propertiesFilePath) throws IOException {
	InputStream stream = null;
	Properties prop = null;
	try {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();           
		stream = loader.getResourceAsStream(propertiesFilePath);
		prop = new Properties();
	    prop.load(stream);
	} catch(FileNotFoundException fnfe) {
	    fnfe.printStackTrace();
	} catch(IOException ioe) {
	    ioe.printStackTrace();
	} finally {
	    stream.close();
	}
	  return prop;
	}

}