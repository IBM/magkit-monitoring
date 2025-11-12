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
 * REST endpoint exposing a list of all Magnolia modules (light and registered) with their resolved versions.
 * Merges definitions from {@link info.magnolia.module.model.reader.LightModuleDefinitionReader} and {@link info.magnolia.module.ModuleRegistry}.
 * <p><strong>Purpose</strong></p>
 * Provides inventory visibility for operational diagnostics and compatibility checks.
 * <p><strong>Key Features</strong></p>
 * <ul>
 * <li>Merges light and traditional module definitions.</li>
 * <li>Sorts output alphabetically.</li>
 * <li>Returns compact POJOs with name and version only.</li>
 * </ul>
 * <p><strong>Null and Error Handling</strong></p>
 * Throws {@link info.magnolia.module.ModuleManagementException} if reading fails. Returns empty list if no modules resolved.
 * <p><strong>Thread-Safety</strong></p>
 * Stateless aside from injected readers; safe for concurrent invocation.
 * <p><strong>Usage Example</strong></p>
 * <pre>{@code
 * List<?> modules = modulesEndpoint.modules();
 * }</pre>
 * @author Dan Olaru (IBM)
 * @since 2020-04-09
 */

@Path("")
@DynamicPath
public class ModulesEndpoint extends AbstractMonitoringEndpoint<MonitoringEndpointDefinition> {

    private final ModuleRegistry _moduleRegistry;
    private final LightModuleDefinitionReader _lightModuleDefinitionReader;

    @Inject
    protected ModulesEndpoint(MonitoringEndpointDefinition endpointDefinition, ModuleRegistry moduleRegistry, LightModuleDefinitionReader lightModuleDefinitionReader) {
        super(endpointDefinition);
        _moduleRegistry = moduleRegistry;
        _lightModuleDefinitionReader = lightModuleDefinitionReader;
    }

    /**
     * Returns merged list of module name/version pairs from light and registered modules.
     * @return sorted list of module response POJOs
     * @throws ModuleManagementException if reading module definitions fails
     */
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

    /**
     * Simple immutable value object representing a module with name and version.
     * <p><strong>Purpose</strong></p>
     * Provides a compact representation of a Magnolia module suitable for JSON serialization in the modules endpoint response.
     * <p><strong>Key Features</strong></p>
     * <ul>
     * <li>Immutable fields set via constructor.</li>
     * <li>Exposes only essential metadata (name, version).</li>
     * </ul>
     * <p><strong>Null and Error Handling</strong></p>
     * Constructor parameters should be non-null; no validation performed internally. Getters may return null if constructed with null values.
     * <p><strong>Thread-Safety</strong></p>
     * Immutable after construction; safe for concurrent read access.
     * <p><strong>Usage Example</strong></p>
     * <pre>{@code
     * ModuleResponsePojo pojo = new ModuleResponsePojo("magkit-monitoring", "1.1.2-SNAPSHOT");
     * }</pre>
     * @author Dan Olaru (IBM)
     * @since 2020-04-09
     */
    private static class ModuleResponsePojo {

        private final String _name;
        private final String _version;

        /**
         * Creates module response.
         * @param name module name
         * @param version module version string
         */
        ModuleResponsePojo(String name, String version) {
            _name = name;
            _version = version;
        }

        /**
         * Returns module name.
         * @return module name
         */
        public String getName() {
            return _name;
        }

        /**
         * Returns module version.
         * @return version string
         */
        public String getVersion() {
            return _version;
        }

    }

}
