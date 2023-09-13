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

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Envrionment Properties Utils class.
 *
 * @author VladNacu
 *
 */
public final class EnvironmentPropertiesUtil {

    private static final String DELIMITER = "=";
    private static final String EMPTY_STRING = "";

    private EnvironmentPropertiesUtil() {

    }

    public static List<String> getJvmArguments() {
        final RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();

        return runtimeMxBean.getInputArguments();
    }

    public static Map<String, String> getSystemProperties() {
        final Map<String, String> map = new HashMap<>();
        final Properties systemProperties = System.getProperties();
        final Enumeration enuProp = systemProperties.propertyNames();

        while (enuProp.hasMoreElements()) {
            String propertyName = (String) enuProp.nextElement();
            String propertyValue = systemProperties.getProperty(propertyName);
            map.put(propertyName, propertyValue);
        }

        return map;
    }

    public static Map<String, String> parsingJvmArgs(final List<String> jvmArgs) {
        final Map<String, String> map = new HashMap<>();
        for (final String arg : jvmArgs) {
            String[] tokens = arg.split(DELIMITER);
            if (tokens.length == 2) {
                map.put(tokens[0], tokens[1]);
            } else {
                map.put(tokens[0], EMPTY_STRING);
            }
        }
        return map;
    }

}
