package com.aperto.magkit.monitoring.endpoint.metrics;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;

/**
 * 
 * This is the service class for Metrics endpoint
 * 
 * @author MIHAELA PAPARETE (IBM)
 * @since 2020-04-08
 *
 */
public class MetricsService {
	private static final DecimalFormat DECIMAL_FORMATTER = new DecimalFormat("#.00");

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

		metricsInfo.setUsedMemory(Double.parseDouble(DECIMAL_FORMATTER.format(usedMemory)));
		metricsInfo.setAvailableMemory(Double.parseDouble(DECIMAL_FORMATTER.format(availableMemory)));

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
