package com.aperto.magkit.monitoring.endpoint.info;

/**
 * 
 * Info Pojo.
 * 
 * @author Claudiu Gonciulea (Aperto - An IBM Company)
 * @since 2020-04-09
 *
 */
public class Info {

    private String _magnoliaEdition;
    private String _magnoliaVersion;
    
    /** Indicates if the Magnolia instance is AUTHOR or PUBLIC */
    private String _magnoliaContext;
    
    private String _operatingSystem;
    private String _javaVersion;
    private String _applicationServer;
    private String _database;
    private String _repository;
    
	public String getMagnoliaEdition() {
		return _magnoliaEdition;
	}
	
	public void setMagnoliaEdition(String magnoliaEdition) {
		_magnoliaEdition = magnoliaEdition;
	}
	
	public String getMagnoliaVersion() {
		return _magnoliaVersion;
	}
	
	public void setMagnoliaVersion(String magnoliaVersion) {
		_magnoliaVersion = magnoliaVersion;
	}
	
	public String getMagnoliaContext() {
		return _magnoliaContext;
	}
	
	public void setMagnoliaContext(String magnoliaContext) {
		_magnoliaContext = magnoliaContext;
	}
	
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
	
	public String getRepository() {
		return _repository;
	}
	
	public void setRepository(String repository) {
		_repository = repository;
	}
}
