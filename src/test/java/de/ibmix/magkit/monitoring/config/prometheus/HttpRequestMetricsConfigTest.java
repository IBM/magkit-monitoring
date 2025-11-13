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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link HttpRequestMetricsConfig} covering default empty initialization, setters and property injection parsing.
 * Ensures URI and SLO bucket parsing maintains order, empty segment preservation (internal only) and integer conversion.
 * @author GitHub Copilot
 * @since 2025-11-12
 */
public class HttpRequestMetricsConfigTest {

    /**
     * Verifies defaults are empty lists.
     */
    @Test
    public void testDefaults() {
        HttpRequestMetricsConfig cfg = new HttpRequestMetricsConfig();
        assertTrue(cfg.getUris().isEmpty());
        assertTrue(cfg.getSloBuckets().isEmpty());
    }

    /**
     * Verifies setters replace list references.
     */
    @Test
    public void testSetters() {
        HttpRequestMetricsConfig cfg = new HttpRequestMetricsConfig();
        List<String> uris = List.of("/a", "/b");
        List<Integer> slo = List.of(10, 20);
        cfg.setUris(uris);
        cfg.setSloBuckets(slo);
        assertSame(uris, cfg.getUris());
        assertSame(slo, cfg.getSloBuckets());
    }

    /**
     * Verifies URI property parsing splits on comma and preserves internal empty segments while dropping trailing empty segment.
     */
    @Test
    public void testProvideUrisFromPropsKeepsInternalEmptySegments() {
        HttpRequestMetricsConfig cfg = new HttpRequestMetricsConfig();
        cfg.provideUrisFromProps("/a,,/b,");
        List<String> uris = cfg.getUris();
        assertEquals(3, uris.size());
        assertEquals("", uris.get(1));
        assertEquals("/b", uris.get(2));
    }

    /**
     * Verifies URI property parsing on empty string results in single empty element.
     */
    @Test
    public void testProvideUrisFromPropsEmptyString() {
        HttpRequestMetricsConfig cfg = new HttpRequestMetricsConfig();
        cfg.provideUrisFromProps("");
        List<String> uris = cfg.getUris();
        assertEquals(1, uris.size());
        assertEquals("", uris.get(0));
    }

    /**
     * Verifies SLO bucket property parsing converts to integers and maintains order.
     */
    @Test
    public void testProvideSloBucketsFromPropsParsing() {
        HttpRequestMetricsConfig cfg = new HttpRequestMetricsConfig();
        cfg.provideSloBucketsFromProps("10,20,30");
        List<Integer> buckets = cfg.getSloBuckets();
        assertEquals(List.of(10, 20, 30), buckets);
    }

    /**
     * Verifies that invalid integer in SLO property results in NumberFormatException.
     */
    @Test
    public void testProvideSloBucketsFromPropsInvalidInteger() {
        HttpRequestMetricsConfig cfg = new HttpRequestMetricsConfig();
        NumberFormatException ex = assertThrows(NumberFormatException.class, () -> cfg.provideSloBucketsFromProps("10,xx,30"));
        assertTrue(ex.getMessage().contains("xx"));
    }

    /**
     * Verifies that setters accept null references (no internal guard) and getters return those nulls.
     */
    @Test
    public void testSetNullLists() {
        HttpRequestMetricsConfig cfg = new HttpRequestMetricsConfig();
        cfg.setUris(null);
        cfg.setSloBuckets(null);
        assertSame(null, cfg.getUris());
        assertSame(null, cfg.getSloBuckets());
    }
}
