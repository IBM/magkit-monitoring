package com.aperto.magkit.monitoring.endpoint.prometheus;

import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;

/**
 * PrometheusMeterRegistryImpl class.
 * 
 * @author VladNacu
 *
 */
public class PrometheusMeterRegistryImpl extends PrometheusMeterRegistry{

    public PrometheusMeterRegistryImpl() {
        super(PrometheusConfig.DEFAULT);
    }

}
