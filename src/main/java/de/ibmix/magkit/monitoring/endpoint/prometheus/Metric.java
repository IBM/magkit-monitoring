package de.ibmix.magkit.monitoring.endpoint.prometheus;

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
