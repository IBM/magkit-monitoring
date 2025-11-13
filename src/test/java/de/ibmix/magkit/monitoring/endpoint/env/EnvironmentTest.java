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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link Environment} covering default initialization and setter/getter behavior.
 * Verifies lists and maps start empty, setters replace references including null assignment and getters return assigned values.
 * @author GitHub Copilot
 * @since 2025-11-12
 */
public class EnvironmentTest {

    /**
     * Verifies defaults are empty collections.
     */
    @Test
    public void testDefaults() {
        Environment env = new Environment();
        assertTrue(env.getJvmArgs().isEmpty());
        assertTrue(env.getSysProp().isEmpty());
        assertTrue(env.getMagnoliaProperties().isEmpty());
    }

    /**
     * Verifies setters replace references.
     */
    @Test
    public void testSetters() {
        Environment env = new Environment();
        List<String> jvmArgs = List.of("-Xmx512m", "-Xms256m");
        Map<String, String> sys = Map.of("java.version", "17");
        Map<String, String> mgnl = Map.of("magnolia.cache.enabled", "true");
        env.setJvmArgs(jvmArgs);
        env.setSysProp(sys);
        env.setMagnoliaProperties(mgnl);
        assertSame(jvmArgs, env.getJvmArgs());
        assertSame(sys, env.getSysProp());
        assertSame(mgnl, env.getMagnoliaProperties());
    }

    /**
     * Verifies setters accept null and getters return null references.
     */
    @Test
    public void testSetNulls() {
        Environment env = new Environment();
        env.setJvmArgs(null);
        env.setSysProp(null);
        env.setMagnoliaProperties(null);
        assertEquals(null, env.getJvmArgs());
        assertEquals(null, env.getSysProp());
        assertEquals(null, env.getMagnoliaProperties());
    }
}

