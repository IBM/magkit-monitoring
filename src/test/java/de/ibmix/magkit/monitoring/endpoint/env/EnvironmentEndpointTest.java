/*-
 * #%L
 * IBM iX Magnolia Monitoring
 * %%
 * Copyright (C) 2023 - 2025 IBM iX
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
package de.ibmix.magkit.monitoring.endpoint.env;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.ibmix.magkit.monitoring.endpoint.MonitoringEndpointDefinition;
import info.magnolia.init.MagnoliaConfigurationProperties;

/**
 * Unit tests for {@link EnvironmentEndpoint} covering environment assembly and Magnolia property filtering excluding system properties.
 * Verifies Magnolia-only properties are returned while overlapping keys are skipped.
 * @author GitHub Copilot
 * @since 2025-11-12
 */
public class EnvironmentEndpointTest {

    /**
     * Verifies env() populates DTO and Magnolia properties excludes system property keys.
     */
    @Test
    public void testEnvPopulatesAndFiltersMagnoliaProperties() {
        MonitoringEndpointDefinition def = Mockito.mock(MonitoringEndpointDefinition.class);
        MagnoliaConfigurationProperties conf = Mockito.mock(MagnoliaConfigurationProperties.class);
        Set<String> keys = new HashSet<>();
        keys.add("java.version");
        keys.add("magnolia.cache.enabled");
        Mockito.when(conf.getKeys()).thenReturn(keys);
        Mockito.when(conf.getProperty("java.version")).thenReturn("17");
        Mockito.when(conf.getProperty("magnolia.cache.enabled")).thenReturn("true");
        EnvironmentEndpoint endpoint = new EnvironmentEndpoint(def, conf);
        Environment env = endpoint.env();
        assertNotNull(env);
        Map<String, String> mgnlProps = env.getMagnoliaProperties();
        assertTrue(!mgnlProps.containsKey("java.version"));
        assertEquals("true", mgnlProps.get("magnolia.cache.enabled"));
    }
}

