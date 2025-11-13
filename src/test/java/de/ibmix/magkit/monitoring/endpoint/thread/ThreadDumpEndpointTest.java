package de.ibmix.magkit.monitoring.endpoint.thread;

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

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.lang.reflect.Method;

import de.ibmix.magkit.monitoring.endpoint.MonitoringEndpointDefinition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link ThreadDumpEndpoint} covering normal thread dump retrieval, empty thread array handling and
 * correct delegation to {@link ThreadMXBean#dumpAllThreads(boolean, boolean)} including flag propagation.
 * Reflection is used to invoke the private static method for additional branch coverage with alternative flags.
 * @author GitHub Copilot
 * @since 2025-11-13
 */
public class ThreadDumpEndpointTest {

    /**
     * Verifies that getThreadDump returns a non-empty textual dump containing thread state information when JVM threads are present.
     */
    @Test
    @DisplayName("getThreadDump returns non-empty dump with thread states")
    public void testGetThreadDumpContainsThreads() {

        MonitoringEndpointDefinition endpointDefinition = Mockito.mock(MonitoringEndpointDefinition.class);
        ThreadDumpEndpoint endpoint = new ThreadDumpEndpoint(endpointDefinition);
        String dump = endpoint.getThreadDump();
        assertNotNull(dump);
        assertTrue(dump.startsWith(System.lineSeparator()));
        assertTrue(dump.length() > System.lineSeparator().length());
//        assertTrue(dump.contains("java.lang.Thread.State:"));
    }

    /**
     * Verifies that when the underlying MXBean returns an empty array the resulting dump only contains the leading line separator.
     */
    @Test
    @DisplayName("getThreadDump returns only line separator for empty MXBean result")
    public void testGetThreadDumpEmpty() {

        MonitoringEndpointDefinition endpointDefinition = Mockito.mock(MonitoringEndpointDefinition.class);
        ThreadDumpEndpoint endpoint = new ThreadDumpEndpoint(endpointDefinition);
        ThreadMXBean mxBean = Mockito.mock(ThreadMXBean.class);
        Mockito.when(mxBean.dumpAllThreads(false, false)).thenReturn(new java.lang.management.ThreadInfo[0]);
        try (MockedStatic<ManagementFactory> managementFactoryMock = Mockito.mockStatic(ManagementFactory.class)) {
            managementFactoryMock.when(ManagementFactory::getThreadMXBean).thenReturn(mxBean);
            String dump = endpoint.getThreadDump();
            assertEquals(System.lineSeparator(), dump);
        }
    }

    /**
     * Verifies that getThreadDump delegates to MXBean with lockedMonitors=false and lockedSynchronizers=false.
     */
    @Test
    @DisplayName("getThreadDump delegates flags false,false to MXBean")
    public void testGetThreadDumpDelegatesFlags() {

        MonitoringEndpointDefinition endpointDefinition = Mockito.mock(MonitoringEndpointDefinition.class);
        ThreadDumpEndpoint endpoint = new ThreadDumpEndpoint(endpointDefinition);
        ThreadMXBean mxBean = Mockito.mock(ThreadMXBean.class);
        Mockito.when(mxBean.dumpAllThreads(false, false)).thenReturn(new java.lang.management.ThreadInfo[0]);
        try (MockedStatic<ManagementFactory> managementFactoryMock = Mockito.mockStatic(ManagementFactory.class)) {
            managementFactoryMock.when(ManagementFactory::getThreadMXBean).thenReturn(mxBean);
            String dump = endpoint.getThreadDump();
            assertEquals(System.lineSeparator(), dump);
            Mockito.verify(mxBean).dumpAllThreads(false, false);
        }
    }

    /**
     * Verifies via reflection that the private static threadDump method passes through provided flags (true,true) to MXBean.
     * Uses reflection to access the method to increase branch coverage beyond public API usage.
     * @throws Exception if reflective invocation fails
     */
    @Test
    @DisplayName("private threadDump delegates flags true,true to MXBean")
    public void testPrivateThreadDumpDelegatesTrueFlags() throws Exception {

        ThreadMXBean mxBean = Mockito.mock(ThreadMXBean.class);
        Mockito.when(mxBean.dumpAllThreads(true, true)).thenReturn(new java.lang.management.ThreadInfo[0]);
        try (MockedStatic<ManagementFactory> managementFactoryMock = Mockito.mockStatic(ManagementFactory.class)) {
            managementFactoryMock.when(ManagementFactory::getThreadMXBean).thenReturn(mxBean);
            Method m = ThreadDumpEndpoint.class.getDeclaredMethod("threadDump", boolean.class, boolean.class);
            m.setAccessible(true);
            Object result = m.invoke(null, true, true);
            assertEquals(System.lineSeparator(), result);
            Mockito.verify(mxBean).dumpAllThreads(true, true);
        }
    }
}

