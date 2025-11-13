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
package de.ibmix.magkit.monitoring.endpoint.prometheus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import io.micrometer.prometheusmetrics.PrometheusConfig;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;
import org.junit.jupiter.api.Test;

import de.ibmix.magkit.monitoring.endpoint.MonitoringEndpointDefinition;


/**
 * Unit tests for {@link PrometheusEndpoint} ensuring delegation to registry scrape.
 * Verifies that returned text equals underlying registry scrape output.
 * @author GitHub Copilot
 * @since 2025-11-13
 */
public class PrometheusEndpointTest {

    /**
     * Ensures prometheus() returns same scrape output as registry.
     */
    @Test
    public void testPrometheusDelegatesToRegistry() {
        MonitoringEndpointDefinition definition = mock(MonitoringEndpointDefinition.class);
        PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        registry.counter("test_counter").increment();
        String expected = registry.scrape();
        PrometheusEndpoint endpoint = new PrometheusEndpoint(definition, registry);
        String result = endpoint.prometheus();
        assertNotNull(result);
        assertEquals(expected, result);
    }
}

