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
 * Provides aggregated runtime and license information about the running Magnolia instance.
 * Combines environment details (OS, JVM, application server, DB, repository), Magnolia version/edition and license
 * metadata into a single JSON structure for operational insight and inventory auditing.
 * <p><strong>Purpose</strong></p>
 * Supplies a consolidated view of system profile and licensing enabling monitoring tools to assess deployment state.
 * <p><strong>Main Functionality</strong></p>
 * Gathers edition/version/instance via {@link info.magnolia.about.app.InstanceConfigurationProvider}, retrieves enterprise license owner and expiration via {@link info.magnolia.license.LicenseManager}, assembles environment details from system properties and provider methods into nested POJOs.
 * <p><strong>Key Features</strong></p>
 * <ul>
 * <li>Reads instance configuration via {@link info.magnolia.about.app.InstanceConfigurationProvider}.</li>
 * <li>Extracts enterprise license owner and expiration via {@link info.magnolia.license.LicenseManager}.</li>
 * <li>Builds nested POJOs ({@link de.ibmix.magkit.monitoring.endpoint.info.pojo.Magnolia}, {@link de.ibmix.magkit.monitoring.endpoint.info.pojo.License}, {@link de.ibmix.magkit.monitoring.endpoint.info.pojo.Environment}).</li>
 * </ul>
 * <p><strong>Usage Preconditions</strong></p>
 * Magnolia dependency injection must provide required services.
 * <p><strong>Side Effects</strong></p>
 * Performs license manager lookups and system property reads; no persistent state changes.
 * <p><strong>Null and Error Handling</strong></p>
 * May throw {@link java.io.IOException} originating from underlying configuration access. Returns fully populated, non-null {@link de.ibmix.magkit.monitoring.endpoint.info.pojo.Info} on success.
 * <p><strong>Thread-Safety</strong></p>
 * Stateless after construction; safe for concurrent GET requests.
 * <p><strong>Usage Example</strong></p>
 * <pre>{@code
 * Info info = infoEndpoint.info();
 * }</pre>
 * <p><strong>Important Details</strong></p>
 * License expiration date is formatted yyyy-MM-dd; consumers should parse accordingly and consider timezone neutrality.
 * @author CLAUDIU GONCIULEA (IBM iX)
 * @since 2020-04-09
 */
@Path("")
@DynamicPath
public class InfoEndpoint extends AbstractMonitoringEndpoint<MonitoringEndpointDefinition> {

    private static final String AUTHOR = "author";
    private static final String PUBLIC = "public";

    private final InstanceConfigurationProvider _configurationProvider;
    private final LicenseManager _licenseManager;

    /**
     * Injection constructor wiring required Magnolia services.
     * @param endpointDefinition monitoring endpoint definition metadata
     * @param configurationProvider instance configuration provider supplying edition, version and environment settings
     * @param licenseManager Magnolia license manager for enterprise module license lookup
     */
    @Inject
    protected InfoEndpoint(MonitoringEndpointDefinition endpointDefinition,
            InstanceConfigurationProvider configurationProvider, LicenseManager licenseManager) {
        super(endpointDefinition);
        _configurationProvider = configurationProvider;
        _licenseManager = licenseManager;
    }

    /**
     * Builds and returns aggregated instance information including Magnolia and environment data plus license details.
     * @return populated info descriptor; never null on success
     * @throws IOException if configuration access encounters an I/O problem
     */
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
