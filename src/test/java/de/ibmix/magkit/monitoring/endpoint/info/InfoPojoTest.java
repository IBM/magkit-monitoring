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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import de.ibmix.magkit.monitoring.endpoint.info.pojo.Info;
import de.ibmix.magkit.monitoring.endpoint.info.pojo.Magnolia;
import de.ibmix.magkit.monitoring.endpoint.info.pojo.License;
import de.ibmix.magkit.monitoring.endpoint.info.pojo.Environment;

/**
 * Unit tests for info endpoint POJOs ({@link Info}, {@link Magnolia}, {@link License}, {@link Environment}) verifying setter/getter behavior including null assignment cases.
 * Ensures each property is mutable and getters return exact references provided to setters.
 * @author GitHub Copilot
 * @since 2025-11-12
 */
public class InfoPojoTest {

    /**
     * Verifies Magnolia setters and getters including null cases.
     */
    @Test
    public void testMagnoliaPojoSetters() {
        Magnolia magnolia = new Magnolia();
        magnolia.setEdition("Enterprise");
        magnolia.setVersion("7.0.1");
        magnolia.setInstance("author");
        License license = new License();
        license.setOwner("Example Corp");
        license.setExpirationDate("2030-06-15");
        magnolia.setLicense(license);
        assertEquals("Enterprise", magnolia.getEdition());
        assertEquals("7.0.1", magnolia.getVersion());
        assertEquals("author", magnolia.getInstance());
        assertSame(license, magnolia.getLicense());
        magnolia.setEdition(null);
        magnolia.setVersion(null);
        magnolia.setInstance(null);
        magnolia.setLicense(null);
        assertNull(magnolia.getEdition());
        assertNull(magnolia.getVersion());
        assertNull(magnolia.getInstance());
        assertNull(magnolia.getLicense());
    }

    /**
     * Verifies License setters and getters including null cases.
     */
    @Test
    public void testLicensePojoSetters() {
        License license = new License();
        license.setOwner("Owner");
        license.setExpirationDate("2028-12-31");
        assertEquals("Owner", license.getOwner());
        assertEquals("2028-12-31", license.getExpirationDate());
        license.setOwner(null);
        license.setExpirationDate(null);
        assertNull(license.getOwner());
        assertNull(license.getExpirationDate());
    }

    /**
     * Verifies Environment setters and getters including null cases.
     */
    @Test
    public void testEnvironmentPojoSetters() {
        Environment environment = new Environment();
        environment.setOperatingSystem("Linux");
        environment.setJavaVersion("Eclipse Adoptium 17");
        environment.setApplicationServer("Tomcat/10.1");
        environment.setDatabase("PostgreSQL 14");
        environment.setDbDriver("org.postgresql.Driver");
        environment.setRepository("Jackrabbit 2.20.0");
        assertEquals("Linux", environment.getOperatingSystem());
        assertEquals("Eclipse Adoptium 17", environment.getJavaVersion());
        assertEquals("Tomcat/10.1", environment.getApplicationServer());
        assertEquals("PostgreSQL 14", environment.getDatabase());
        assertEquals("org.postgresql.Driver", environment.getDbDriver());
        assertEquals("Jackrabbit 2.20.0", environment.getRepository());
        environment.setOperatingSystem(null);
        environment.setJavaVersion(null);
        environment.setApplicationServer(null);
        environment.setDatabase(null);
        environment.setDbDriver(null);
        environment.setRepository(null);
        assertNull(environment.getOperatingSystem());
        assertNull(environment.getJavaVersion());
        assertNull(environment.getApplicationServer());
        assertNull(environment.getDatabase());
        assertNull(environment.getDbDriver());
        assertNull(environment.getRepository());
    }

    /**
     * Verifies Info setters and getters including null cases.
     */
    @Test
    public void testInfoPojoSetters() {
        Info info = new Info();
        Magnolia magnolia = new Magnolia();
        Environment environment = new Environment();
        info.setMagnolia(magnolia);
        info.setEnvironment(environment);
        assertSame(magnolia, info.getMagnolia());
        assertSame(environment, info.getEnvironment());
        info.setMagnolia(null);
        info.setEnvironment(null);
        assertNull(info.getMagnolia());
        assertNull(info.getEnvironment());
    }

    /**
     * Smoke test constructing full object graph.
     */
    @Test
    public void testFullGraphConstruction() {
        Info info = new Info();
        Magnolia magnolia = new Magnolia();
        License license = new License();
        Environment environment = new Environment();
        license.setOwner("Corp");
        license.setExpirationDate("2031-01-01");
        magnolia.setEdition("Enterprise");
        magnolia.setVersion("7.1.0");
        magnolia.setInstance("author");
        magnolia.setLicense(license);
        environment.setOperatingSystem("Linux");
        info.setMagnolia(magnolia);
        info.setEnvironment(environment);
        assertNotNull(info.getMagnolia());
        assertNotNull(info.getEnvironment());
        assertEquals("Enterprise", info.getMagnolia().getEdition());
        assertEquals("Linux", info.getEnvironment().getOperatingSystem());
    }
}

