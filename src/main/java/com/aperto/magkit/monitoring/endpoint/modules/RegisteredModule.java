package com.aperto.magkit.monitoring.endpoint.modules;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * RegisteredModule Pojo.
 * 
 * @author Dan Olaru (IBM)
 * @since 2020-04-09
 *
 */

public class RegisteredModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisteredModule.class);

    private String _name;
    private String _version;

    public RegisteredModule(Node module) {
        try {
            _name = module.getName();
            _version = module.getProperty("version").getValue().getString();

        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getVersion() {
        return _version;
    }

    public void setVersion(String version) {
        _version = version;
    }

}
