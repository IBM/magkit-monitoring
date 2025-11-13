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
package de.ibmix.magkit.monitoring.endpoint.health;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.ibmix.magkit.monitoring.endpoint.MonitoringEndpointDefinition;

/**
 * Unit tests for {@link HealthEndpoint} covering instance construction and health method response.
 * Ensures a new {@link Health} object is returned each invocation with default status value "UP".
 * @author GitHub Copilot
 * @since 2025-11-12
 */
public class HealthEndpointTest {

    /**
     * Verifies health() returns non-null Health DTO with default status "UP".
     */
    @Test
    public void testHealthDefaultStatus() {
        MonitoringEndpointDefinition def = Mockito.mock(MonitoringEndpointDefinition.class);
        HealthEndpoint endpoint = new HealthEndpoint(def);
        Health h1 = endpoint.health();
        assertNotNull(h1);
        assertEquals("UP", h1.getStatus());
    }

    /**
     * Verifies each call returns a new instance (stateless behavior) allowing future mutation isolation.
     */
    @Test
    public void testHealthReturnsNewInstancePerCall() {
        MonitoringEndpointDefinition def = Mockito.mock(MonitoringEndpointDefinition.class);
        HealthEndpoint endpoint = new HealthEndpoint(def);
        Health h1 = endpoint.health();
        Health h2 = endpoint.health();
        assertNotSame(h1, h2);
    }
}
