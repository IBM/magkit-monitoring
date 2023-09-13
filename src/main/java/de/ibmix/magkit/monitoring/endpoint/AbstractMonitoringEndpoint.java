package de.ibmix.magkit.monitoring.endpoint;

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

import info.magnolia.rest.AbstractEndpoint;

/**
 * Abstract Endpoint. Should be implemented by all other Monitoring Endpoints.
 *
 * @author Soenke Schmidt (Aperto - An IBM Company)
 * @since 2020-03-29
 * @param <D> endpoint definition.
 *
 */
public abstract class AbstractMonitoringEndpoint<D extends MonitoringEndpointDefinition> extends AbstractEndpoint<D> {

    protected AbstractMonitoringEndpoint(D endpointDefinition) {
        super(endpointDefinition);
    }
}
