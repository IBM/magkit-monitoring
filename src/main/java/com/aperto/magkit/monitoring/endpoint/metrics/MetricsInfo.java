package com.aperto.magkit.monitoring.endpoint.metrics;

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
