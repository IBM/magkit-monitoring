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
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.List;

import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;
import org.junit.jupiter.api.Test;

import de.ibmix.magkit.monitoring.MonitoringModule;
import de.ibmix.magkit.monitoring.config.prometheus.HttpRequestMetricsConfig;
import de.ibmix.magkit.monitoring.config.prometheus.PrometheusConfig;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.Tags;

/**
 * Unit tests for {@link PrometheusMeterRegistryProvider} covering HTTP whitelist filter and SLO histogram configuration.
 * Ensures non-whitelisted URIs are blanked and SLO buckets applied to http_server_requests histogram config.
 * @author GitHub Copilot
 * @since 2025-11-13
 */
public class PrometheusMeterRegistryProviderTest {

    /**
     * Verifies http whitelist filter blanks non-whitelisted URIs.
     */
    @Test
    public void testHttpWhitelistFilterBlanksNonWhitelistedUris() {
        PrometheusConfig prometheusConfig = new PrometheusConfig();
        HttpRequestMetricsConfig httpCfg = new HttpRequestMetricsConfig();
        httpCfg.setUris(List.of("/allowed/.*"));
        prometheusConfig.setHttpRequestMetricsConfig(httpCfg);
        prometheusConfig.setMetrics(List.of("http"));
        MonitoringModule module = mock(MonitoringModule.class);
        when(module.getPrometheusConfig()).thenReturn(prometheusConfig);
        PrometheusMeterRegistryProvider provider = new PrometheusMeterRegistryProvider(module);
        PrometheusMeterRegistry registry = provider.get();
        Timer timer1 = registry.timer("http_server_requests", Tags.of(Tag.of("uri", "/allowed/test")));
        Timer timer2 = registry.timer("http_server_requests", Tags.of(Tag.of("uri", "/blocked/test")));
        assertEquals("/allowed/test", timer1.getId().getTag("uri"));
        assertEquals("", timer2.getId().getTag("uri"));
    }

    /**
     * Verifies SLO buckets configuration is applied (histogram count array length equals number of SLO buckets) when metrics contain http.
     */
    @Test
    public void testRequestHistogramFilterAppliesSloBuckets() {
        PrometheusConfig prometheusConfig = new PrometheusConfig();
        HttpRequestMetricsConfig httpCfg = new HttpRequestMetricsConfig();
        httpCfg.setUris(List.of("/any"));
        httpCfg.setSloBuckets(List.of(10, 20, 30));
        prometheusConfig.setHttpRequestMetricsConfig(httpCfg);
        prometheusConfig.setMetrics(List.of("http"));
        MonitoringModule module = mock(MonitoringModule.class);
        when(module.getPrometheusConfig()).thenReturn(prometheusConfig);
        PrometheusMeterRegistryProvider provider = new PrometheusMeterRegistryProvider(module);
        PrometheusMeterRegistry registry = provider.get();
        Timer timer = registry.timer("http_server_requests", Tags.of(Tag.of("uri", "/any")));
        timer.record(Duration.ofMillis(5));
        timer.record(Duration.ofMillis(15));
        timer.record(Duration.ofMillis(25));
        timer.record(Duration.ofMillis(35));
        assertNotNull(timer);
        assertEquals(3, timer.takeSnapshot().histogramCounts().length);
    }

    /**
     * Verifies bindMetrics binds supported binder for list entries matching Metric ids.
     */
    @Test
    public void testBindMetricsRegistersBinders() {
        PrometheusConfig prometheusConfig = new PrometheusConfig();
        prometheusConfig.setMetrics(List.of("JvmMemory", "JvmGc"));
        HttpRequestMetricsConfig httpCfg = new HttpRequestMetricsConfig();
        prometheusConfig.setHttpRequestMetricsConfig(httpCfg);
        MonitoringModule module = mock(MonitoringModule.class);
        when(module.getPrometheusConfig()).thenReturn(prometheusConfig);
        PrometheusMeterRegistryProvider provider = new PrometheusMeterRegistryProvider(module);
        PrometheusMeterRegistry registry = provider.get();
        assertNotNull(registry.find("jvm.memory.used").meter());
    }
}
