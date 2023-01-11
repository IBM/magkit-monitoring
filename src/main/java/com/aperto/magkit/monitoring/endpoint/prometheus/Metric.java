package com.aperto.magkit.monitoring.endpoint.prometheus;

import java.util.Optional;

import io.micrometer.core.instrument.binder.MeterBinder;
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmHeapPressureMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmInfoMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.logging.Log4j2Metrics;
import io.micrometer.core.instrument.binder.system.FileDescriptorMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.core.instrument.binder.system.UptimeMetrics;

/**
 * 
 * Enum of available Metric Binders.
 * 
 * @author Soenke Schmidt - IBM iX
 * @since 04.01.2023
 *
 */
public enum Metric {
    
    UPTIME("Uptime", UptimeMetrics.class),
    PROCESSOR("Processor", ProcessorMetrics.class),
    FILE_DESCRIPTOR("FileDescriptor", FileDescriptorMetrics.class),
    JVM_INFO("JvmInfo", JvmInfoMetrics.class),
    JVM_MEMORY("JvmMemory", JvmMemoryMetrics.class),
    JVM_HEAP_PRESSURE("JvmHeapPressure", JvmHeapPressureMetrics.class),
    JVM_THREAD("JvmThread", JvmThreadMetrics.class),
    JVM_GC("JvmGc", JvmGcMetrics.class),
    CLASSLOADER("Classloader", ClassLoaderMetrics.class),
    LOG4J2("Log4J2", Log4j2Metrics.class);

    private String _id;
    private Class<? extends MeterBinder> _meterBinderClass;

    Metric(String id, Class<? extends MeterBinder> meterBinderClass) {
        _id = id;
        _meterBinderClass = meterBinderClass;
    }

    public String getId() {
        return _id;
    }

    public Class<? extends MeterBinder> getMeterBinderClass() {
        return _meterBinderClass;
    }

    public MeterBinder getBinderInstance() throws ReflectiveOperationException {
        return _meterBinderClass.getConstructor().newInstance();
    }

    public static Optional<Metric> getById(String id) {
        for (var smb : Metric.values()) {
            if (smb.getId().equalsIgnoreCase(id)) {
                return Optional.of(smb);
            }
        }
        return Optional.empty();
    }

}
