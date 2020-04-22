package com.aperto.magkit.monitoring.endpoint.metrics;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

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
	private BigDecimal _usedMemory;

	// Represented in MB
	private BigDecimal _availableMemory;

	private int _nbActiveThreads;

	private List<GarbageCollectorInfo> _gcInfo = new ArrayList<GarbageCollectorInfo>();

	private long _totalCollectionCount;
	private long _totalCollectionTime;

	@JsonProperty(value = "usedMemory (MB)")
	public BigDecimal getUsedMemory() {
		return _usedMemory;
	}

	@JsonProperty(value = "usedMemory (MB)")
	public void setUsedMemory(BigDecimal usedMemory) {
		_usedMemory = usedMemory;
	}

	@JsonProperty(value = "availableMemory (MB)")
	public BigDecimal getAvailableMemory() {
		return _availableMemory;
	}

	@JsonProperty(value = "availableMemory (MB)")
	public void setAvailableMemory(BigDecimal availableMemory) {
		_availableMemory = availableMemory;
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

	@JsonProperty(value = "totalCollectionTime (ms)")
	public long getTotalCollectionTime() {
		return _totalCollectionTime;
	}

	@JsonProperty(value = "totalCollectionTime (ms)")
	public void setTotalCollectionTime(long totalCollectionTime) {
		_totalCollectionTime = totalCollectionTime;
	}
}
