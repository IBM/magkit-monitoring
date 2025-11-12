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

import java.util.ArrayList;
import java.util.List;

import info.magnolia.module.DefaultModuleVersionHandler;
import info.magnolia.module.InstallContext;
import info.magnolia.module.delta.BootstrapConditionally;
import info.magnolia.module.delta.FilterOrderingTask;
import info.magnolia.module.delta.Task;
import info.magnolia.module.model.Version;

/**
 * Module version handler orchestrating install, update and startup tasks for the monitoring module.
 * <p><strong>Purpose</strong></p>
 * Ensures required security principals (monitoring role &amp; user) are bootstrapped and orders the Prometheus filter
 * appropriately in Magnolia's filter chain during startup.
 * <p><strong>Main Functionality</strong></p>
 * Extends {@link DefaultModuleVersionHandler} to append extra tasks in installation/update phases and apply
 * filter ordering at runtime startup.
 * <p><strong>Key Features</strong></p>
 * <ul>
 * <li>Conditional bootstrap of monitoring role and user to avoid duplicates.</li>
 * <li>Defines filter ordering ensuring Prometheus HTTP metrics capture occurs after content type resolution.</li>
 * <li>Leverages Magnolia's task framework for idempotent configuration.</li>
 * </ul>
 * <p><strong>Usage Preconditions</strong></p>
 * Magnolia installation/update lifecycle must invoke handler; task resource paths must be correct within module JAR.
 * <p><strong>Null and Error Handling</strong></p>
 * Relies on Magnolia task framework for error management; this class itself does not throw checked exceptions.
 * <p><strong>Thread-Safety</strong></p>
 * Stateless aside from task creation; methods create new task lists per invocation and are safe for concurrent lifecycle processing (normally single-threaded by Magnolia).
 * <p><strong>Usage Example</strong></p>
 * <pre>{@code
 * // Automatic during module install/update; not invoked directly by user code.
 * }</pre>
 * <p><strong>Important Details</strong></p>
 * Filter ordering names must match configured filter identifiers; changes in Magnolia core filter naming may require adjustments.
 *
 * @author Soenke Schmidt (IBM iX)
 * @since 2020-03-29
 */
public class MonitoringModuleVersionHandler extends DefaultModuleVersionHandler {

    /**
     * Creates a task bootstrapping the monitoring role if absent.
     * @return bootstrap task instance
     */
    private Task installMonitoringRole() {
        return new BootstrapConditionally("Install monitoring-base-role if not present", "/mgnl-bootstrap/install/userroles.monitoring-base.xml");
    }

    /**
     * Creates a task bootstrapping the monitoring user if absent.
     * @return bootstrap task instance
     */
    private Task installMonitoringUser() {
        return new BootstrapConditionally("Install monitoring user if not present", "/mgnl-bootstrap/install/users.system.monitoring.xml");
    }

    /**
     * Adds extra install tasks (role amp; user bootstrap) to default tasks.
     * @param installContext Magnolia install context
     * @return list of install tasks including monitoring additions
     */
    @Override
    protected List<Task> getExtraInstallTasks(InstallContext installContext) {
        List<Task> installTasks = new ArrayList<>(super.getExtraInstallTasks(installContext));
        installTasks.add(installMonitoringRole());
        installTasks.add(installMonitoringUser());
        return installTasks;
    }

    /**
     * Adds extra update tasks (role &amp; user bootstrap) to default tasks.
     * @param forVersion target version for update
     * @return list of update tasks including monitoring additions
     */
    @Override
    protected List<Task> getDefaultUpdateTasks(Version forVersion) {
        List<Task> updateTasks = new ArrayList<>(super.getDefaultUpdateTasks(forVersion));
        updateTasks.add(installMonitoringRole());
        updateTasks.add(installMonitoringUser());
        return updateTasks;
    }

    /**
     * Adds startup task ordering Prometheus filter relative to contentType filter.
     * @param installContext Magnolia install context
     * @return list of startup tasks including filter ordering
     */
    @Override
    protected List<Task> getStartupTasks(InstallContext installContext) {
        List<Task> startupTasks = new ArrayList<>(super.getStartupTasks(installContext));
        startupTasks.add(new FilterOrderingTask("prometheus", new String[] { "contentType" }));
        return startupTasks;
    }
}
