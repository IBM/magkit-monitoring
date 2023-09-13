package de.ibmix.magkit.monitoring.endpoint.modules;

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

import de.ibmix.magkit.monitoring.endpoint.AbstractMonitoringEndpoint;
import de.ibmix.magkit.monitoring.endpoint.MonitoringEndpointDefinition;

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
