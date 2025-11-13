package de.ibmix.magkit.monitoring.endpoint.overview;

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
import java.util.List;
import java.util.Map;

/**
 * Container aggregating monitoring endpoint information grouped by version identifiers.
 * Used as internal structure for building the overview endpoint response.
 * <p><strong>Purpose</strong></p>
 * Holds a map from version string to list of endpoint descriptors facilitating serialization.
 * <p><strong>Main Functionality</strong></p>
 * Provides a mutable map allowing incremental accumulation of endpoint info objects keyed by version strings before final serialization.
 * <p><strong>Key Features</strong></p>
 * <ul>
 * <li>Mutable map allowing incremental population.</li>
 * <li>Simple getters/setters for JSON binding.</li>
 * </ul>
 * <p><strong>Null and Error Handling</strong></p>
 * Map initialized empty; setters accept null which may cause NPE on subsequent reads. Avoid setting null.
 * <p><strong>Thread-Safety</strong></p>
 * Not thread-safe; external synchronization required if modified concurrently.
 * <p><strong>Usage Example</strong></p>
 * <pre>{@code
 * Overview o = new Overview();
 * o.getCategorizedEndpoints().put("v1", List.of(new EndpointInfo("health","/monitoring/v1/health")));
 * }</pre>
 * <p><strong>Important Details</strong></p>
 * Version key "custom" is used for unversioned endpoints; clients should treat it as a special bucket.
 * @author Dan Olaru (IBM)
 * @since 2020-04-24
 */

public class Overview {

    private Map<String, List<EndpointInfo>> _categorizedEndpoints;

    /**
     * Creates an empty overview container with an initialized map.
     */
    public Overview() {
        super();
        _categorizedEndpoints = new HashMap<>();
    }

    /**
     * Returns map of version to list of endpoint info.
     * @return categorized endpoints map; never null unless set to null explicitly
     */
    public Map<String, List<EndpointInfo>> getCategorizedEndpoints() {
        return _categorizedEndpoints;
    }

    /**
     * Replaces categorized endpoints map.
     * @param categorizedEndpoints new map; may be null (not recommended)
     */
    public void setCategorizedEndpoints(Map<String, List<EndpointInfo>> categorizedEndpoints) {
        _categorizedEndpoints = categorizedEndpoints;
    }

}
