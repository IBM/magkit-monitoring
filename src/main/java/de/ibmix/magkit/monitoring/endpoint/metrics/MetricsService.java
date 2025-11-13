package de.ibmix.magkit.monitoring.endpoint.metrics;

/*-
 * #%L
 * IBM iX Magnolia Monitoring
 * %%
 * Copyright (C) 2023 IBM iX
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

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Service component assembling JVM runtime performance metrics for monitoring exposure.
 * <p><strong>Purpose</strong></p>
 * Collects and normalizes core JVM indicators (memory usage, active thread count, garbage collection stats) into a {@link MetricsInfo} data transfer object.
 * <p><strong>Main Functionality</strong></p>
 * Reads {@link Runtime} memory figures, enumerates {@link java.lang.management.GarbageCollectorMXBean} instances, calculates derived memory metrics and aggregates GC totals.
 * <p><strong>Key Features</strong></p>
 * <ul>
 * <li>Precision memory values in megabytes with fixed scale (4 decimals).</li>
 * <li>Per-collector and aggregated GC counts/time normalization (negative unsupported values treated as zero in totals).</li>
 * <li>Stateless transformation producing a fresh snapshot per invocation.</li>
 * </ul>
 * <p><strong>Usage Preconditions</strong></p>
 * JVM must expose standard MXBeans; no Magnolia dependencies required for operation.
 * <p><strong>Side Effects</strong></p>
 * None; only reads JVM state and instantiates DTO/value objects.
 * <p><strong>Null and Error Handling</strong></p>
 * No checked exceptions. MXBean counters of -1 (unsupported) are excluded from aggregated totals; memory calculations use current runtime values and may fluctuate between calls.
 * <p><strong>Thread-Safety</strong></p>
 * Stateless and immutable after construction; multiple threads can invoke concurrently without synchronization.
 * <p><strong>Usage Example</strong></p>
 * <pre>{@code
 * MetricsInfo snapshot = metricsService.getInfoMetrics();
 * }</pre>
 * <p><strong>Extensibility</strong></p>
 * Additional metrics can be integrated by enriching {@link MetricsInfo} and updating logic in {@link #getInfoMetrics()} without altering existing fields.
 * <p><strong>Important Details</strong></p>
 * Memory computations derive used = total - free and available = max - used; these are instantaneous and not averaged.
 *
 * @author MIHAELA PAPARETE (IBM)
 * @since 2020-04-08
 */
public class MetricsService {

    private static final int MB_SIZE = 1024 * 1024;

    /**
     * Builds and returns a point-in-time {@link MetricsInfo} snapshot representing current JVM performance indicators.
     * <p>Computes used, available and total memory (in MB), active thread count and both per-garbage-collector plus aggregated collection metrics.</p>
     * @return populated metrics info snapshot (never null)
     */
    public MetricsInfo getInfoMetrics() {
        MetricsInfo metricsInfo = new MetricsInfo();

        // the current allocated space ready for new objects
        long freeMemory = Runtime.getRuntime().freeMemory();

        // the total allocated space reserved for the java process thus far
        long totalMemory = Runtime.getRuntime().totalMemory();

        // the maximum amount of memory that the JVM will attempt to use
        long maxMemory = Runtime.getRuntime().maxMemory();

        double usedMemory = (double) (totalMemory - freeMemory) / MB_SIZE;
        double availableMemory = (double) maxMemory / MB_SIZE - usedMemory;

        metricsInfo.setUsedMemoryMb(BigDecimal.valueOf(usedMemory).setScale(4, RoundingMode.HALF_UP));
        metricsInfo.setAvailableMemoryMb(BigDecimal.valueOf(availableMemory).setScale(4, RoundingMode.HALF_UP));

        BigDecimal total = metricsInfo.getAvailableMemoryMb().add(metricsInfo.getUsedMemoryMb());
        metricsInfo.setTotalMemoryMb(total.setScale(4, RoundingMode.HALF_UP));

        // the number of active threads for the current thread
        int activeThreads = Thread.activeCount();
        metricsInfo.setNbActiveThreads(activeThreads);

        long totalGarbageCollections = 0;
        long garbageCollectionTime = 0;

        for (GarbageCollectorMXBean gc : getGarbageCollectorMXBeans()) {
            GarbageCollectorInfo gcInfo = getGarbageCollectorInfo(gc);
            metricsInfo.getGcInfo().add(gcInfo);
            totalGarbageCollections += gcInfo.getCollectionCount() >= 0 ? gcInfo.getCollectionCount() : 0;
            garbageCollectionTime += gcInfo.getCollectionTime() >= 0 ? gcInfo.getCollectionTime() : 0;
        }

        metricsInfo.setTotalCollectionCount(totalGarbageCollections);
        metricsInfo.setTotalCollectionTime(garbageCollectionTime);
        return metricsInfo;
    }

    /**
     * Template method returning the list of GC MXBeans. Extracted for testability so subclasses can supply custom beans.
     * @return list of GC MXBeans
     */
    protected List<GarbageCollectorMXBean> getGarbageCollectorMXBeans() {
        return ManagementFactory.getGarbageCollectorMXBeans();
    }

    /**
     * Extracts garbage collector statistics into a value object.
     * @param gc garbage collector MXBean
     * @return populated garbage collector info
     */
    public GarbageCollectorInfo getGarbageCollectorInfo(GarbageCollectorMXBean gc) {
        long count = gc.getCollectionCount();
        long time = gc.getCollectionTime();
        String name = gc.getName();
        String[] memoryPools = gc.getMemoryPoolNames();

        GarbageCollectorInfo gcInfo = new GarbageCollectorInfo();
        gcInfo.setName(name);
        gcInfo.setCollectionCount(count);
        gcInfo.setCollectionTime(time);
        gcInfo.setMemoryPools(memoryPools);
        return gcInfo;
    }
}
