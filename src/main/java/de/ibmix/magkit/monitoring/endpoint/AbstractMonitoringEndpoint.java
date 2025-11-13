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
 * Base abstraction for all monitoring REST endpoints within the module.
 * Provides a typed link to the {@link MonitoringEndpointDefinition} describing an endpoint's metadata.
 * <p><strong>Purpose</strong></p>
 * Centralizes common endpoint wiring by delegating to Magnolia's {@link info.magnolia.rest.AbstractEndpoint} while
 * adding the monitoring specific generic type bound.
 * <p><strong>Main Functionality</strong></p>
 * Offers a single protected constructor delegating the endpoint definition to the Magnolia REST base class so that
 * subclasses only need to provide their specific definition and resource methods.
 * <p><strong>Key Features</strong></p>
 * <ul>
 * <li>Generic type safety for monitoring endpoint definitions.</li>
 * <li>Reduces duplication across concrete endpoint implementations.</li>
 * <li>Seamless integration with Magnolia REST infrastructure.</li>
 * </ul>
 * <p><strong>Usage Preconditions</strong></p>
 * Subclasses must supply a non-null endpoint definition instance. Magnolia DI is expected to construct concrete endpoints.
 * <p><strong>Null and Error Handling</strong></p>
 * The constructor will forward the definition to the superclass; passing null will result in downstream failures. No further validation is performed here.
 * <p><strong>Thread-Safety</strong></p>
 * Stateless aside from immutable reference managed by superclass; safe for concurrent request handling assuming subclass logic is thread-safe.
 * <p><strong>Usage Example</strong></p>
 * <pre>{@code
 * public class MyEndpoint extends AbstractMonitoringEndpoint<MonitoringEndpointDefinition> {
 *
 *     public MyEndpoint(MonitoringEndpointDefinition def) {
 *         super(def);
 *     }
 * }
 * }</pre>
 * <p><strong>Important Details</strong></p>
 * This base class intentionally avoids adding behavior to keep concrete endpoints flexible; shared logic should be
 * introduced via separate utilities or composition to preserve single responsibility.
 * @author Soenke Schmidt (IBM iX)
 * @since 2020-03-29
 * @param <D> concrete monitoring endpoint definition type
 */
public abstract class AbstractMonitoringEndpoint<D extends MonitoringEndpointDefinition> extends AbstractEndpoint<D> {

    /**
     * Creates a new monitoring endpoint base instance.
     * @param endpointDefinition monitoring endpoint definition metadata; must not be null
     */
    protected AbstractMonitoringEndpoint(D endpointDefinition) {
        super(endpointDefinition);
    }
}
