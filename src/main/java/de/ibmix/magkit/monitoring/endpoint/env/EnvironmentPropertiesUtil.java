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
 * Utility class exposing helper methods to extract environment details of the running JVM and Magnolia instance.
 * Provides access to JVM input arguments, system properties and parsing support for transforming raw JVM argument
 * strings into key/value pairs.
 * <p><strong>Purpose</strong></p>
 * Centralizes environment inspection logic needed by monitoring endpoints without scattering low level
 * ManagementFactory and System property access throughout the code base.
 * <p><strong>Key Features</strong></p>
 * <ul>
 * <li>Fetch JVM input arguments via {@link RuntimeMXBean}.</li>
 * <li>Enumerate system properties into a map.</li>
 * <li>Parse raw JVM argument list tokens into key/value form using '=' delimiter.</li>
 * </ul>
 * <p><strong>Usage Preconditions</strong></p>
 * JVM must grant permission for accessing runtime MXBean and system properties; a SecurityManager (if present) may restrict access.
 * <p><strong>Null and Error Handling</strong></p>
 * Methods never return null; empty collections are possible. Parsing treats any token without '=' as a key with empty value.
 * <p><strong>Side Effects</strong></p>
 * No side effects; purely reads runtime metadata.
 * <p><strong>Thread-Safety</strong></p>
 * Thread-safe: all methods are stateless and rely on JVM provided thread-safe structures.
 * <p><strong>Usage Example</strong></p>
 * <pre>{@code
 * List<String> args = EnvironmentPropertiesUtil.getJvmArguments();
 * Map<String, String> sysProps = EnvironmentPropertiesUtil.getSystemProperties();
 * Map<String, String> parsedArgs = EnvironmentPropertiesUtil.parsingJvmArgs(args);
 * }</pre>
 * <p><strong>Important Details</strong></p>
 * Sensitive system properties (passwords, secrets) are returned unfiltered; callers should remove sensitive entries before serialization.
 * @author VladNacu
 * @since 2020-04-13
 */
public final class EnvironmentPropertiesUtil {

    private static final String DELIMITER = "=";
    private static final String EMPTY_STRING = "";

    private EnvironmentPropertiesUtil() {

    }

    /**
     * Returns the JVM input arguments as provided by the runtime MXBean (e.g. -Xms, -Xmx, system properties).
     * @return list of raw JVM input argument strings; never null
     */
    public static List<String> getJvmArguments() {
        final RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        return runtimeMxBean.getInputArguments();
    }

    /**
     * Returns all system properties as a map from property name to value.
     * @return map containing system property names and values; never null, possibly empty
     */
    public static Map<String, String> getSystemProperties() {
        final Map<String, String> map = new HashMap<>();
        final Properties systemProperties = System.getProperties();
        final Enumeration<?> enuProp = systemProperties.propertyNames();
        while (enuProp.hasMoreElements()) {
            String propertyName = (String) enuProp.nextElement();
            String propertyValue = systemProperties.getProperty(propertyName);
            map.put(propertyName, propertyValue);
        }
        return map;
    }

    /**
     * Parses a list of raw JVM argument tokens into a map using '=' as delimiter. Tokens without a value produce an empty value.
     * @param jvmArgs list of raw JVM argument strings; may be empty but not null for safe usage
     * @return map of argument key to value (empty string if no value present)
     */
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
