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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;
import org.junit.jupiter.api.Test;

import de.ibmix.magkit.monitoring.MonitoringModule;
import de.ibmix.magkit.monitoring.config.prometheus.HttpRequestMetricsConfig;
import de.ibmix.magkit.monitoring.config.prometheus.PrometheusConfig;

/**
 * Unit tests for {@link PrometheusFilter} covering conditional activation and tag generation.
 * Ensures no timing when http metric disabled and timing recorded when enabled with proper tags.
 * @author GitHub Copilot
 * @since 2025-11-13
 */
public class PrometheusFilterTest {

    /**
     * Verifies filter does not record metrics when http metric not present.
     * @throws IOException io error
     * @throws ServletException servlet error
     */
    @Test
    public void testNoMetricsWhenHttpNotEnabled() throws IOException, ServletException {
        PrometheusConfig prometheusConfig = new PrometheusConfig();
        prometheusConfig.setMetrics(List.of("JvmMemory"));
        HttpRequestMetricsConfig httpCfg = new HttpRequestMetricsConfig();
        prometheusConfig.setHttpRequestMetricsConfig(httpCfg);
        MonitoringModule module = mock(MonitoringModule.class);
        when(module.getPrometheusConfig()).thenReturn(prometheusConfig);
        PrometheusMeterRegistry registry = new PrometheusMeterRegistry(io.micrometer.prometheusmetrics.PrometheusConfig.DEFAULT);
        PrometheusFilter filter = new PrometheusFilter(module, registry);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        when(request.getRequestURI()).thenReturn("/test/uri");
        when(request.getMethod()).thenReturn("GET");
        when(response.getStatus()).thenReturn(200);
        filter.doFilter(request, response, chain);
        verify(chain).doFilter(request, response);
        assertTrue(registry.find("http_server_requests").timers().isEmpty());
    }

    /**
     * Verifies filter records timer with expected tags when http metric enabled.
     * @throws IOException io error
     * @throws ServletException servlet error
     */
    @Test
    public void testRecordsTimerWhenHttpEnabled() throws IOException, ServletException {
        PrometheusConfig prometheusConfig = new PrometheusConfig();
        prometheusConfig.setMetrics(List.of("http"));
        HttpRequestMetricsConfig httpCfg = new HttpRequestMetricsConfig();
        prometheusConfig.setHttpRequestMetricsConfig(httpCfg);
        MonitoringModule module = mock(MonitoringModule.class);
        when(module.getPrometheusConfig()).thenReturn(prometheusConfig);
        PrometheusMeterRegistry registry = new PrometheusMeterRegistry(io.micrometer.prometheusmetrics.PrometheusConfig.DEFAULT);
        PrometheusFilter filter = new PrometheusFilter(module, registry);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        when(request.getRequestURI()).thenReturn("/test/uri");
        when(request.getMethod()).thenReturn("POST");
        when(response.getStatus()).thenReturn(201);
        doAnswer(invocation -> null).when(chain).doFilter(request, response);
        filter.doFilter(request, response, chain);
        assertEquals(1, registry.find("http_server_requests").timers().size());
        var timer = registry.find("http_server_requests").timer();
        assertNotNull(timer);
        assertEquals("/test/uri", timer.getId().getTag("uri"));
        assertEquals("POST", timer.getId().getTag("method"));
        assertEquals("201", timer.getId().getTag("status"));
    }
}
