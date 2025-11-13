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
package de.ibmix.magkit.monitoring.endpoint.logs;

import static de.ibmix.magkit.test.cms.context.ContextMockUtils.mockWebContext;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javax.jcr.RepositoryException;

import de.ibmix.magkit.test.cms.context.ContextMockUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.ibmix.magkit.monitoring.endpoint.MonitoringEndpointDefinition;
import info.magnolia.context.WebContext;
import info.magnolia.init.MagnoliaConfigurationProperties;

/**
 * Unit tests for {@link LogsEndpoint} covering directory listing, error handling and log content retrieval.
 * Ensures .log filtering, URI composition, CRLF line endings, extension handling and case-insensitive access.
 * @author GitHub Copilot
 * @since 2025-11-12
 */
public class LogsEndpointTest {

    private Path _tempLogDir;

    @BeforeEach
    public void setUp() throws IOException, RepositoryException {
        _tempLogDir = Files.createTempDirectory("logsEndpointTest");
        Files.writeString(_tempLogDir.resolve("system.log"), "line1\nline2");
        Files.writeString(_tempLogDir.resolve("ignored.txt"), "nope");
        Files.writeString(_tempLogDir.resolve("access.log"), "a1\na2\n");

        WebContext webContext = mockWebContext();
        de.ibmix.magkit.test.servlet.HttpServletRequestStubbingOperation.stubRequestUri("/monitoring/logs").of(webContext.getRequest());
    }

    @AfterEach
    public void tearDown() throws IOException {
        ContextMockUtils.cleanContext();
        try (java.util.stream.Stream<Path> walk = Files.walk(_tempLogDir)) {
            walk.sorted(java.util.Comparator.reverseOrder()).forEach(p -> {
                try {
                    Files.deleteIfExists(p);
                } catch (IOException e) {
                    // ignoring cleanup failure to not mask test result
                }
            });
        }
    }

    /**
     * Verifies availableLogs() lists only .log files and constructs proper URI paths.
     */
    @Test
    public void testAvailableLogsListsOnlyLogFiles() {
        MonitoringEndpointDefinition def = Mockito.mock(MonitoringEndpointDefinition.class);
        MagnoliaConfigurationProperties props = Mockito.mock(MagnoliaConfigurationProperties.class);
        Mockito.when(props.getProperty("magnolia.logs.dir")).thenReturn(_tempLogDir.toString());
        LogsEndpoint endpoint = new LogsEndpoint(def, props);
        try (javax.ws.rs.core.Response response = endpoint.availableLogs()) {
            assertEquals(200, response.getStatus());
            Object entity = response.getEntity();
            assertTrue(entity instanceof java.util.List);
            @SuppressWarnings("unchecked")
            List<LogInfo> infos = (List<LogInfo>) entity;
            assertEquals(2, infos.size());
            assertTrue(infos.stream().allMatch(i -> i.getName().endsWith(".log")));
            assertTrue(infos.stream().allMatch(i -> i.getPath().startsWith("/monitoring/logs/")));
        }
    }

    /**
     * Verifies availableLogs() returns 500 when underlying directory access throws an IOException.
     */
    @Test
    public void testAvailableLogsHandlesIoException() {
        MonitoringEndpointDefinition def = Mockito.mock(MonitoringEndpointDefinition.class);
        MagnoliaConfigurationProperties props = Mockito.mock(MagnoliaConfigurationProperties.class);
        Mockito.when(props.getProperty("magnolia.logs.dir")).thenReturn(_tempLogDir.resolve("missing").toString());
        LogsEndpoint endpoint = new LogsEndpoint(def, props);
        try (javax.ws.rs.core.Response response = endpoint.availableLogs()) {
            assertEquals(500, response.getStatus());
            assertNotNull(response.getEntity());
        }
    }

    /**
     * Verifies logs() returns file content with CRLF endings when name given without extension.
     */
    @Test
    public void testLogsReturnsContentWithoutExtension() {
        MonitoringEndpointDefinition def = Mockito.mock(MonitoringEndpointDefinition.class);
        MagnoliaConfigurationProperties props = Mockito.mock(MagnoliaConfigurationProperties.class);
        Mockito.when(props.getProperty("magnolia.logs.dir")).thenReturn(_tempLogDir.toString());
        LogsEndpoint endpoint = new LogsEndpoint(def, props);
        try (javax.ws.rs.core.Response response = endpoint.logs("system")) {
            assertEquals(200, response.getStatus());
            Object entity = response.getEntity();
            assertTrue(entity instanceof StringBuilder);
            String content = entity.toString();
            assertTrue(content.contains("line1\r\nline2\r\n"));
        }
    }

    /**
     * Verifies logs() returns file content when full filename including extension provided.
     */
    @Test
    public void testLogsReturnsContentWithExtension() {
        MonitoringEndpointDefinition def = Mockito.mock(MonitoringEndpointDefinition.class);
        MagnoliaConfigurationProperties props = Mockito.mock(MagnoliaConfigurationProperties.class);
        Mockito.when(props.getProperty("magnolia.logs.dir")).thenReturn(_tempLogDir.toString());
        LogsEndpoint endpoint = new LogsEndpoint(def, props);
        try (javax.ws.rs.core.Response response = endpoint.logs("access.log")) {
            assertEquals(200, response.getStatus());
            String content = response.getEntity().toString();
            assertTrue(content.contains("a1\r\na2\r\n"));
        }
    }

