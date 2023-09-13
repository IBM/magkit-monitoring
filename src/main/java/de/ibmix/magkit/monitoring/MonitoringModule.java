package de.ibmix.magkit.monitoring;

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

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.ibmix.magkit.monitoring.config.prometheus.PrometheusConfig;

import info.magnolia.module.ModuleLifecycle;
import info.magnolia.module.ModuleLifecycleContext;

/**
 *
 * Module Class.
 *
 * @author Soenke Schmidt (IBM iX)
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
