package de.ibmix.magkit.monitoring.endpoint.thread;

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
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.ibmix.magkit.monitoring.endpoint.AbstractMonitoringEndpoint;
import de.ibmix.magkit.monitoring.endpoint.MonitoringEndpointDefinition;

import info.magnolia.rest.DynamicPath;

/**
 * REST endpoint producing a textual JVM thread dump for diagnostic purposes.
 * Delegates to {@link java.lang.management.ThreadMXBean} to collect active thread information.
 * <p><strong>Purpose</strong></p>
 * Offers a lightweight liveness and contention analysis aid by exposing thread states and stack traces.
 * <p><strong>Main Functionality</strong></p>
 * Captures all active threads via {@link ThreadMXBean#dumpAllThreads(boolean, boolean)} and concatenates their formatted
 * {@link java.lang.management.ThreadInfo#toString()} representations into a single textual dump.
 * <p><strong>Key Features</strong></p>
 * <ul>
 * <li>Generates full thread dump including stack traces.</li>
 * <li>Plain text response for easy viewing.</li>
 * </ul>
 * <p><strong>Null and Error Handling</strong></p>
 * Always returns a non-null string. No explicit exception handling; relies on JVM MXBean availability.
 * <p><strong>Thread-Safety</strong></p>
 * Stateless; safe for concurrent invocations. Under heavy load dumps may be expensive.
 * <p><strong>Usage Example</strong></p>
 * <pre>{@code
 * String dump = threadDumpEndpoint.getThreadDump();
 * }</pre>
 * <p><strong>Important Details</strong></p>
 * ThreadInfo formatting already includes stack trace; consumers should avoid additional parsing for performance reasons.
 * @author Sorina Radulescu
 * @since 2020-04-11
 */
@Path("")
@DynamicPath
public class ThreadDumpEndpoint extends AbstractMonitoringEndpoint<MonitoringEndpointDefinition> {

    /**
     * Injection constructor wiring the monitoring endpoint definition.
     * @param endpointDefinition endpoint definition metadata
     */
    @Inject
    protected ThreadDumpEndpoint(MonitoringEndpointDefinition endpointDefinition) {
        super(endpointDefinition);
    }

    /**
     * Returns the current thread dump as plain text.
     * @return textual thread dump containing threads and stack traces
     */
    @GET
    @Path("")
    @Produces(MediaType.TEXT_PLAIN)
    public String getThreadDump() {
        String threadDump = threadDump(false, false);
        return threadDump;
    }

    private static String threadDump(boolean lockedMonitors, boolean lockedSynchronizers) {
        StringBuffer threadDump = new StringBuffer(System.lineSeparator());
        ThreadMXBean threadMxBean = ManagementFactory.getThreadMXBean();
        for (ThreadInfo threadInfo : threadMxBean.dumpAllThreads(lockedMonitors, lockedSynchronizers)) {
            threadDump.append(threadInfo.toString());
        }
        return threadDump.toString();
    }

}
