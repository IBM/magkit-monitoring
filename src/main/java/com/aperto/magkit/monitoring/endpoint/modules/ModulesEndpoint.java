package com.aperto.magkit.monitoring.endpoint.modules;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.aperto.magkit.monitoring.endpoint.AbstractMonitoringEndpoint;
import com.aperto.magkit.monitoring.endpoint.MonitoringEndpointDefinition;

import info.magnolia.module.ModuleManagementException;
import info.magnolia.module.ModuleRegistry;
import info.magnolia.module.model.ModuleDefinition;
import info.magnolia.module.model.reader.LightModuleDefinitionReader;
import info.magnolia.rest.DynamicPath;

/**
 * 
 * Modules endpoint.
 * 
 * @author Dan Olaru (IBM)
 * @since 2020-04-09
 *
 */

@Path("")
@DynamicPath
public class ModulesEndpoint extends AbstractMonitoringEndpoint<MonitoringEndpointDefinition> {

    private ModuleRegistry _moduleRegistry;
    private LightModuleDefinitionReader _lightModuleDefinitionReader;

    @Inject
    protected ModulesEndpoint(MonitoringEndpointDefinition endpointDefinition, ModuleRegistry moduleRegistry, LightModuleDefinitionReader lightModuleDefinitionReader) {
        super(endpointDefinition);
        _moduleRegistry = moduleRegistry;
        _lightModuleDefinitionReader = lightModuleDefinitionReader;
    }

    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ModuleResponsePojo> modules() throws ModuleManagementException {

        Map<String, ModuleDefinition> lightModuleDefinitonMap = _lightModuleDefinitionReader.readAll();
        Map<String, ModuleDefinition> registeredModuleDefinitionMap = _moduleRegistry.getModuleDefinitions().stream()
                .collect(Collectors.toMap(ModuleDefinition::getName, Function.identity()));

        registeredModuleDefinitionMap.putAll(lightModuleDefinitonMap);

        return registeredModuleDefinitionMap.values().stream()
            .map((md) -> new ModuleResponsePojo(md.getName(), md.getVersion().toString()))
            .sorted(Comparator.comparing(ModuleResponsePojo::getName))
            .collect(Collectors.toList());
    }

    private static class ModuleResponsePojo {

        private final String _name;
        private final String _version;

        ModuleResponsePojo(String name, String version) {
            _name = name;
            _version = version;
        }

        public String getName() {
            return _name;
        }

        public String getVersion() {
            return _version;
        }

    }

}
