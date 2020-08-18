package com.aperto.magkit.monitoring.endpoint.prometheus;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.aperto.magkit.monitoring.endpoint.AbstractMonitoringEndpoint;
import com.aperto.magkit.monitoring.endpoint.MonitoringEndpointDefinition;

import info.magnolia.rest.DynamicPath;
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.logging.Log4j2Metrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.core.instrument.binder.system.UptimeMetrics;
import io.micrometer.prometheus.PrometheusMeterRegistry;

/**
 * Prometheus endpoint class.
 * 
 * @author VladNacu
 *
 */
@Path("")
@DynamicPath
public class PrometheusEndpoint extends AbstractMonitoringEndpoint<MonitoringEndpointDefinition> {

    private final PrometheusMeterRegistry _registry;

    @Inject
    protected PrometheusEndpoint(MonitoringEndpointDefinition endpointDefinition, PrometheusMeterRegistry registry) {
        super(endpointDefinition);
        _registry = registry;

    }

    private void init() {
        new JvmThreadMetrics().bindTo(_registry);
        new JvmGcMetrics().bindTo(_registry);
        new JvmMemoryMetrics().bindTo(_registry);
        new ProcessorMetrics().bindTo(_registry);
        new UptimeMetrics().bindTo(_registry);
        new ClassLoaderMetrics().bindTo(_registry);
        new Log4j2Metrics().bindTo(_registry);
    }

    @GET
    @Path("")
    @Produces(MediaType.TEXT_PLAIN)
    public String prometheus() {
        init();
        return _registry.scrape();

    }


}
