package de.ibmix.magkit.monitoring.endpoint.modules;

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

import static de.ibmix.magkit.test.cms.module.ModuleDefinitionStubbingOperation.stubName;
import static de.ibmix.magkit.test.cms.module.ModuleDefinitionStubbingOperation.stubVersion;
import static de.ibmix.magkit.test.cms.module.ModuleMockUtils.mockModuleDefinition;
import static de.ibmix.magkit.test.cms.module.ModuleMockUtils.mockModuleRegistry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.ibmix.magkit.test.cms.context.ContextMockUtils;
import de.ibmix.magkit.test.cms.module.ModuleMockUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import de.ibmix.magkit.monitoring.endpoint.MonitoringEndpointDefinition;
import info.magnolia.module.ModuleRegistry;
import info.magnolia.module.model.ModuleDefinition;
import info.magnolia.module.model.reader.LightModuleDefinitionReader;

/**
 * Unit tests for {@link ModulesEndpoint} covering empty sources and merge + sort behaviour including light module override.
 * Ensures alphabetic ordering and correct version resolution via {@link ModuleDefinition#getVersion()} toString output.
 * @author GitHub Copilot
 * @since 2025-11-13
 */
public class ModulesEndpointTest {

    @AfterEach
    public void tearDown() {
        ContextMockUtils.cleanContext();
    }

    /**
     * Verifies that an empty light module map and empty registry list produce an empty result list.
     * @throws Exception if invocation fails unexpectedly
     */
    @Test
    public void testModulesEmptyReturnsEmptyList() throws Exception {
        MonitoringEndpointDefinition endpointDefinition = mock(MonitoringEndpointDefinition.class);
        ModuleRegistry moduleRegistry = ModuleMockUtils.mockModuleRegistry();
        LightModuleDefinitionReader lightReader = mock(LightModuleDefinitionReader.class);
        when(lightReader.readAll()).thenReturn(Collections.emptyMap());
        ModulesEndpoint endpoint = new ModulesEndpoint(endpointDefinition, moduleRegistry, lightReader);
        List<?> result = endpoint.modules();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Verifies merge logic: registered and light modules are combined, light definitions override duplicates, output sorted alphabetically and versions resolved.
     * @throws Exception if invocation fails unexpectedly
     */
    @Test
    public void testModulesMergeSortAndOverride() throws Exception {
        // Mock magnolia modules:
        ModuleDefinition module1 = mockModuleDefinition("moduleA", stubVersion("1.5"));
        ModuleDefinition module2 = mockModuleDefinition("moduleB", stubVersion("3.1"));
        ModuleRegistry moduleRegistry = mockModuleRegistry();
        // Stubbing of ModuleRegistry is incomplete. Mock result of getModuleDefinitions():
        when(moduleRegistry.getModuleDefinitions()).thenReturn(List.of(module1, module2));

        // Mock light modules
        LightModuleDefinitionReader lightReader = mock(LightModuleDefinitionReader.class);
        Map<String, ModuleDefinition> lightMap = new HashMap<>();
        lightMap.put("moduleA", mockPlainModuleDefinition("moduleA", "2.0"));
        lightMap.put("moduleC", mockPlainModuleDefinition("moduleC", "1.0"));
        when(lightReader.readAll()).thenReturn(lightMap);

        MonitoringEndpointDefinition endpointDefinition = mock(MonitoringEndpointDefinition.class);
        ModulesEndpoint endpoint = new ModulesEndpoint(endpointDefinition, moduleRegistry, lightReader);
        List<ModulesEndpoint.ModuleResponsePojo> result = endpoint.modules();
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("moduleA", result.get(0).getName());
        assertEquals("2.0.0", result.get(0).getVersion());
        assertEquals("moduleB", result.get(1).getName());
        assertEquals("3.1.0", result.get(1).getVersion());
        assertEquals("moduleC", result.get(2).getName());
        assertEquals("1.0.0", result.get(2).getVersion());
    }

    private ModuleDefinition mockPlainModuleDefinition(String name, String version) {
        ModuleDefinition moduleDefinition = mock(ModuleDefinition.class);
        stubVersion(version).of(moduleDefinition);
        stubName(name).of(moduleDefinition);
        return moduleDefinition;
    }
}
