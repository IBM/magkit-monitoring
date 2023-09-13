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
 *
 * The pojo is used to provide general information about the JVM runtime.
 *
 * @author MIHAELA PAPARETE (IBM)
 * @since 2020-04-08
 *
 */
public class MetricsInfo {

    // Represented in MB
    private BigDecimal _usedMemoryMb;

    // Represented in MB
    private BigDecimal _availableMemoryMb;

    // Represented in MB
    private BigDecimal _totalMemoryMb;

    private int _nbActiveThreads;

    private List<GarbageCollectorInfo> _gcInfo = new ArrayList<GarbageCollectorInfo>();

    private long _totalCollectionCount;
    private long _totalCollectionTime;

    public BigDecimal getUsedMemoryMb() {
        return _usedMemoryMb;
    }

    public void setUsedMemoryMb(BigDecimal usedMemory) {
        _usedMemoryMb = usedMemory;
    }

    public BigDecimal getAvailableMemoryMb() {
        return _availableMemoryMb;
    }

    public void setAvailableMemoryMb(BigDecimal availableMemory) {
        _availableMemoryMb = availableMemory;
    }

    public BigDecimal getTotalMemoryMb() {
        return _totalMemoryMb;
    }

    public void setTotalMemoryMb(BigDecimal totalMemory) {
        _totalMemoryMb = totalMemory;
    }

    public int getNbActiveThreads() {
        return _nbActiveThreads;
    }

    public void setNbActiveThreads(int nbActiveThreads) {
        _nbActiveThreads = nbActiveThreads;
    }

    public List<GarbageCollectorInfo> getGcInfo() {
        return _gcInfo;
    }

    public long getTotalCollectionCount() {
        return _totalCollectionCount;
    }

    public void setTotalCollectionCount(long totalCollectionCount) {
        _totalCollectionCount = totalCollectionCount;
    }

    public long getTotalCollectionTime() {
        return _totalCollectionTime;
    }

    public void setTotalCollectionTime(long totalCollectionTime) {
        _totalCollectionTime = totalCollectionTime;
    }
}
