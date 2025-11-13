package de.ibmix.magkit.monitoring.endpoint.heap;

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

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import javax.management.MBeanServer;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.ibmix.magkit.monitoring.endpoint.MonitoringEndpointDefinition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.sun.management.HotSpotDiagnosticMXBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link HeapDumpEndpoint} covering normal execution, IOException handling and MXBean invocation logic.
 * Focus is on ensuring response construction, header population and proper delegation to the HotSpot diagnostic MXBean.
 * The real heap dump operation is mocked to avoid generating large files during tests.
 * @author GitHub Copilot
 * @since 2025-11-12
 */
public class HeapDumpEndpointTest {

    /**
     * Verifies that getHeapDump returns a 200 OK response with an attached file whose name matches expected pattern
     * and that a file is physically created when the dump operation succeeds.
     * @throws Exception if test setup fails
     */
    @Test
    @DisplayName("Successful heap dump response contains file and header")
    public void testGetHeapDumpSuccess() throws Exception {

        MonitoringEndpointDefinition endpointDefinition = Mockito.mock(MonitoringEndpointDefinition.class);
        HeapDumpEndpoint endpoint = new HeapDumpEndpoint(endpointDefinition);
        try (MockedStatic<HeapDumpEndpoint> heapDumpMock = Mockito.mockStatic(HeapDumpEndpoint.class)) {
            heapDumpMock.when(() -> HeapDumpEndpoint.dumpHeap(Mockito.anyString(), Mockito.anyBoolean())).thenAnswer(invocation -> {
                String filePath = invocation.getArgument(0);
                File createdFile = new File(filePath);
                createdFile.getParentFile().mkdirs();
                createdFile.createNewFile();
                return null;
            });
            Response response = endpoint.getHeapDump();
            assertEquals(200, response.getStatus());
            Object entity = response.getEntity();
            assertNotNull(entity);
            assertEquals(File.class, entity.getClass());
            File dumpFile = (File) entity;
            assertTrue(dumpFile.getName().startsWith(HeapDumpEndpoint.FILE_NAME));
            assertTrue(dumpFile.getName().endsWith(HeapDumpEndpoint.FILE_EXTENSION));
            assertTrue(dumpFile.exists());
            String contentDisposition = response.getHeaderString("Content-Disposition");
            assertNotNull(contentDisposition);
            assertTrue(contentDisposition.contains(dumpFile.getName()));
            assertEquals(MediaType.APPLICATION_OCTET_STREAM_TYPE, response.getMediaType());
            // Cleanup created test file to avoid polluting workspace
            assertTrue(dumpFile.delete());
        }
    }

    /**
     * Verifies that an IOException thrown by dumpHeap is logged (not asserted) and the response is still constructed
     * with a file reference (not existing physically) and correct headers.
     * @throws Exception if test setup fails
     */
    @Test
    @DisplayName("IOException during heap dump still returns response")
    public void testGetHeapDumpIOException() throws Exception {

        MonitoringEndpointDefinition endpointDefinition = Mockito.mock(MonitoringEndpointDefinition.class);
        HeapDumpEndpoint endpoint = new HeapDumpEndpoint(endpointDefinition);
        try (MockedStatic<HeapDumpEndpoint> heapDumpMock = Mockito.mockStatic(HeapDumpEndpoint.class)) {
            heapDumpMock.when(() -> HeapDumpEndpoint.dumpHeap(Mockito.anyString(), Mockito.anyBoolean())).thenThrow(new IOException("failure"));
            Response response = endpoint.getHeapDump();
            assertEquals(200, response.getStatus());
            Object entity = response.getEntity();
            assertNotNull(entity);
            assertEquals(File.class, entity.getClass());
            File dumpFile = (File) entity;
            assertTrue(dumpFile.getName().startsWith(HeapDumpEndpoint.FILE_NAME));
            assertTrue(dumpFile.getName().endsWith(HeapDumpEndpoint.FILE_EXTENSION));
            assertFalse(dumpFile.exists());
            String contentDisposition = response.getHeaderString("Content-Disposition");
            assertNotNull(contentDisposition);
            assertTrue(contentDisposition.contains(dumpFile.getName()));
            assertEquals(MediaType.APPLICATION_OCTET_STREAM_TYPE, response.getMediaType());
        }
    }

    /**
     * Verifies that the static dumpHeap method creates and uses a HotSpotDiagnosticMXBean via ManagementFactory and
     * delegates the call with provided parameters.
     * @throws Exception if test setup fails
     */
    @Test
    @DisplayName("dumpHeap delegates to HotSpotDiagnosticMXBean")
    public void testDumpHeapInvokesHotSpotMxBean() throws Exception {

        String path = "target/test-heap-dump.hprof";
        MBeanServer mbeanServer = Mockito.mock(MBeanServer.class);
        HotSpotDiagnosticMXBean hotSpotBean = Mockito.mock(HotSpotDiagnosticMXBean.class);
        try (MockedStatic<ManagementFactory> managementFactoryMock = Mockito.mockStatic(ManagementFactory.class)) {
            managementFactoryMock.when(ManagementFactory::getPlatformMBeanServer).thenReturn(mbeanServer);
            managementFactoryMock.when(() -> ManagementFactory.newPlatformMXBeanProxy(mbeanServer,
                "com.sun.management:type=HotSpotDiagnostic", HotSpotDiagnosticMXBean.class)).thenReturn(hotSpotBean);
            HeapDumpEndpoint.dumpHeap(path, true);
            Mockito.verify(hotSpotBean).dumpHeap(path, true);
        }
    }

    /**
     * Verifies delegation to MXBean with live=false flag.
     * @throws Exception if test setup fails
     */
    @Test
    @DisplayName("dumpHeap delegates with live=false")
    public void testDumpHeapInvokesHotSpotMxBeanLiveFalse() throws Exception {

        String path = "target/test-heap-dump-live-false.hprof";
        MBeanServer mbeanServer = Mockito.mock(MBeanServer.class);
        HotSpotDiagnosticMXBean hotSpotBean = Mockito.mock(HotSpotDiagnosticMXBean.class);
        try (MockedStatic<ManagementFactory> managementFactoryMock = Mockito.mockStatic(ManagementFactory.class)) {
            managementFactoryMock.when(ManagementFactory::getPlatformMBeanServer).thenReturn(mbeanServer);
            managementFactoryMock.when(() -> ManagementFactory.newPlatformMXBeanProxy(mbeanServer,
                "com.sun.management:type=HotSpotDiagnostic", HotSpotDiagnosticMXBean.class)).thenReturn(hotSpotBean);
            HeapDumpEndpoint.dumpHeap(path, false);
            Mockito.verify(hotSpotBean).dumpHeap(path, false);
        }
    }
}
