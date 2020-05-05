package com.aperto.magkit.monitoring.endpoint.info;

import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.inject.Inject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.aperto.magkit.monitoring.endpoint.AbstractMonitoringEndpoint;
import com.aperto.magkit.monitoring.endpoint.MonitoringEndpointDefinition;
import com.aperto.magkit.monitoring.endpoint.info.pojo.Environment;
import com.aperto.magkit.monitoring.endpoint.info.pojo.Info;
import com.aperto.magkit.monitoring.endpoint.info.pojo.License;
import com.aperto.magkit.monitoring.endpoint.info.pojo.Magnolia;

import info.magnolia.about.app.InstanceConfigurationProvider;
import info.magnolia.license.LicenseConsts;
import info.magnolia.license.LicenseManager;
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
	
	private static final String AUTHOR = "author";
	private static final String PUBLIC = "public";
	
	private InstanceConfigurationProvider configurationProvider;
	private LicenseManager licenseManager;
	
	
	@Inject
    protected InfoEndpoint(MonitoringEndpointDefinition endpointDefinition, InstanceConfigurationProvider configurationProvider, LicenseManager licenseManager) {
        
		super(endpointDefinition);
        
		this.configurationProvider = configurationProvider;
		this.licenseManager = licenseManager;
    }

    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Info info() throws IOException {
    	
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    	
    	Info info = new Info();
    	
    	Magnolia magnolia = new Magnolia();
    	License license = new License();
    	Environment environment = new Environment();

    	magnolia.setEdition(configurationProvider.getEditionName());
    	magnolia.setVersion(configurationProvider.getMagnoliaVersion());
    	magnolia.setInstance(configurationProvider.isAdmin() == true ? AUTHOR : PUBLIC);
    	
    	info.magnolia.license.License magnoliaLicense = licenseManager.getLicense(LicenseConsts.MODULE_ENTERPRISE);

    	license.setOwner(magnoliaLicense.getOwner());
    	license.setExpirationDate(formatter.format(magnoliaLicense.getExpirationDate()));
    	
    	magnolia.setLicense(license);
    	
    	environment.setOperatingSystem(System.getProperty("os.name"));
    	environment.setJavaVersion(System.getProperty("java.vendor") + " " + System.getProperty("java.version"));
    	environment.setApplicationServer(configurationProvider.getApplicationServer());
    	environment.setDatabase(configurationProvider.getDatabase());
    	environment.setDbDriver(configurationProvider.getDatabaseDriver());
    	environment.setRepository(configurationProvider.getJcrName() + " " + configurationProvider.getJcrVersion());
    	
    	info.setMagnolia(magnolia);
    	info.setEnvironment(environment);

        return info;
    }
}
