package com.aperto.magkit.monitoring.endpoint.logs;

public class Logs {

	private String statusMessage = "No Log File Selected!";
	
	private String logName;
	
	//TODO: find the proper type to accommodate a long log
	private String logContents;

	public Logs () {}
	
	public Logs(String logName) {
		this.logName = logName;
		this.statusMessage = "";
	}
	
	public Logs(String logName, String logContents) {
		this.logName = logName;
		this.logContents = logContents;
		this.statusMessage = "";
	}


	public String getLogName() {
		return logName;
	}

	public void setLogName(String logName) {
		this.logName = logName;
	}

	public String getLogContents() {
		return logContents;
	}

	public void setLogContents(String logContents) {
		this.logContents = logContents;
	}

}
