package com.aperto.magkit.monitoring.endpoint.modules;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.version.Version;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aperto.magkit.monitoring.endpoint.info.InfoEndpoint;

public class RegisteredModule {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RegisteredModule.class);
	
	private String name;
	private String version;
	
	
	public RegisteredModule (Node module) {
		try {
			this.name = module.getName();
			this.version =  module.getProperty("version").getValue().getString();
			
		} catch (RepositoryException e) {
			LOGGER.error(e.getMessage(), e);
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
