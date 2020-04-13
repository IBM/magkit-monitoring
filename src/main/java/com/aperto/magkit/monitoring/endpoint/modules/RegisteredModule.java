package com.aperto.magkit.monitoring.endpoint.modules;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.version.Version;

public class RegisteredModule {
	private String name;
	private String version;
	
	
	public RegisteredModule (Node module) {
		try {
			this.name = module.getName();
			this.version =  module.getProperty("version").getValue().getString();
			
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	
}
