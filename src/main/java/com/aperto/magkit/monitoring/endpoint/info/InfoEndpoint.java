package com.aperto.magkit.monitoring.endpoint.info;

import java.io.FileReader;
import java.io.IOException;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import com.aperto.magkit.monitoring.endpoint.AbstractMonitoringEndpoint;
import com.aperto.magkit.monitoring.endpoint.MonitoringEndpointDefinition;

import info.magnolia.cms.pddescriptor.ProductDescriptorExtractor;
import info.magnolia.context.MgnlContext;
import info.magnolia.context.WebContext;
import info.magnolia.repository.DefaultRepositoryManager;
import info.magnolia.rest.DynamicPath;

/**
 * 
 * Info Endpoint.
 * 
 * @author Claudiu Gonciulea (Aperto - An IBM Company)
 * @since 2020-04-09
 *
 */
@Path("")
@DynamicPath
public class InfoEndpoint extends AbstractMonitoringEndpoint<MonitoringEndpointDefinition> {
	
	ProductDescriptorExtractor pde;
	DefaultRepositoryManager drm;
	
	@Inject
    protected InfoEndpoint(MonitoringEndpointDefinition endpointDefinition, ProductDescriptorExtractor pde, DefaultRepositoryManager drm) {
        super(endpointDefinition);
        this.pde = pde;
        this.drm = drm;
    }

    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Info info() {
    	
    	Info info = new Info();
    	
    	info.setMagnoliaEdition(pde.get(ProductDescriptorExtractor.EDITION));
    	info.setMagnoliaVersion(getMagnoliaVersion());
    	info.setMagnoliaContext(getMagnoliaInstanceType());
    	info.setOperatingSystem(System.getProperty("os.name"));
    	info.setJavaVersion(System.getProperty("java.vendor") + " " + System.getProperty("java.version"));
    	info.setApplicationServer(getApplicationServer());
    	// TODO set _database
    	info.setRepository(getGitRepository());

        return info;
    }
    
    private String getMagnoliaVersion() { // TODO replace and get from ProductDescriptorExtractor ?
    	
    	String version = null;
    	
    	try {
			Session session = MgnlContext.getJCRSession("config");
			Node coreNode = session.getNode("/modules/core");
			version = coreNode.getProperty("version").getString();
		} catch (RepositoryException e) {
			
			e.printStackTrace(); // TODO deal with exception message
		}
    	
    	return version;
    }
    
    private String getMagnoliaInstanceType() {
    	
    	String version = null;
    	
    	try {
			Session session = MgnlContext.getJCRSession("config");
			Node serverNode = session.getNode("/server");
			version = serverNode.getProperty("admin").getString();
		} catch (RepositoryException e) {
			
			e.printStackTrace(); // TODO deal with exception message
		}
    	
    	if (version.equals("true")) {
    		return "AUTHOR";
    	} else {
    		return "PUBLIC";
    	}
    }
    
    private String getGitRepository() {
    	
    	String repository = null;
    	
    	MavenXpp3Reader reader = new MavenXpp3Reader();
        try {
			Model model = reader.read(new FileReader("pom.xml"));
			repository = model.getScm().getConnection();
		} catch (IOException | XmlPullParserException e) {

			e.printStackTrace(); // TODO deal with exception message
		}
        
        return repository;
    }
    
    private String getApplicationServer() {
    	
    	WebContext web = MgnlContext.getWebContext();
    	String appServer = web.getServletContext().getServerInfo();

    	return appServer;
    }
}
