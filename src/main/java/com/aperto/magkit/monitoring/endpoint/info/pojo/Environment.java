package com.aperto.magkit.monitoring.endpoint.info.pojo;

/**
 * 
 * Environment POJO.
 * 
 * @author CLAUDIU GONCIULEA (Aperto - An IBM Company)
 * @since 2020-04-09
 *
 */
public class Environment {

	private String _operatingSystem;
    private String _javaVersion;
    private String _applicationServer;
    private String _database;
    private String _dbDriver;
    private String _repository;
    
    public String getOperatingSystem() {
		return _operatingSystem;
	}
	
	public void setOperatingSystem(String operatingSystem) {
		_operatingSystem = operatingSystem;
	}
	
	public String getJavaVersion() {
		return _javaVersion;
	}
	
	public void setJavaVersion(String javaVersion) {
		_javaVersion = javaVersion;
	}
	
	public String getApplicationServer() {
		return _applicationServer;
	}
	
	public void setApplicationServer(String applicationServer) {
		_applicationServer = applicationServer;
	}

	public String getDatabase() {
		return _database;
	}

	public void setDatabase(String database) {
		_database = database;
	}

	public String getDbDriver() {
		return _dbDriver;
	}

	public void setDbDriver(String dbDriver) {
		_dbDriver = dbDriver;
	}

	public String getRepository() {
		return _repository;
	}

	public void setRepository(String repository) {
		_repository = repository;
	}
}
