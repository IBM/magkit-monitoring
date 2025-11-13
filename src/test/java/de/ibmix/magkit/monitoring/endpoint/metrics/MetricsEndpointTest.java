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
package de.ibmix.magkit.monitoring.endpoint.metrics;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import de.ibmix.magkit.monitoring.endpoint.MonitoringEndpointDefinition;

/**
 * Unit tests for {@link MetricsEndpoint} ensuring delegation to {@link MetricsService}.
 * Verifies returned DTO is the one provided by the service and not recreated.
 * @author GitHub Copilot
 * @since 2025-11-12
 */
public class MetricsEndpointTest {

    /**
     * Verifies getMetrics() delegates directly to service and returns its result.
     */
    @Test
    public void testGetMetricsDelegatesToService() {
        MonitoringEndpointDefinition definition = mock(MonitoringEndpointDefinition.class);
        MetricsService service = mock(MetricsService.class);
        MetricsInfo info = new MetricsInfo();
        when(service.getInfoMetrics()).thenReturn(info);
        MetricsEndpoint endpoint = new MetricsEndpoint(definition, service);
        MetricsInfo result = endpoint.getMetrics();
        assertNotNull(result);
        assertSame(info, result);
        verify(service, times(1)).getInfoMetrics();
    }
}