    /**
     * Verifies case-insensitive handling of log names including extension.
     */
    @Test
    public void testLogsCaseInsensitiveExtension() {
        MonitoringEndpointDefinition def = Mockito.mock(MonitoringEndpointDefinition.class);
        MagnoliaConfigurationProperties props = Mockito.mock(MagnoliaConfigurationProperties.class);
        Mockito.when(props.getProperty("magnolia.logs.dir")).thenReturn(_tempLogDir.toString());
        LogsEndpoint endpoint = new LogsEndpoint(def, props);
        try (javax.ws.rs.core.Response response = endpoint.logs("SyStEm.LoG")) {
            assertEquals(200, response.getStatus());
            String content = response.getEntity().toString();
            assertTrue(content.contains("line1\r\nline2\r\n"));
        }
    }

    /**
     * Verifies logs() returns 400 for missing file.
     */
    @Test
    public void testLogsMissingFile() {
        MonitoringEndpointDefinition def = Mockito.mock(MonitoringEndpointDefinition.class);
        MagnoliaConfigurationProperties props = Mockito.mock(MagnoliaConfigurationProperties.class);
        Mockito.when(props.getProperty("magnolia.logs.dir")).thenReturn(_tempLogDir.toString());
        LogsEndpoint endpoint = new LogsEndpoint(def, props);
        try (javax.ws.rs.core.Response response = endpoint.logs("unknown")) {
            assertEquals(400, response.getStatus());
        }
    }

    /**
     * Simple coverage test for {@link LogInfo} getters and setters.
     */
    @Test
    public void testLogInfoGettersAndSetters() {
        LogInfo info = new LogInfo("file.log", "/monitoring/logs/file.log");
        assertEquals("file.log", info.getName());
        assertEquals("/monitoring/logs/file.log", info.getPath());
        info.setName("other.log");
        info.setPath("/monitoring/logs/other.log");
        assertEquals("other.log", info.getName());
        assertEquals("/monitoring/logs/other.log", info.getPath());
    }

    /**
     * Verifies availableLogs() returns empty list for an empty directory.
     */
    @Test
    public void testAvailableLogsEmptyDirectory() throws IOException {
        Path emptyDir = Files.createTempDirectory("logsEndpointEmpty");
        MonitoringEndpointDefinition def = Mockito.mock(MonitoringEndpointDefinition.class);
        MagnoliaConfigurationProperties props = Mockito.mock(MagnoliaConfigurationProperties.class);
        Mockito.when(props.getProperty("magnolia.logs.dir")).thenReturn(emptyDir.toString());
        LogsEndpoint endpoint = new LogsEndpoint(def, props);
        try (javax.ws.rs.core.Response response = endpoint.availableLogs()) {
            assertEquals(200, response.getStatus());
            Object entity = response.getEntity();
            assertTrue(entity instanceof java.util.List);
            @SuppressWarnings("unchecked")
            List<LogInfo> infos = (List<LogInfo>) entity;
            assertEquals(0, infos.size());
        }
        try (java.util.stream.Stream<Path> walk = Files.walk(emptyDir)) {
            walk.sorted(java.util.Comparator.reverseOrder()).forEach(p -> {
                try {
                    Files.deleteIfExists(p);
                } catch (IOException e) {
                    // ignoring cleanup failure
                }
            });
        }
    }

    /**
     * Verifies logs() missing file returns expected error message body.
     */
    @Test
    public void testLogsMissingFileMessage() {
        MonitoringEndpointDefinition def = Mockito.mock(MonitoringEndpointDefinition.class);
        MagnoliaConfigurationProperties props = Mockito.mock(MagnoliaConfigurationProperties.class);
        Mockito.when(props.getProperty("magnolia.logs.dir")).thenReturn(_tempLogDir.toString());
        LogsEndpoint endpoint = new LogsEndpoint(def, props);
        try (javax.ws.rs.core.Response response = endpoint.logs("doesNotExist")) {
            assertEquals(400, response.getStatus());
            assertEquals("The specified log file couldn't be found!", response.getEntity());
        }
    }

    /**
     * Verifies system.log content ends with CRLF after last line when accessed with explicit extension.
     */
    @Test
    public void testLogsExplicitExtensionEndsWithCrlf() {
        MonitoringEndpointDefinition def = Mockito.mock(MonitoringEndpointDefinition.class);
        MagnoliaConfigurationProperties props = Mockito.mock(MagnoliaConfigurationProperties.class);
        Mockito.when(props.getProperty("magnolia.logs.dir")).thenReturn(_tempLogDir.toString());
        LogsEndpoint endpoint = new LogsEndpoint(def, props);
        try (javax.ws.rs.core.Response response = endpoint.logs("system.log")) {
            assertEquals(200, response.getStatus());
            String content = response.getEntity().toString();
            assertTrue(content.endsWith("\r\n"));
            assertTrue(content.contains("line1\r\nline2\r\n"));
        }
    }
}
