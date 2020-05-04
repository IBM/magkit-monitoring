package com.aperto.magkit.monitoring.endpoint.info;

import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aperto.magkit.monitoring.endpoint.AbstractMonitoringEndpoint;
import com.aperto.magkit.monitoring.endpoint.MonitoringEndpointDefinition;
import com.aperto.magkit.monitoring.endpoint.info.pojo.Environment;
import com.aperto.magkit.monitoring.endpoint.info.pojo.Info;
import com.aperto.magkit.monitoring.endpoint.info.pojo.License;
import com.aperto.magkit.monitoring.endpoint.info.pojo.Magnolia;
import com.aperto.magkit.monitoring.endpoint.info.service.InfoService;

import info.magnolia.cms.pddescriptor.ProductDescriptorExtractor;
import info.magnolia.rest.DynamicPath;

/**
 * 
 * Info Endpoint.
 * 
 * @author CLAUDIU GONCIULEA (Aperto - An IBM Company)
 * @since 2020-04-09
 *
 */
@Path("")
@DynamicPath
public class InfoEndpoint extends AbstractMonitoringEndpoint<MonitoringEndpointDefinition> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InfoEndpoint.class);

	private InfoService infoService;
	
	private ProductDescriptorExtractor pde;
	
	@Inject
    protected InfoEndpoint(MonitoringEndpointDefinition endpointDefinition, ProductDescriptorExtractor pde, InfoService infoService) {
        
		super(endpointDefinition);
        
		this.infoService = infoService;
		this.pde = pde;
    }

    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Info info() throws IOException {
    	
    	Info info = new Info();
    	
    	Magnolia magnolia = new Magnolia();
    	License license = new License();
    	Environment environment = new Environment();
    	
    	
    	magnolia.setEdition(pde.get(ProductDescriptorExtractor.EDITION));
    	magnolia.setVersion(infoService.getMagnoliaVersion());
    	magnolia.setInstance(infoService.getMagnoliaInstanceType());
    	
    	String LICENSE_FILE_PATH = "info/magnolia/cms/pddescriptor/pddescriptor.xml";
    	String PLATFORM_COMPONENTS = "/info/magnolia/init/platform-components.xml";
    	InputStream is = getClass().getClassLoader().getResourceAsStream(PLATFORM_COMPONENTS);
    	int i;
    	char c;
    	
    	try {
            System.out.println("pddescriptor.xml printed:");
            
            while((i = is.read())!=-1) {

               c = (char)i;
               System.out.print(c);
            }
            
         } catch(Exception e) {
        	 LOGGER.error(e.getMessage(), e);
         } finally {
            if(is!=null)
               is.close();
         }
    	

    	// TODO set license fields value
    	magnolia.setLicense(license);

    	
    	environment.setOperatingSystem(System.getProperty("os.name"));
    	environment.setJavaVersion(System.getProperty("java.vendor") + " " + System.getProperty("java.version"));
    	environment.setApplicationServer(infoService.getApplicationServer());
    	// TODO set _database, _dbDriver
    	environment.setRepository(infoService.getJcrRepository());
    	
    	info.setMagnolia(magnolia);
    	info.setEnvironment(environment);

        return info;
    }
}
