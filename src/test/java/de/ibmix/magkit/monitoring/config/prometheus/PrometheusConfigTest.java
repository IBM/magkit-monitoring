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
package de.ibmix.magkit.monitoring.config.prometheus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link PrometheusConfig} covering default initialization, setters and property injection behavior.
 * Ensures metrics defaults are loaded, property parsing preserves internal empty segments and HTTP request config delegation works.
 * @author GitHub Copilot
 * @since 2025-11-12
 */
public class PrometheusConfigTest {

    /**
     * Verifies default metrics list matches the specified initial configuration.
     */
    @Test
    public void testDefaultMetrics() {
        PrometheusConfig prometheusConfig = new PrometheusConfig();
        List<String> metrics = prometheusConfig.getMetrics();
        assertEquals(List.of("Uptime", "Processor", "JvmThread", "JvmGc", "JvmMemory", "ClassLoader", "Log4J2"), metrics);
    }

    /**
     * Verifies setter replaces metrics list reference.
     */
    @Test
    public void testSetMetrics() {
        PrometheusConfig prometheusConfig = new PrometheusConfig();
        List<String> newMetrics = List.of("A", "B");
        prometheusConfig.setMetrics(newMetrics);
        assertSame(newMetrics, prometheusConfig.getMetrics());
    }

    /**
     * Verifies property injection parsing splits on commas and retains internal empty segments but drops trailing empty segment.
     */
    @Test
    public void testProvideMetricsFromPropsKeepsInternalEmptySegments() {
        PrometheusConfig prometheusConfig = new PrometheusConfig();
        prometheusConfig.provideMetricsFromProps("A,B,,C,");
        List<String> metrics = prometheusConfig.getMetrics();
        assertEquals(4, metrics.size());
        assertEquals("", metrics.get(2));
        assertEquals("C", metrics.get(3));
    }

    /**
     * Verifies property injection with empty string results in single empty element.
     */
    @Test
    public void testProvideMetricsFromPropsEmptyString() {
        PrometheusConfig prometheusConfig = new PrometheusConfig();
        prometheusConfig.provideMetricsFromProps("");
        List<String> metrics = prometheusConfig.getMetrics();
        assertEquals(1, metrics.size());
        assertEquals("", metrics.get(0));
    }

    /**
     * Verifies HTTP request metrics config getter/setter.
     */
    @Test
    public void testHttpRequestMetricsConfigSetterGetter() {
        PrometheusConfig prometheusConfig = new PrometheusConfig();
        HttpRequestMetricsConfig httpCfg = new HttpRequestMetricsConfig();
        prometheusConfig.setHttpRequestMetricsConfig(httpCfg);
        assertSame(httpCfg, prometheusConfig.getHttpRequestMetricsConfig());
    }

    /**
     * Verifies that setting metrics to null stores null reference (framework allows it) and getter returns null.
     */
    @Test
    public void testSetMetricsNull() {
        PrometheusConfig prometheusConfig = new PrometheusConfig();
        prometheusConfig.setMetrics(null);
        assertSame(null, prometheusConfig.getMetrics());
    }
}
