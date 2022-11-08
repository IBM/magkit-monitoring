package com.aperto.magkit.monitoring;

import java.util.ArrayList;
import java.util.List;

import info.magnolia.module.DefaultModuleVersionHandler;
import info.magnolia.module.InstallContext;
import info.magnolia.module.delta.BootstrapConditionally;
import info.magnolia.module.delta.FilterOrderingTask;
import info.magnolia.module.delta.Task;
import info.magnolia.module.model.Version;

/**
 * 
 * Module Version Handler.
 * 
 * @author Soenke Schmidt (Aperto - An IBM Company)
 * @since 2020-03-29
 *
 */
public class MonitoringModuleVersionHandler extends DefaultModuleVersionHandler {
    
    private Task installMonitoringRole() {
        return new BootstrapConditionally("Install monitoring-base-role if not present", "/mgnl-bootstrap/install/userroles.monitoring-base.xml");
    }
    
    private Task installMonitoringUser() {
        return new BootstrapConditionally("Install monitoring user if not present", "/mgnl-bootstrap/install/users.system.monitoring.xml");
    }
    
    @Override
    protected List<Task> getExtraInstallTasks(InstallContext installContext) {
        List<Task> installTasks = new ArrayList<>(super.getExtraInstallTasks(installContext));
        installTasks.add(installMonitoringRole());
        installTasks.add(installMonitoringUser());
        return installTasks;
    }
    
    @Override
    protected List<Task> getDefaultUpdateTasks(Version forVersion) {
        List<Task> updateTasks = new ArrayList<>(super.getDefaultUpdateTasks(forVersion));
        updateTasks.add(installMonitoringRole());
        updateTasks.add(installMonitoringUser());
        return updateTasks;
    }

    @Override
    protected List<Task> getStartupTasks(InstallContext installContext) {
        List<Task> startupTasks = new ArrayList<>(super.getStartupTasks(installContext));
        startupTasks.add(new FilterOrderingTask("prometheus", new String[] { "contentType" }));
        return startupTasks;
    }
}