package com.aperto.magkit.monitoring;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aperto.magkit.monitoring.config.prometheus.PrometheusConfig;

import info.magnolia.module.ModuleLifecycle;
import info.magnolia.module.ModuleLifecycleContext;

/**
 * 
 * Module Class.
 * 
 * @author Soenke Schmidt (Aperto - An IBM Company)
 * @since 2020-03-29
 *
 */
public class MonitoringModule implements ModuleLifecycle {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonitoringModule.class);

    @Inject
    private PrometheusConfig _prometheusConfig;

    public void start(ModuleLifecycleContext moduleLifecycleContext) {
        LOGGER.info("Starting Monitoring Module");
    }

    public void stop(ModuleLifecycleContext moduleLifecycleContext) {
        LOGGER.info("Stopping Monitoring Module");
    }

    public PrometheusConfig getPrometheusConfig() {
        return _prometheusConfig;
    }

    public void setPrometheusConfig(PrometheusConfig prometheusConfig) {
        _prometheusConfig = prometheusConfig;
    }

}
