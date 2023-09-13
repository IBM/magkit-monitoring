package de.ibmix.magkit.monitoring.endpoint.info;

/*-
 * #%L
 * IBM iX Magnolia Monitoring
 * %%
 * Copyright (C) 2023 IBM iX
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.inject.Inject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.ibmix.magkit.monitoring.endpoint.AbstractMonitoringEndpoint;
import de.ibmix.magkit.monitoring.endpoint.MonitoringEndpointDefinition;
import de.ibmix.magkit.monitoring.endpoint.info.pojo.Environment;
import de.ibmix.magkit.monitoring.endpoint.info.pojo.Info;
import de.ibmix.magkit.monitoring.endpoint.info.pojo.License;
import de.ibmix.magkit.monitoring.endpoint.info.pojo.Magnolia;

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

    private InstanceConfigurationProvider _configurationProvider;
    private LicenseManager _licenseManager;

    @Inject
    protected InfoEndpoint(MonitoringEndpointDefinition endpointDefinition,
            InstanceConfigurationProvider configurationProvider, LicenseManager licenseManager) {

        super(endpointDefinition);

        _configurationProvider = configurationProvider;
        _licenseManager = licenseManager;
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

        magnolia.setEdition(_configurationProvider.getEditionName());
        magnolia.setVersion(_configurationProvider.getMagnoliaVersion());
        magnolia.setInstance(_configurationProvider.isAdmin() ? AUTHOR : PUBLIC);

        info.magnolia.license.License magnoliaLicense = _licenseManager.getLicense(LicenseConsts.MODULE_ENTERPRISE);

        license.setOwner(magnoliaLicense.getOwner());
        license.setExpirationDate(formatter.format(magnoliaLicense.getExpirationDate()));

        magnolia.setLicense(license);

        environment.setOperatingSystem(System.getProperty("os.name"));
        environment.setJavaVersion(System.getProperty("java.vendor") + " " + System.getProperty("java.version"));
        environment.setApplicationServer(_configurationProvider.getApplicationServer());
        environment.setDatabase(_configurationProvider.getDatabase());
        environment.setDbDriver(_configurationProvider.getDatabaseDriver());
        environment.setRepository(_configurationProvider.getJcrName() + " " + _configurationProvider.getJcrVersion());

        info.setMagnolia(magnolia);
        info.setEnvironment(environment);

        return info;
    }
}
