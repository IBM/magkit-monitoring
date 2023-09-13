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
 *
 * Magnolia POJO.
 *
 * @author CLAUDIU GONCIULEA (Aperto - An IBM Company)
 * @since 2020-04-09
 *
 */
public class Magnolia {

    private String _edition;
    private String _version;
    private String _instance;

    private License _license;

    public String getEdition() {
        return _edition;
    }

    public void setEdition(String edition) {
        _edition = edition;
    }

    public String getVersion() {
        return _version;
    }

    public void setVersion(String version) {
        _version = version;
    }

    public String getInstance() {
        return _instance;
    }

    public void setInstance(String instance) {
        _instance = instance;
    }

    public License getLicense() {
        return _license;
    }

    public void setLicense(License license) {
        _license = license;
    }
}
