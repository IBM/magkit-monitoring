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

/**
 *
 * This is the service class for Metrics endpoint.
 *
 * @author MIHAELA PAPARETE (IBM)
 * @since 2020-04-08
 *
 */
public class MetricsService {

    private static final int MB_SIZE = 1024 * 1024;

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

        for (GarbageCollectorMXBean gc : ManagementFactory.getGarbageCollectorMXBeans()) {
            GarbageCollectorInfo gcInfo = getGarbageCollectorInfo(gc);

            metricsInfo.getGcInfo().add(gcInfo);

            totalGarbageCollections += gcInfo.getCollectionCount() >= 0 ? gcInfo.getCollectionCount() : 0;
            garbageCollectionTime += gcInfo.getCollectionTime() >= 0 ? gcInfo.getCollectionTime() : 0;
        }

        metricsInfo.setTotalCollectionCount(totalGarbageCollections);
        metricsInfo.setTotalCollectionTime(garbageCollectionTime);

        return metricsInfo;
    }

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
