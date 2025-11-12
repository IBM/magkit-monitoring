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

/**
 * Value object encapsulating statistics for a single JVM garbage collector.
 * <p><strong>Purpose</strong></p>
 * Holds immutable-like snapshot data (name, associated memory pools, collection invocation count and cumulative collection time) for inclusion in higher-level monitoring responses.
 * <p><strong>Main Functionality</strong></p>
 * Provides a simple data carrier with standard getters/setters used by {@link MetricsService} when creating {@link MetricsInfo} snapshots.
 * <p><strong>Key Features</strong></p>
 * <ul>
 * <li>Supports multiple memory pools per collector.</li>
 * <li>Collection count and time values directly mirror MXBean counters (may be -1 if unsupported).</li>
 * </ul>
 * <p><strong>Side Effects</strong></p>
 * None; no logic beyond property access and mutation.
 * <p><strong>Null and Error Handling</strong></p>
 * Fields may be null until populated. Negative numeric values (e.g. -1) represent unsupported metrics and are preserved for caller interpretation.
 * <p><strong>Thread-Safety</strong></p>
 * Not thread-safe for concurrent mutation; expected to be confined to a single thread during population then used read-only.
 * <p><strong>Usage Example</strong></p>
 * <pre>{@code
 * GarbageCollectorInfo gci = new GarbageCollectorInfo();
 * gci.setName("G1 Young Generation");
 * gci.setMemoryPools(new String[]{"G1 Eden Space"});
 * }</pre>
 * <p><strong>Extensibility</strong></p>
 * Additional GC metrics can be added by introducing new fields and corresponding accessors without affecting existing serialization.
 * <p><strong>Important Details</strong></p>
 * Unsupported (-1) MXBean counters are preserved to allow clients to distinguish unsupported vs zero values.
 *
 * @author MIHAELA PAPARETE (IBM)
 * @since 2020-04-08
 */
public class GarbageCollectorInfo {

    private String _name;
    private String[] _memoryPools;
    private long _collectionCount;
    private long _collectionTime;

    /**
     * Returns garbage collector name.
     * @return collector name
     */
    public String getName() {
        return _name;
    }

    /**
     * Sets garbage collector name.
     * @param name name string
     */
    public void setName(String name) {
        _name = name;
    }

    /**
     * Returns associated memory pool names.
     * @return array of pool names
     */
    public String[] getMemoryPools() {
        return _memoryPools;
    }

    /**
     * Sets associated memory pool names.
     * @param memoryPools pool names array
     */
    public void setMemoryPools(String[] memoryPools) {
        _memoryPools = memoryPools;
    }

    /**
     * Returns collection invocation count (may be -1 if unsupported).
     * @return collection count
     */
    public long getCollectionCount() {
        return _collectionCount;
    }

    /**
     * Sets collection invocation count.
     * @param collectionCount count value
     */
    public void setCollectionCount(long collectionCount) {
        _collectionCount = collectionCount;
    }

    /**
     * Returns cumulative collection time in milliseconds (may be -1 if unsupported).
     * @return collection time ms
     */
    public long getCollectionTime() {
        return _collectionTime;
    }

    /**
     * Sets cumulative collection time in milliseconds.
     * @param collectionTime time value ms
     */
    public void setCollectionTime(long collectionTime) {
        _collectionTime = collectionTime;
    }
}
