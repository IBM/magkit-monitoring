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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.lang.management.GarbageCollectorMXBean;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.math.RoundingMode;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link MetricsService} validating memory calculations, GC aggregation and exclusion of negative counters.
 * Provides deterministic GC bean list via subclass overriding {@link MetricsService#getGarbageCollectorMXBeans()}.
 * @author GitHub Copilot
 * @since 2025-11-12
 */
public class MetricsServiceTest {

    /**
     * Verifies getInfoMetrics() aggregates GC counts/time ignoring negative unsupported values and includes per-collector details.
     */
    @Test
    public void testGetInfoMetricsAggregatesGcDataAndCalculatesMemory() throws MalformedObjectNameException {
        GarbageCollectorMXBean positiveGc = mockGarbageCollectorMXBean("PositiveGC", new String[]{"Eden"}, 5L, 120L);
        GarbageCollectorMXBean negativeGc = mockGarbageCollectorMXBean("NegativeGC", new String[]{"Old"}, -1L, -1L);
        MetricsService service = new MetricsService() {
            @Override
            protected List<GarbageCollectorMXBean> getGarbageCollectorMXBeans() {
                return Arrays.asList(positiveGc, negativeGc);
            }
        };
        MetricsInfo info = service.getInfoMetrics();
        assertNotNull(info);
        BigDecimal used = info.getUsedMemoryMb();
        BigDecimal available = info.getAvailableMemoryMb();
        BigDecimal total = info.getTotalMemoryMb();
        assertNotNull(used);
        assertNotNull(available);
        assertNotNull(total);
        assertEquals(used.add(available).setScale(4, RoundingMode.HALF_UP), total.setScale(4, RoundingMode.HALF_UP));
        assertEquals(4, used.scale());
        assertEquals(4, available.scale());
        assertEquals(4, total.scale());
        assertTrue(info.getNbActiveThreads() >= 1);
        assertEquals(2, info.getGcInfo().size());
        assertEquals(5L, info.getTotalCollectionCount());
        assertEquals(120L, info.getTotalCollectionTime());
        GarbageCollectorInfo gc0 = info.getGcInfo().get(0);
        assertEquals("PositiveGC", gc0.getName());
        assertEquals(5L, gc0.getCollectionCount());
        assertEquals(120L, gc0.getCollectionTime());
        GarbageCollectorInfo gc1 = info.getGcInfo().get(1);
        assertEquals("NegativeGC", gc1.getName());
        assertEquals(-1L, gc1.getCollectionCount());
        assertEquals(-1L, gc1.getCollectionTime());
    }

    /**
     * Verifies getGarbageCollectorInfo() extracts raw bean values without transformation.
     */
    @Test
    public void testGetGarbageCollectorInfoReturnsBeanValues() throws MalformedObjectNameException {
        GarbageCollectorMXBean bean = mockGarbageCollectorMXBean("TestGC", new String[]{"PoolA", "PoolB"}, 10L, 250L);
        MetricsService service = new MetricsService();
        GarbageCollectorInfo info = service.getGarbageCollectorInfo(bean);
        assertEquals("TestGC", info.getName());
        assertEquals(10L, info.getCollectionCount());
        assertEquals(250L, info.getCollectionTime());
        assertEquals(2, info.getMemoryPools().length);
    }

    private GarbageCollectorMXBean mockGarbageCollectorMXBean(String name, String[] pools, long count, long time) throws MalformedObjectNameException {
        GarbageCollectorMXBean bean = mock(GarbageCollectorMXBean.class);
        doReturn(name).when(bean).getName();
        doReturn(pools).when(bean).getMemoryPoolNames();
        doReturn(count).when(bean).getCollectionCount();
        doReturn(time).when(bean).getCollectionTime();
        doReturn(true).when(bean).isValid();
        doReturn(new ObjectName("test:type=GarbageCollector,name=" + name)).when(bean).getObjectName();
        return bean;
    }
}
