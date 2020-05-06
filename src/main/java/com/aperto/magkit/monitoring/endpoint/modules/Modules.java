package com.aperto.magkit.monitoring.endpoint.modules;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;

/**
 * 
 * Modules Pojo.
 * 
 * @author Dan Olaru (IBM)
 * @since 2020-04-09
 *
 */

public class Modules {

    private List<RegisteredModule> _modules;

    public Modules() {
        _modules = new ArrayList<RegisteredModule>();
    }

    public List<RegisteredModule> getModules() {
        return _modules;
    }

    public void setModules(List<RegisteredModule> modules) {
        _modules = modules;
    }

    public void addRegisteredModule(Node module) {
        _modules.add(new RegisteredModule(module));
    }
}
