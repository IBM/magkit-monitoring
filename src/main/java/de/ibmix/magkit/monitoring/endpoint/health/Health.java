package de.ibmix.magkit.monitoring.endpoint.health;

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

/**
 * Simple data transfer object conveying the current application health status for monitoring purposes.
 * The status defaults to "UP" and may be changed by future health evaluation logic before serialization.
 * <p><strong>Purpose</strong></p>
 * Encapsulates a single health indicator string that can be extended or replaced by richer health models.
 * <p><strong>Main Functionality</strong></p>
 * Provides a mutable status field with getter/setter enabling straightforward JSON serialization.
 * <p><strong>Key Features</strong></p>
 * <ul>
 * <li>Provides default status value "UP".</li>
 * <li>Serializable as JSON by JAX-RS.</li>
 * </ul>
 * <p><strong>Null and Error Handling</strong></p>
 * Status is never null by default; setters allow null which may lead to clients receiving a null status. Avoid setting null.
 * <p><strong>Thread-Safety</strong></p>
 * Not thread-safe; mutable field without synchronization. Use separate instances per request.
 * <p><strong>Usage Example</strong></p>
 * <pre>{@code
 * Health h = new Health();
 * h.setStatus("DOWN");
 * }</pre>
 * <p><strong>Important Details</strong></p>
 * Additional health dimensions should be added in new fields rather than overloading the status string to maintain clarity.
 * @author Soenke Schmidt (IBM iX)
 * @since 2020-03-29
 */
public class Health {

    private String _status = "UP";

    /**
     * Returns current health status value.
     * @return health status string; may be "UP" or other values
     */
    public String getStatus() {
        return _status;
    }

    /**
     * Updates the health status value.
     * @param status new health status (avoid null)
     */
    public void setStatus(String status) {
        _status = status;
    }

}
