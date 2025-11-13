package de.ibmix.magkit.monitoring.endpoint.env;

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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.ibmix.magkit.monitoring.endpoint.AbstractMonitoringEndpoint;
import de.ibmix.magkit.monitoring.endpoint.MonitoringEndpointDefinition;

import info.magnolia.init.MagnoliaConfigurationProperties;
import info.magnolia.rest.DynamicPath;

/**
 * REST endpoint exposing runtime environment details of the Magnolia application and underlying JVM.
 * Delivers JVM arguments, system properties and Magnolia-specific configuration properties merged into a DTO.
 * <p><strong>Purpose</strong></p>
 * Provides operational insight into the current runtime configuration for diagnostics and monitoring dashboards.
 * <p><strong>Main Functionality</strong></p>
 * Collects JVM and system properties via utility methods, filters Magnolia configuration keys not already present and assembles them into an {@link Environment} DTO for JSON serialization.
 * <p><strong>Key Features</strong></p>
 * <ul>
 * <li>Collects JVM input arguments.</li>
 * <li>Lists system properties excluding duplicates with Magnolia properties.</li>
 * <li>Exposes Magnolia configuration properties not already present as system properties.</li>
 * </ul>
 * <p><strong>Usage Preconditions</strong></p>
 * Requires dependency injection of {@link info.magnolia.init.MagnoliaConfigurationProperties}. Security constraints may limit returned values.
 * <p><strong>Null and Error Handling</strong></p>
 * Returns an {@link Environment} instance with empty collections if underlying sources are empty. No exceptions thrown for missing properties.
 * <p><strong>Thread-Safety</strong></p>
 * Stateless aside from injected configuration reference; safe for concurrent GET requests.
 * <p><strong>Usage Example</strong></p>
 * <pre>{@code
 * Environment env = environmentEndpoint.env();
 * }</pre>
 * <p><strong>Important Details</strong></p>
 * Sensitive Magnolia properties are not filtered; consumers should sanitize before external exposure.
 * @author VladNacu
 * @since 2020-04-13
 */
@Path("")
@DynamicPath
public class EnvironmentEndpoint extends AbstractMonitoringEndpoint<MonitoringEndpointDefinition> {

    private final MagnoliaConfigurationProperties _mgnlConfProp;

    /**
     * Constructs the environment endpoint with its monitoring definition and Magnolia configuration properties.
     * @param endpointDefinition monitoring endpoint definition metadata
     * @param mgnlConfProp Magnolia configuration properties bean providing keys and values
     */
    @Inject
    protected EnvironmentEndpoint(MonitoringEndpointDefinition endpointDefinition,
            MagnoliaConfigurationProperties mgnlConfProp) {
        super(endpointDefinition);
        _mgnlConfProp = mgnlConfProp;
    }

    /**
     * Returns an {@link Environment} DTO populated with JVM arguments, system properties and Magnolia properties not present in system properties.
     * @return populated environment descriptor; never null
     */
    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Environment env() {
        final Environment env = new Environment();
        env.setJvmArgs(EnvironmentPropertiesUtil.getJvmArguments());
        env.setSysProp(EnvironmentPropertiesUtil.getSystemProperties());
        env.setMagnoliaProperties(getMagnoliaProperties(env.getSysProp()));
        return env;
    }

    /**
     * Filters Magnolia configuration properties excluding those already defined as system properties.
     * @param systemProperties map of current system properties
     * @return map of Magnolia-only properties
     */
    private Map<String, String> getMagnoliaProperties(final Map<String, String> systemProperties) {
        final Set<String> magnoliaKeys = _mgnlConfProp.getKeys();
        final Map<String, String> map = new HashMap<>();
        for (final String key : magnoliaKeys) {
            if (!systemProperties.keySet().contains(key)) {
                map.put(key, _mgnlConfProp.getProperty(key));
            }
        }
        return map;
    }

}
