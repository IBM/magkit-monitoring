package com.aperto.magkit.monitoring.endpoint.metrics;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * The pojo is used to provide Garbage Collector information for the JVM
 * runtime.
 * 
 * @author MIHAELA PAPARETE (IBM)
 * @since 2020-04-08
 *
 */
public class GarbageCollectorInfo {
	private String _name;
	private String[] _memoryPools;
	private long _collectionCount;
	private long _collectionTime;

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public String[] getMemoryPools() {
		return _memoryPools;
	}

	public void setMemoryPools(String[] memoryPools) {
		_memoryPools = memoryPools;
	}

	public long getCollectionCount() {
		return _collectionCount;
	}

	public void setCollectionCount(long collectionCount) {
		_collectionCount = collectionCount;
	}

	@JsonProperty(value = "collectionTime (ms)")
	public long getCollectionTime() {
		return _collectionTime;
	}

	@JsonProperty(value = "collectionTime (ms)")
	public void setCollectionTime(long collectionTime) {
		_collectionTime = collectionTime;
	}
}
