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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Environment POJO.
 *
 * @author VladNacu
 *
 */
public class Environment {

    private List<String> _jvmArgs = new ArrayList<>();

    private Map<String, String> _sysProp = new HashMap<>();

    private Map<String, String> _magnoliaProperties = new HashMap<>();

    /**
     * default no args constructor.
     */
    public Environment() {

    }

    public List<String> getJvmArgs() {
        return _jvmArgs;
    }

    public void setJvmArgs(List<String> jvmArgs) {
        _jvmArgs = jvmArgs;
    }

    public Map<String, String> getSysProp() {
        return _sysProp;
    }

    public void setSysProp(Map<String, String> sysProp) {
        _sysProp = sysProp;
    }

    public Map<String, String> getMagnoliaProperties() {
        return _magnoliaProperties;
    }

    public void setMagnoliaProperties(Map<String, String> magnoliaProperties) {
        _magnoliaProperties = magnoliaProperties;
    }

}
