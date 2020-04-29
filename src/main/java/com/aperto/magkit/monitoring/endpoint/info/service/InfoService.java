package com.aperto.magkit.monitoring.endpoint.info.service;

import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.jackrabbit.commons.JcrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.magnolia.context.MgnlContext;
import info.magnolia.context.WebContext;

public class InfoService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InfoService.class);
	
	private static final String AUTHOR = "author";
	private static final String PUBLIC = "public";
	private static final String TRUE = "true";

	public String getMagnoliaVersion() { // TODO replace and get from ProductDescriptorExtractor ?
    	
    	String version = null;
    	
    	try {
			Session session = MgnlContext.getJCRSession("config");
			Node coreNode = session.getNode("/modules/core");
			version = coreNode.getProperty("version").getString();
		} catch (RepositoryException e) {
			LOGGER.error("Error occurred on getting magnolia version", e);
		}
    	
    	return version;
    }
    
    public String getMagnoliaInstanceType() {
    	
    	String version = null;
    	
    	try {
			Session session = MgnlContext.getJCRSession("config");
			Node serverNode = session.getNode("/server");
			version = serverNode.getProperty("admin").getString();
		} catch (RepositoryException e) {
			LOGGER.error("Error occurred on getting magnolia instance type", e);
		}
    	
    	if (version.equals(TRUE)) {
    		return AUTHOR;
    	} else {
    		return PUBLIC;
    	}
    }
    
    public String getApplicationServer() {
    	
    	WebContext web = MgnlContext.getWebContext();
    	String appServer = web.getServletContext().getServerInfo();

    	return appServer;
    }
    
    public String getJcrRepository() {
    	
    	String jcrRepositoryName = null;
    	String jcrRepositoryVersion = null;
    	
    	try {
	
    		jcrRepositoryName = JcrUtils.getRepository().getDescriptor(Repository.REP_NAME_DESC);
    		jcrRepositoryVersion = JcrUtils.getRepository().getDescriptor(Repository.REP_VERSION_DESC);
    		
		} catch (RepositoryException e) {
			LOGGER.error("Error occurred on getting repository description", e);
		}
    	
    	return jcrRepositoryName + " " + jcrRepositoryVersion;
    }
}
