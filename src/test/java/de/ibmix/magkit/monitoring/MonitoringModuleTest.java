/*-
 * #%L
 * IBM iX Magnolia Monitoring
 * %%
 * Copyright (C) 2025 IBM iX
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
package de.ibmix.magkit.monitoring;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.ibmix.magkit.monitoring.config.prometheus.PrometheusConfig;
import info.magnolia.module.ModuleLifecycleContext;

/**
 * Unit tests for {@link MonitoringModule} covering lifecycle start/stop invocation, configuration getter/setter behavior
 * and null handling when no configuration is injected. Ensures mutable reference is replaced via setter and accessible via getter.
 * @author GitHub Copilot
 * @since 2025-11-13
 */
public class MonitoringModuleTest {

    /**
     * Verifies getPrometheusConfig returns null before injection and returns same instance after setter injection.
     */
    @Test
    public void testGetterSetterAndInitialNullState() {
        MonitoringModule module = new MonitoringModule();
        assertNull(module.getPrometheusConfig());
        PrometheusConfig cfg = new PrometheusConfig();
        module.setPrometheusConfig(cfg);
        assertSame(cfg, module.getPrometheusConfig());
    }

    /**
     * Verifies setter replaces existing configuration reference with a different instance.
     */
    @Test
    public void testSetterReplacesConfiguration() {
        MonitoringModule module = new MonitoringModule();
        PrometheusConfig cfg1 = new PrometheusConfig();
        PrometheusConfig cfg2 = new PrometheusConfig();
        module.setPrometheusConfig(cfg1);
        assertSame(cfg1, module.getPrometheusConfig());
        module.setPrometheusConfig(cfg2);
        assertNotSame(cfg1, module.getPrometheusConfig());
        assertSame(cfg2, module.getPrometheusConfig());
    }

    /**
     * Verifies start and stop lifecycle methods execute without throwing exceptions when configuration is null.
     */
    @Test
    public void testStartStopWithoutConfiguration() {
        MonitoringModule module = new MonitoringModule();
        ModuleLifecycleContext lifecycleContext = Mockito.mock(ModuleLifecycleContext.class);
        module.start(lifecycleContext);
        module.stop(lifecycleContext);
    }

    /**
     * Verifies lifecycle methods also execute with a configured PrometheusConfig.
     */
    @Test
    public void testStartStopWithConfiguration() {
        MonitoringModule module = new MonitoringModule();
        PrometheusConfig cfg = new PrometheusConfig();
        module.setPrometheusConfig(cfg);
        ModuleLifecycleContext lifecycleContext = Mockito.mock(ModuleLifecycleContext.class);
        module.start(lifecycleContext);
        module.stop(lifecycleContext);
        assertSame(cfg, module.getPrometheusConfig());
    }
}

