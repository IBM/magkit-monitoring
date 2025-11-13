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
package de.ibmix.magkit.monitoring.endpoint.overview;

import static de.ibmix.magkit.test.cms.context.ContextMockUtils.mockWebContext;
import static de.ibmix.magkit.test.servlet.HttpServletRequestStubbingOperation.stubRequestUri;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.ibmix.magkit.monitoring.endpoint.ConfiguredMonitoringEndpointDefinition;
import de.ibmix.magkit.monitoring.endpoint.MonitoringEndpointDefinition;
import de.ibmix.magkit.test.cms.context.ContextMockUtils;
import info.magnolia.context.WebContext;
import info.magnolia.rest.EndpointDefinition;
import info.magnolia.rest.registry.EndpointDefinitionRegistry;
import info.magnolia.config.registry.DefinitionProvider;
import info.magnolia.config.registry.DefinitionMetadata;

/**
 * Unit tests for {@link OverviewEndpoint} covering aggregation logic including version parsing, exclusion of self, path construction and bucket accumulation.
 * Ensures correct handling of versioned and unversioned endpoints, multiple endpoints per version and omission of the overview endpoint itself.
 * Also provides simple coverage for {@link EndpointInfo} and {@link Overview} data containers.
 * @author GitHub Copilot
 * @since 2025-11-13
 */
public class OverviewEndpointTest {

    @BeforeEach
    public void setUp() throws RepositoryException {
        WebContext webContext = mockWebContext();
        stubRequestUri("/monitoring").of(webContext.getRequest());
    }

    @AfterEach
    public void tearDown() {
        ContextMockUtils.cleanContext();
    }

    /**
     * Verifies overview() returns empty map when registry has no providers.
     */
    @Test
    public void testOverviewEmptyProviders() {
        MonitoringEndpointDefinition endpointDef = Mockito.mock(MonitoringEndpointDefinition.class);
        EndpointDefinitionRegistry registry = Mockito.mock(EndpointDefinitionRegistry.class);
        Collection<DefinitionProvider<EndpointDefinition>> providers = new ArrayList<>();
        Mockito.when(registry.getAllProviders()).thenReturn(providers);

        OverviewEndpoint endpoint = new OverviewEndpoint(endpointDef, registry);
        Response response = endpoint.overview();
        assertEquals(200, response.getStatus());
        Object entity = response.getEntity();
        assertTrue(entity instanceof Map);
        @SuppressWarnings("unchecked")
        Map<String, List<EndpointInfo>> categorized = (Map<String, List<EndpointInfo>>) entity;
        assertTrue(categorized.isEmpty());
    }

    /**
     * Verifies unversioned monitoring endpoint is categorized under "custom" with correct name and path.
     */
    @Test
    public void testOverviewUnversionedEndpointIncluded() {
        MonitoringEndpointDefinition endpointDef = Mockito.mock(MonitoringEndpointDefinition.class);
        EndpointDefinitionRegistry registry = Mockito.mock(EndpointDefinitionRegistry.class);
        DefinitionProvider<EndpointDefinition> provider = mockProvider("monitoring/health", "other-module");
        Collection<DefinitionProvider<EndpointDefinition>> providers = new ArrayList<>();
        providers.add(provider);
        Mockito.when(registry.getAllProviders()).thenReturn(providers);

        OverviewEndpoint endpoint = new OverviewEndpoint(endpointDef, registry);
        Response response = endpoint.overview();
        assertEquals(200, response.getStatus());
        @SuppressWarnings("unchecked")
        Map<String, List<EndpointInfo>> categorized = (Map<String, List<EndpointInfo>>) response.getEntity();
        assertTrue(categorized.containsKey("custom"));
        List<EndpointInfo> infos = categorized.get("custom");
        assertEquals(1, infos.size());
        EndpointInfo info = infos.get(0);
        assertEquals("health", info.getName());
        assertEquals("/monitoring/health", info.getPath());

    }

    /**
     * Verifies versioned endpoint reference id with three segments is categorized by its version segment.
     */
    @Test
    public void testOverviewVersionedEndpointIncluded() {
        MonitoringEndpointDefinition endpointDef = Mockito.mock(MonitoringEndpointDefinition.class);
        EndpointDefinitionRegistry registry = Mockito.mock(EndpointDefinitionRegistry.class);
        DefinitionProvider<EndpointDefinition> provider = mockProvider("monitoring/v1/metrics", "other-module");
        Collection<DefinitionProvider<EndpointDefinition>> providers = new ArrayList<>();
        providers.add(provider);
        Mockito.when(registry.getAllProviders()).thenReturn(providers);

        OverviewEndpoint endpoint = new OverviewEndpoint(endpointDef, registry);
        Response response = endpoint.overview();
        assertEquals(200, response.getStatus());
        @SuppressWarnings("unchecked")
        Map<String, List<EndpointInfo>> categorized = (Map<String, List<EndpointInfo>>) response.getEntity();
        assertTrue(categorized.containsKey("v1"));
        EndpointInfo info = categorized.get("v1").get(0);
        assertEquals("metrics", info.getName());
        assertEquals("/monitoring/v1/metrics", info.getPath());
    }

