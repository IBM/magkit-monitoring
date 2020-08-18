package com.aperto.magkit.monitoring;

import java.util.ArrayList;
import java.util.List;

import info.magnolia.module.DefaultModuleVersionHandler;
import info.magnolia.module.InstallContext;
import info.magnolia.module.delta.FilterOrderingTask;
import info.magnolia.module.delta.Task;

/**
 * 
 * Module Version Handler.
 * 
 * @author Soenke Schmidt (Aperto - An IBM Company)
 * @since 2020-03-29
 *
 */
public class MonitoringModuleVersionHandler extends DefaultModuleVersionHandler {

    @Override
    protected List<Task> getStartupTasks(InstallContext installContext) {
        List<Task> startupTasks = new ArrayList<>(super.getStartupTasks(installContext));
        startupTasks.add(new FilterOrderingTask("prometheus", new String[]{"contentType"}));
        return startupTasks;
    }
}