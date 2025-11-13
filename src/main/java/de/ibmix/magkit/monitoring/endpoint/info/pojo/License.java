package de.ibmix.magkit.monitoring.endpoint.info.pojo;

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
 * Value object representing Magnolia license ownership and expiration metadata.
 * Used within the info endpoint aggregation to surface licensing state.
 * <p><strong>Key Features</strong></p>
 * <ul>
 * <li>Owner name for accountability.</li>
 * <li>Expiration date formatted as yyyy-MM-dd.</li>
 * </ul>
 * <p><strong>Null and Error Handling</strong></p>
 * Fields may be null if not set; clients should handle missing values gracefully.
 * <p><strong>Thread-Safety</strong></p>
 * Not thread-safe; mutate per request only.
 * <p><strong>Usage Example</strong></p>
 * <pre>{@code
 * License l = new License();
 * l.setOwner("Example Corp");
 * }</pre>
 * @author CLAUDIU GONCIULEA (IBM iX)
 * @since 2020-04-09
 */
public class License {

    private String _owner;
    private String _expirationDate;

    /**
     * Returns license owner entity name.
     * @return owner string; may be null
     */
    public String getOwner() {
        return _owner;
    }

    /**
     * Sets license owner entity name.
     * @param owner owner name; may be null
     */
    public void setOwner(String owner) {
        _owner = owner;
    }

    /**
     * Returns license expiration date as formatted string.
     * @return expiration date; may be null
     */
    public String getExpirationDate() {
        return _expirationDate;
    }

    /**
     * Sets license expiration date.
     * @param expirationDate formatted date string yyyy-MM-dd; may be null
     */
    public void setExpirationDate(String expirationDate) {
        _expirationDate = expirationDate;
    }
}
