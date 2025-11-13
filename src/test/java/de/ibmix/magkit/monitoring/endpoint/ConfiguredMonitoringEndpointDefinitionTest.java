/*-
 * #%L
 * IBM iX Magnolia Monitoring
 * %%
 * Copyright (C) 2025 IBM iX
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
package de.ibmix.magkit.monitoring.endpoint;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link ConfiguredMonitoringEndpointDefinition} covering default values, name fallback behavior,
 * explicit name override, reset to fallback and error condition when implementation class is absent.
 * Ensures enabled and sensitive flags default to true and endpoint path handling works as expected.
 * @author GitHub Copilot
 * @since 2025-11-13
 */
public class ConfiguredMonitoringEndpointDefinitionTest {

    /**
     * Verifies default state: enabled and sensitive flags true, endpoint path null and name access throws without implementation class.
     */
    @Test
    public void testDefaultStateAndMissingImplementationClassNameAccess() {
        ConfiguredMonitoringEndpointDefinition definition = new ConfiguredMonitoringEndpointDefinition();
        assertTrue(definition.isEnabled());
        assertTrue(definition.isSensitive());
        assertNull(definition.getEndpointPath());
        assertThrows(NullPointerException.class, definition::getName);
    }

    /**
     * Verifies name fallback returns implementation class FQN when explicit name not set.
     */
    @Test
    public void testNameFallbackToImplementationClass() {
        ConfiguredMonitoringEndpointDefinition definition = new ConfiguredMonitoringEndpointDefinition();
        definition.setImplementationClass(SampleEndpoint.class);
        assertEquals(SampleEndpoint.class.getName(), definition.getName());
    }

    /**
     * Verifies explicit name overrides fallback.
     */
    @Test
    public void testExplicitNameOverride() {
        ConfiguredMonitoringEndpointDefinition definition = new ConfiguredMonitoringEndpointDefinition();
        definition.setImplementationClass(SampleEndpoint.class);
        definition.setName("customName");
        assertEquals("customName", definition.getName());
    }

    /**
     * Verifies setting name back to null restores fallback behavior.
     */
    @Test
    public void testResetNameToNullRestoresFallback() {
        ConfiguredMonitoringEndpointDefinition definition = new ConfiguredMonitoringEndpointDefinition();
        definition.setImplementationClass(SampleEndpoint.class);
        definition.setName("temporaryName");
        assertEquals("temporaryName", definition.getName());
        definition.setName(null);
        assertEquals(SampleEndpoint.class.getName(), definition.getName());
    }

    /**
     * Verifies endpoint path can be set and retrieved and flags can be toggled off.
     */
    @Test
    public void testEndpointPathAndFlagMutation() {
        ConfiguredMonitoringEndpointDefinition definition = new ConfiguredMonitoringEndpointDefinition();
        definition.setImplementationClass(SampleEndpoint.class);
        definition.setEndpointPath("health");
        assertEquals("health", definition.getEndpointPath());
        definition.setEnabled(false);
        definition.setSensitive(false);
        assertFalse(definition.isEnabled());
        assertFalse(definition.isSensitive());
    }

    /**
     * Verifies explicit name remains unchanged when implementation class is changed afterwards.
     */
    @Test
    public void testExplicitNamePersistsAfterImplementationClassChange() {
        ConfiguredMonitoringEndpointDefinition definition = new ConfiguredMonitoringEndpointDefinition();
        definition.setImplementationClass(SampleEndpoint.class);
        definition.setName("stableName");
        definition.setImplementationClass(AlternateEndpoint.class);
        assertEquals("stableName", definition.getName());
    }

    /**
     * Verifies resetting endpoint path to null after being set returns null again.
     */
    @Test
    public void testResetEndpointPathToNull() {
        ConfiguredMonitoringEndpointDefinition definition = new ConfiguredMonitoringEndpointDefinition();
        definition.setImplementationClass(SampleEndpoint.class);
        definition.setEndpointPath("metrics");
        assertEquals("metrics", definition.getEndpointPath());
        definition.setEndpointPath(null);
        assertNull(definition.getEndpointPath());
    }

    /**
     * Verifies flags can be toggled off and on again restoring true state values.
     */
    @Test
    public void testReEnableAndResensitizeFlags() {
        ConfiguredMonitoringEndpointDefinition definition = new ConfiguredMonitoringEndpointDefinition();
        definition.setImplementationClass(SampleEndpoint.class);
        assertTrue(definition.isEnabled());
        assertTrue(definition.isSensitive());
        definition.setEnabled(false);
        definition.setSensitive(false);
        assertFalse(definition.isEnabled());
        assertFalse(definition.isSensitive());
        definition.setEnabled(true);
        definition.setSensitive(true);
        assertTrue(definition.isEnabled());
        assertTrue(definition.isSensitive());
    }

    /**
     * Verifies getName throws NullPointerException after clearing implementation class and explicit name.
     */
    @Test
    public void testNameThrowsAfterClearingImplementationClassAndName() {
        ConfiguredMonitoringEndpointDefinition definition = new ConfiguredMonitoringEndpointDefinition();
        definition.setImplementationClass(SampleEndpoint.class);
        definition.setName("temp");
        assertEquals("temp", definition.getName());
        definition.setName(null);
        definition.setImplementationClass(null);
        assertThrows(NullPointerException.class, definition::getName);
    }

    /**
     * Alternate endpoint implementation class for testing name persistence logic.
     */
    private static class AlternateEndpoint {
    }

    /**
     * Dummy endpoint implementation class for testing name fallback logic.
     */
    private static class SampleEndpoint {
    }
}
