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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import info.magnolia.module.InstallContext;
import info.magnolia.module.delta.Task;
import info.magnolia.module.delta.BootstrapConditionally;
import info.magnolia.module.delta.FilterOrderingTask;
import info.magnolia.module.model.Version;

/**
 * Unit tests for {@link MonitoringModuleVersionHandler} covering addition of install, update and startup tasks.
 * Ensures that role and user bootstrap tasks and Prometheus filter ordering task are appended to base handler task lists.
 * @author GitHub Copilot
 * @since 2025-11-13
 */
public class MonitoringModuleVersionHandlerTest {

    /**
     * Verifies getExtraInstallTasks appends two BootstrapConditionally tasks (role and user) to base list.
     */
    @Test
    public void testExtraInstallTasksContainRoleAndUserBootstrap() {
        MonitoringModuleVersionHandler handler = new MonitoringModuleVersionHandler();
        InstallContext installContext = Mockito.mock(InstallContext.class);
        List<Task> tasks = handler.getExtraInstallTasks(installContext);
        long bootstrapCount = tasks.stream().filter(t -> t instanceof BootstrapConditionally).count();
        assertEquals(2, bootstrapCount);
        assertTrue(tasks.stream().filter(t -> t instanceof BootstrapConditionally).anyMatch(t -> t.getName().contains("role")));
        assertTrue(tasks.stream().filter(t -> t instanceof BootstrapConditionally).anyMatch(t -> t.getName().contains("user")));
    }

    /**
     * Verifies getDefaultUpdateTasks appends two BootstrapConditionally tasks (role and user) to base list.
     */
    @Test
    public void testDefaultUpdateTasksContainRoleAndUserBootstrap() {
        MonitoringModuleVersionHandler handler = new MonitoringModuleVersionHandler();
        Version version = Mockito.mock(Version.class);
        List<Task> tasks = handler.getDefaultUpdateTasks(version);
        long bootstrapCount = tasks.stream().filter(t -> t instanceof BootstrapConditionally).count();
        assertEquals(2, bootstrapCount);
    }

    /**
     * Verifies getStartupTasks appends a single FilterOrderingTask for prometheus filter ordering.
     */
    @Test
    public void testStartupTasksContainPrometheusFilterOrdering() {
        MonitoringModuleVersionHandler handler = new MonitoringModuleVersionHandler();
        InstallContext installContext = Mockito.mock(InstallContext.class);
        List<Task> tasks = handler.getStartupTasks(installContext);
        long orderingCount = tasks.stream().filter(t -> t instanceof FilterOrderingTask).count();
        assertEquals(1, orderingCount);
        assertTrue(tasks.stream().anyMatch(t -> t instanceof FilterOrderingTask && t.getName().contains("prometheus")));
    }
}
