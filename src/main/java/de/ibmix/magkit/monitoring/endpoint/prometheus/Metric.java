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
 * Enumeration of supported Prometheus/Micrometer metric binder types exposed by the prometheus endpoint.
 * Each enum constant couples a stable identifier with a Micrometer {@link io.micrometer.core.instrument.binder.MeterBinder} implementation class.
 * <p><strong>Purpose</strong></p>
 * Provides lookup and reflective instantiation of meter binders based on configured metric id strings.
 * <p><strong>Main Functionality</strong></p>
 * Supplies id string via {@link #getId()}, binder class via {@link #getMeterBinderClass()}, reflective instance creation via {@link #getBinderInstance()} and reverse lookup via {@link #getById(String)}.
 * <p><strong>Key Features</strong></p>
 * <ul>
 * <li>Case-insensitive id matching.</li>
 * <li>Reflective creation of binder instance.</li>
 * </ul>
 * <p><strong>Null and Error Handling</strong></p>
 * <code>getById</code> returns empty Optional if id not recognized. <code>getBinderInstance</code> throws ReflectiveOperationException on construction failure.
 * <p><strong>Thread-Safety</strong></p>
 * Enum instances are immutable and thread-safe; reflective instantiation may incur overhead.
 * <p><strong>Usage Example</strong></p>
 * <pre>{@code
 * Metric.getById("JvmMemory").ifPresent(m -> m.getBinderInstance().bindTo(registry));
 * }</pre>
 * <p><strong>Important Details</strong></p>
 * Binder classes must provide a public no-args constructor; otherwise reflective instantiation fails.
 * @author Soenke Schmidt - IBM iX
 * @since 2023-01-04
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

    /**
     * Returns stable identifier for this metric binder.
     * @return id string
     */
    public String getId() {
        return _id;
    }

    /**
     * Returns binder implementation class.
     * @return binder class type
     */
    public Class<? extends MeterBinder> getMeterBinderClass() {
        return _meterBinderClass;
    }

    /**
     * Reflectively creates a new binder instance.
     * @return new meter binder
     * @throws ReflectiveOperationException if construction fails
     */
    public MeterBinder getBinderInstance() throws ReflectiveOperationException {
        return _meterBinderClass.getConstructor().newInstance();
    }

    /**
     * Looks up a Metric enum constant by id (case-insensitive).
     * @param id configured id string
     * @return optional metric constant
     */
    public static Optional<Metric> getById(String id) {
        for (var smb : Metric.values()) {
            if (smb.getId().equalsIgnoreCase(id)) {
                return Optional.of(smb);
            }
        }
        return Optional.empty();
    }

}
