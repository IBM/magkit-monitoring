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

import info.magnolia.rest.EndpointDefinition;

/**
 * Contract describing metadata of a monitoring REST endpoint used by the module for registration and exposure control.
 * Extends Magnolia's {@link info.magnolia.rest.EndpointDefinition} adding sensitivity semantics.
 * <p><strong>Purpose</strong></p>
 * Provides a uniform way to query whether an endpoint is enabled, its path, name and whether it should be treated as sensitive.
 * <p><strong>Main Functionality</strong></p>
 * Declares a single additional method {@link #isSensitive()} complementing the Magnolia endpoint definition; implementations supply all required metadata for REST registration and access constraints.
 * <p><strong>Key Features</strong></p>
 * <ul>
 * <li>Sensitivity flag for differentiating endpoints requiring stricter authorization.</li>
 * <li>Integration with Magnolia REST endpoint definition lifecycle.</li>
 * </ul>
 * <p><strong>Usage Preconditions</strong></p>
 * Implementations must supply consistent responses; returning null for required metadata in parent interface may lead to registration issues.
 * <p><strong>Thread-Safety</strong></p>
 * Implementations should be configured once and then accessed concurrently in a read-only fashion.
 * <p><strong>Important Details</strong></p>
 * The sensitivity attribute allows external security logic to enforce different role requirements; its semantics should remain stable to avoid breaking authorization policies.
 * @author Soenke Schmidt (IBM iX)
 * @since 2020-03-31
 */
public interface MonitoringEndpointDefinition extends EndpointDefinition {

    /**
     * Indicates whether the endpoint is considered sensitive and may require stricter authorization.
     * @return true if sensitive
     */
    boolean isSensitive();

}
