package com.aperto.magkit.monitoring.endpoint.logs;


/**
 * 
 * LogInfo POJO
 * 
 * @authors Dan Olaru (IBM)
 * @since 2020-04-09
 *
 */

public class LogInfo {
	
	private String _name = "";
	
	private String _path = "";
	
	
	public LogInfo(String logName, String logPath) {
		super();
		this._name = logName;
		this._path = logPath;
	}
	
	public String getName() {
		return _name;
	}
	public void setName(String logName) {
		this._name = logName;
	}
	
	public String getPath() {
		return _path;
	}
	public void setPath(String logPath) {
		this._path = logPath;
	}
	
	

}
