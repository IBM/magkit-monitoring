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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Data transfer object aggregating JVM runtime metrics for monitoring responses.
 * <p><strong>Purpose</strong></p>
 * Encapsulates memory usage figures (used, available, total), active thread count, per-garbage-collector statistics and aggregated GC totals into a serializable structure.
 * <p><strong>Main Functionality</strong></p>
 * Serves as a container populated by {@link MetricsService}, exposing getters for JSON serialization without business logic.
 * <p><strong>Key Features</strong></p>
 * <ul>
 * <li>Memory metrics represented in megabytes with decimal precision.</li>
 * <li>List of {@link GarbageCollectorInfo} for detailed per-collector insight.</li>
 * <li>Aggregated garbage collection counts and times across all collectors.</li>
 * </ul>
 * <p><strong>Side Effects</strong></p>
 * None; purely holds values assigned by the service layer.
 * <p><strong>Null and Error Handling</strong></p>
 * BigDecimal fields may be null until set; callers should guard against null when performing arithmetic. Negative GC counters/time values mirror underlying MXBean semantics (e.g. -1 unsupported) and are not corrected here.
 * <p><strong>Thread-Safety</strong></p>
 * Not thread-safe for concurrent mutation; intended for single-thread population and read-only consumption thereafter.
 * <p><strong>Usage Example</strong></p>
 * <pre>{@code
 * MetricsInfo info = metricsService.getInfoMetrics();
 * }</pre>
 * <p><strong>Extensibility</strong></p>
 * Additional metrics can be added via new fields and corresponding getters/setters without breaking existing consumers.
 * <p><strong>Important Details</strong></p>
 * Memory values reflect a single collection cycle; for trend analysis external sampling/storage is required.
 *
 * @author MIHAELA PAPARETE (IBM)
 * @since 2020-04-08
 */
public class MetricsInfo {

    // Represented in MB
    private BigDecimal _usedMemoryMb;

    // Represented in MB
    private BigDecimal _availableMemoryMb;

    // Represented in MB
    private BigDecimal _totalMemoryMb;

    private int _nbActiveThreads;

    private final List<GarbageCollectorInfo> _gcInfo = new ArrayList<>();

    private long _totalCollectionCount;
    private long _totalCollectionTime;

    /**
     * Returns used heap memory in megabytes.
     * @return used memory MB
     */
    public BigDecimal getUsedMemoryMb() {
        return _usedMemoryMb;
    }

    /**
     * Sets used heap memory in megabytes.
     * @param usedMemory value MB
     */
    public void setUsedMemoryMb(BigDecimal usedMemory) {
        _usedMemoryMb = usedMemory;
    }

    /**
     * Returns available memory estimate in megabytes.
     * @return available memory MB
     */
    public BigDecimal getAvailableMemoryMb() {
        return _availableMemoryMb;
    }

    /**
     * Sets available memory estimate in megabytes.
     * @param availableMemory value MB
     */
    public void setAvailableMemoryMb(BigDecimal availableMemory) {
        _availableMemoryMb = availableMemory;
    }

    /**
     * Returns total memory (used + available) in megabytes.
     * @return total memory MB
     */
    public BigDecimal getTotalMemoryMb() {
        return _totalMemoryMb;
    }

    /**
     * Sets total memory in megabytes.
     * @param totalMemory value MB
     */
    public void setTotalMemoryMb(BigDecimal totalMemory) {
        _totalMemoryMb = totalMemory;
    }

    /**
     * Returns number of active threads.
     * @return active thread count
     */
    public int getNbActiveThreads() {
        return _nbActiveThreads;
    }

    /**
     * Sets number of active threads.
     * @param nbActiveThreads count value
     */
    public void setNbActiveThreads(int nbActiveThreads) {
        _nbActiveThreads = nbActiveThreads;
    }

    /**
     * Returns list of garbage collector infos.
     * @return list of GC info objects
     */
    public List<GarbageCollectorInfo> getGcInfo() {
        return _gcInfo;
    }

    /**
     * Returns total garbage collection count aggregated across collectors.
     * @return collection count
     */
    public long getTotalCollectionCount() {
        return _totalCollectionCount;
    }

    /**
     * Sets total garbage collection count.
     * @param totalCollectionCount aggregated count
     */
    public void setTotalCollectionCount(long totalCollectionCount) {
        _totalCollectionCount = totalCollectionCount;
    }

    /**
     * Returns total garbage collection time in milliseconds aggregated across collectors.
     * @return total GC time ms
     */
    public long getTotalCollectionTime() {
        return _totalCollectionTime;
    }

    /**
     * Sets total garbage collection time in milliseconds.
     * @param totalCollectionTime aggregated time ms
     */
    public void setTotalCollectionTime(long totalCollectionTime) {
        _totalCollectionTime = totalCollectionTime;
    }
}
