/*-
 * #%L
 * IBM iX Magnolia Monitoring
 * %%
 * Copyright (C) 2023 - 2025 IBM iX
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
package de.ibmix.magkit.monitoring.endpoint.env;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link EnvironmentPropertiesUtil} covering JVM args retrieval, system properties mapping and parsing logic including empty value handling.
 * Ensures tokens without value map to empty string, '=' delimiter splitting logs empty value when more than one '=' exists and system properties contain at least one expected key.
 * @author GitHub Copilot
 * @since 2025-11-12
 */
public class EnvironmentPropertiesUtilTest {

    /**
     * Verifies JVM argument list is non-null (content depends on runtime, may be empty in restrictive environments).
     */
    @Test
    public void testGetJvmArgumentsNonNull() {
        List<String> args = EnvironmentPropertiesUtil.getJvmArguments();
        assertTrue(args != null);
    }

    /**
     * Verifies system properties map contains java.version and non-null value.
     */
    @Test
    public void testGetSystemPropertiesContainsJavaVersion() {
        Map<String, String> sys = EnvironmentPropertiesUtil.getSystemProperties();
        assertTrue(sys.containsKey("java.version"));
        assertFalse(sys.get("java.version").isEmpty());
    }

    /**
     * Verifies parsing splits key=value and assigns empty string when no value part present or more than one '=' exists.
     */
    @Test
    public void testParsingJvmArgs() {
        List<String> raw = List.of("-Dkey=value", "-Donlykey", "-Dcomplex=a=b=c");
        Map<String, String> parsed = EnvironmentPropertiesUtil.parsingJvmArgs(raw);
        assertEquals("value", parsed.get("-Dkey"));
        assertEquals("", parsed.get("-Donlykey"));
        assertEquals("", parsed.get("-Dcomplex"));
    }
}