    /**
     * Verifies overview endpoint itself is excluded based on module and name criteria.
     */
    @Test
    public void testOverviewExcludesSelf() {
        MonitoringEndpointDefinition endpointDef = Mockito.mock(MonitoringEndpointDefinition.class);
        EndpointDefinitionRegistry registry = Mockito.mock(EndpointDefinitionRegistry.class);
        DefinitionProvider<EndpointDefinition> selfProvider = mockProvider("monitoring/overview", "magkit-monitoring");
        Collection<DefinitionProvider<EndpointDefinition>> providers = new ArrayList<>();
        providers.add(selfProvider);
        Mockito.when(registry.getAllProviders()).thenReturn(providers);

        OverviewEndpoint endpoint = new OverviewEndpoint(endpointDef, registry);
        Response response = endpoint.overview();
        assertEquals(200, response.getStatus());
        @SuppressWarnings("unchecked")
        Map<String, List<EndpointInfo>> categorized = (Map<String, List<EndpointInfo>>) response.getEntity();
        assertTrue(categorized.isEmpty());
    }

    /**
     * Verifies multiple endpoints under same version accumulate in the list without overwriting.
     */
    @Test
    public void testOverviewMultipleEndpointsSameVersion() {
        MonitoringEndpointDefinition endpointDef = Mockito.mock(MonitoringEndpointDefinition.class);
        EndpointDefinitionRegistry registry = Mockito.mock(EndpointDefinitionRegistry.class);
        DefinitionProvider<EndpointDefinition> providerAlpha = mockProvider("monitoring/v2/alpha", "moduleA");
        DefinitionProvider<EndpointDefinition> providerBeta = mockProvider("monitoring/v2/beta", "moduleB");
        Collection<DefinitionProvider<EndpointDefinition>> providers = new ArrayList<>();
        providers.add(providerAlpha);
        providers.add(providerBeta);
        Mockito.when(registry.getAllProviders()).thenReturn(providers);

        OverviewEndpoint endpoint = new OverviewEndpoint(endpointDef, registry);
        Response response = endpoint.overview();
        assertEquals(200, response.getStatus());
        @SuppressWarnings("unchecked")
        Map<String, List<EndpointInfo>> categorized = (Map<String, List<EndpointInfo>>) response.getEntity();
        List<EndpointInfo> v2Infos = categorized.get("v2");
        assertEquals(2, v2Infos.size());
        List<String> names = v2Infos.stream().map(EndpointInfo::getName).collect(java.util.stream.Collectors.toList());
        assertTrue(names.contains("alpha"));
        assertTrue(names.contains("beta"));
    }

    /**
     * Simple coverage for {@link EndpointInfo} getters and setters and {@link Overview} map setter.
     */
    @Test
    public void testEndpointInfoAndOverviewDataClasses() {
        EndpointInfo info = new EndpointInfo("name", "/monitoring/name");
        assertEquals("name", info.getName());
        assertEquals("/monitoring/name", info.getPath());
        info.setName("other");
        info.setPath("/monitoring/other");
        assertEquals("other", info.getName());
        assertEquals("/monitoring/other", info.getPath());
        Overview overview = new Overview();
        assertTrue(overview.getCategorizedEndpoints().isEmpty());
        Map<String, List<EndpointInfo>> customMap = new HashMap<>();
        customMap.put("vX", new ArrayList<>(List.of(info)));
        overview.setCategorizedEndpoints(customMap);
        assertEquals(1, overview.getCategorizedEndpoints().get("vX").size());
    }

    private DefinitionProvider<EndpointDefinition> mockProvider(String referenceId, String module) {
        @SuppressWarnings("unchecked")
        DefinitionProvider<EndpointDefinition> provider = (DefinitionProvider<EndpointDefinition>) Mockito.mock(DefinitionProvider.class);
        DefinitionMetadata metadata = Mockito.mock(DefinitionMetadata.class);
        ConfiguredMonitoringEndpointDefinition definition = new ConfiguredMonitoringEndpointDefinition();
        definition.setImplementationClass(OverviewEndpoint.class);
        Mockito.when(provider.get()).thenReturn(definition);
        Mockito.when(provider.getMetadata()).thenReturn(metadata);
        Mockito.when(metadata.getReferenceId()).thenReturn(referenceId);
        Mockito.when(metadata.getModule()).thenReturn(module);
        return provider;
    }
}
