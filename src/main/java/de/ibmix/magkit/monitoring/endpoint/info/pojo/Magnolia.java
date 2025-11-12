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
 * Descriptor for Magnolia instance metadata: edition, version, instance type (author/public) and license.
 * <p><strong>Key Features</strong></p>
 * <ul>
 * <li>Captures edition and version strings.</li>
 * <li>Distinguishes author vs public instance.</li>
 * <li>Embeds {@link License} object.</li>
 * </ul>
 * <p><strong>Null and Error Handling</strong></p>
 * Fields may be null until populated; clients should handle missing properties.
 * <p><strong>Thread-Safety</strong></p>
 * Not thread-safe; instantiate per request.
 * <p><strong>Usage Example</strong></p>
 * <pre>{@code
 * Magnolia m = new Magnolia();
 * m.setEdition("Enterprise");
 * }</pre>
 * @author CLAUDIU GONCIULEA (IBM iX)
 * @since 2020-04-09
 */
public class Magnolia {

    private String _edition;
    private String _version;
    private String _instance;
    private License _license;

    /**
     * Returns edition string.
     * @return edition; may be null
     */
    public String getEdition() {
        return _edition;
    }

    /**
     * Sets edition string.
     * @param edition edition value; may be null
     */
    public void setEdition(String edition) {
        _edition = edition;
    }

    /**
     * Returns version string.
     * @return version; may be null
     */
    public String getVersion() {
        return _version;
    }

    /**
     * Sets version string.
     * @param version version value; may be null
     */
    public void setVersion(String version) {
        _version = version;
    }

    /**
     * Returns instance type (author/public).
     * @return instance type; may be null
     */
    public String getInstance() {
        return _instance;
    }

    /**
     * Sets instance type (author/public).
     * @param instance instance type; may be null
     */
    public void setInstance(String instance) {
        _instance = instance;
    }

    /**
     * Returns license metadata.
     * @return license descriptor; may be null
     */
    public License getLicense() {
        return _license;
    }

    /**
     * Sets license metadata.
     * @param license license descriptor; may be null
     */
    public void setLicense(License license) {
        _license = license;
    }
}
