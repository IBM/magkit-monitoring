/*-
 * #%L
 * IBM iX Magnolia Monitoring
 * %%
 * Copyright (C) 2025 IBM iX
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
package de.ibmix.magkit.monitoring.endpoint.info;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.ibmix.magkit.monitoring.endpoint.MonitoringEndpointDefinition;
import de.ibmix.magkit.monitoring.endpoint.info.pojo.Info;
import de.ibmix.magkit.monitoring.endpoint.info.pojo.Magnolia;
import de.ibmix.magkit.monitoring.endpoint.info.pojo.License;
import de.ibmix.magkit.monitoring.endpoint.info.pojo.Environment;
import info.magnolia.about.app.InstanceConfigurationProvider;
import info.magnolia.license.LicenseManager;
import info.magnolia.license.LicenseConsts;

/**
 * Unit tests for {@link InfoEndpoint} covering author/public instance selection, license date formatting and environment population.
 * Ensures proper mapping of configuration provider and license manager data into the {@link Info} aggregate DTO.
 * @author GitHub Copilot
 * @since 2025-11-12
 */
public class InfoEndpointTest {

    /**
     * Verifies info() populates Magnolia data for an author instance including edition, version, instance type and formatted license date.
     * @throws IOException if endpoint invocation throws
     */
    @Test
    public void testInfoAuthorInstanceDataPopulation() throws IOException {
        MonitoringEndpointDefinition endpointDefinition = Mockito.mock(MonitoringEndpointDefinition.class);
        InstanceConfigurationProvider configurationProvider = Mockito.mock(InstanceConfigurationProvider.class);
        LicenseManager licenseManager = Mockito.mock(LicenseManager.class);
        info.magnolia.license.License magnoliaLicense = Mockito.mock(info.magnolia.license.License.class);
        Mockito.when(configurationProvider.getEditionName()).thenReturn("Enterprise");
        Mockito.when(configurationProvider.getMagnoliaVersion()).thenReturn("7.0.1");
        Mockito.when(configurationProvider.isAdmin()).thenReturn(true);
        Mockito.when(configurationProvider.getApplicationServer()).thenReturn("Tomcat/10.1");
        Mockito.when(configurationProvider.getDatabase()).thenReturn("PostgreSQL 14");
        Mockito.when(configurationProvider.getDatabaseDriver()).thenReturn("org.postgresql.Driver");
        Mockito.when(configurationProvider.getJcrName()).thenReturn("Jackrabbit");
        Mockito.when(configurationProvider.getJcrVersion()).thenReturn("2.20.0");
        LocalDate expiration = LocalDate.parse("2030-06-15", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Mockito.when(magnoliaLicense.getOwner()).thenReturn("Example Corp");
        Mockito.when(magnoliaLicense.getValidityEndDate()).thenReturn(expiration);
        Mockito.when(licenseManager.getLicense(LicenseConsts.MODULE_ENTERPRISE)).thenReturn(magnoliaLicense);
        InfoEndpoint endpoint = new InfoEndpoint(endpointDefinition, configurationProvider, licenseManager);
        Info info = endpoint.info();
        assertNotNull(info);
        Magnolia magnolia = info.getMagnolia();
        assertNotNull(magnolia);
        assertEquals("Enterprise", magnolia.getEdition());
        assertEquals("7.0.1", magnolia.getVersion());
        assertEquals("author", magnolia.getInstance());
        License license = magnolia.getLicense();
        assertNotNull(license);
        assertEquals("Example Corp", license.getOwner());
        assertEquals("2030-06-15", license.getExpirationDate());
        Environment environment = info.getEnvironment();
        assertNotNull(environment);
        assertEquals("Tomcat/10.1", environment.getApplicationServer());
        assertEquals("PostgreSQL 14", environment.getDatabase());
        assertEquals("org.postgresql.Driver", environment.getDbDriver());
        assertEquals("Jackrabbit 2.20.0", environment.getRepository());
        String expectedJavaVersion = System.getProperty("java.vendor") + " " + System.getProperty("java.version");
        assertEquals(expectedJavaVersion, environment.getJavaVersion());
        assertEquals(System.getProperty("os.name"), environment.getOperatingSystem());
    }

    /**
     * Verifies info() sets instance type to public when configuration provider reports non-admin and populates remaining fields equivalently.
     * @throws IOException if endpoint invocation throws
     */
    @Test
    public void testInfoPublicInstanceDataPopulation() throws IOException {
        MonitoringEndpointDefinition endpointDefinition = Mockito.mock(MonitoringEndpointDefinition.class);
        InstanceConfigurationProvider configurationProvider = Mockito.mock(InstanceConfigurationProvider.class);
        LicenseManager licenseManager = Mockito.mock(LicenseManager.class);
        info.magnolia.license.License magnoliaLicense = Mockito.mock(info.magnolia.license.License.class);
        Mockito.when(configurationProvider.getEditionName()).thenReturn("Community");
        Mockito.when(configurationProvider.getMagnoliaVersion()).thenReturn("7.0.1");
        Mockito.when(configurationProvider.isAdmin()).thenReturn(false);
        Mockito.when(configurationProvider.getApplicationServer()).thenReturn("Jetty/11");
        Mockito.when(configurationProvider.getDatabase()).thenReturn("H2 2.0");
        Mockito.when(configurationProvider.getDatabaseDriver()).thenReturn("org.h2.Driver");
        Mockito.when(configurationProvider.getJcrName()).thenReturn("Oak");
        Mockito.when(configurationProvider.getJcrVersion()).thenReturn("1.48.0");
        LocalDate expiration = LocalDate.parse("2028-12-31", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Mockito.when(magnoliaLicense.getOwner()).thenReturn("Public Owner");
        Mockito.when(magnoliaLicense.getValidityEndDate()).thenReturn(expiration);
        Mockito.when(licenseManager.getLicense(LicenseConsts.MODULE_ENTERPRISE)).thenReturn(magnoliaLicense);
        InfoEndpoint endpoint = new InfoEndpoint(endpointDefinition, configurationProvider, licenseManager);
        Info info = endpoint.info();
        Magnolia magnolia = info.getMagnolia();
        assertEquals("public", magnolia.getInstance());
        assertEquals("Community", magnolia.getEdition());
        License license = magnolia.getLicense();
        assertEquals("Public Owner", license.getOwner());
        assertEquals("2028-12-31", license.getExpirationDate());
        Environment environment = info.getEnvironment();
        assertEquals("Oak 1.48.0", environment.getRepository());
        assertEquals("Jetty/11", environment.getApplicationServer());
        assertEquals("H2 2.0", environment.getDatabase());
        assertEquals("org.h2.Driver", environment.getDbDriver());
    }

    /**
     * Verifies each invocation of info() returns a newly constructed {@link Info} instance (stateless behavior).
     * @throws IOException if endpoint invocation throws
     */
    @Test
    public void testInfoReturnsNewInstancePerCall() throws IOException {
        MonitoringEndpointDefinition endpointDefinition = Mockito.mock(MonitoringEndpointDefinition.class);
        InstanceConfigurationProvider configurationProvider = Mockito.mock(InstanceConfigurationProvider.class);
        LicenseManager licenseManager = Mockito.mock(LicenseManager.class);
        info.magnolia.license.License magnoliaLicense = Mockito.mock(info.magnolia.license.License.class);
        Mockito.when(configurationProvider.getEditionName()).thenReturn("Enterprise");
        Mockito.when(configurationProvider.getMagnoliaVersion()).thenReturn("7.0.1");
        Mockito.when(configurationProvider.isAdmin()).thenReturn(true);
        Mockito.when(configurationProvider.getApplicationServer()).thenReturn("Tomcat/10.1");
        Mockito.when(configurationProvider.getDatabase()).thenReturn("PostgreSQL 14");
        Mockito.when(configurationProvider.getDatabaseDriver()).thenReturn("org.postgresql.Driver");
        Mockito.when(configurationProvider.getJcrName()).thenReturn("Jackrabbit");
        Mockito.when(configurationProvider.getJcrVersion()).thenReturn("2.20.0");
        LocalDate expiration = LocalDate.parse("2030-06-15", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Mockito.when(magnoliaLicense.getOwner()).thenReturn("Example Corp");
        Mockito.when(magnoliaLicense.getValidityEndDate()).thenReturn(expiration);
        Mockito.when(licenseManager.getLicense(LicenseConsts.MODULE_ENTERPRISE)).thenReturn(magnoliaLicense);
        InfoEndpoint endpoint = new InfoEndpoint(endpointDefinition, configurationProvider, licenseManager);
        Info info1 = endpoint.info();
        Info info2 = endpoint.info();
        assertNotSame(info1, info2);
        assertTrue(info1.getMagnolia() != null && info2.getMagnolia() != null);
    }
}

