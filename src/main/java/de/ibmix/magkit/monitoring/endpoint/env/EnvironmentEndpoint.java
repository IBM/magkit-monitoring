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
 * Environment Endpoint class.
 *
 * @author VladNacu
 *
 */
@Path("")
@DynamicPath
public class EnvironmentEndpoint extends AbstractMonitoringEndpoint<MonitoringEndpointDefinition> {

    private final MagnoliaConfigurationProperties _mgnlConfProp;

    @Inject
    protected EnvironmentEndpoint(MonitoringEndpointDefinition endpointDefinition,
            MagnoliaConfigurationProperties mgnlConfProp) {

        super(endpointDefinition);
        _mgnlConfProp = mgnlConfProp;

    }

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
