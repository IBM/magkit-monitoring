package de.ibmix.magkit.monitoring.endpoint.heap;

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

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;

import javax.inject.Inject;
import javax.management.MBeanServer;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.ibmix.magkit.monitoring.endpoint.AbstractMonitoringEndpoint;
import de.ibmix.magkit.monitoring.endpoint.MonitoringEndpointDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.management.HotSpotDiagnosticMXBean;

import info.magnolia.rest.DynamicPath;

/**
 * REST endpoint producing a JVM heap dump file for offline memory analysis.
 * Uses HotSpot diagnostic MBean to create an HPROF heap dump served as a downloadable binary.
 * <p><strong>Purpose</strong></p>
 * Enables on-demand capture of heap state for troubleshooting memory leaks and object retention.
 * <p><strong>Main Functionality</strong></p>
 * Creates a timestamped file in a configurable directory, invokes {@link com.sun.management.HotSpotDiagnosticMXBean#dumpHeap(String, boolean)} and streams the resulting file with appropriate Content-Disposition headers.
 * <p><strong>Key Features</strong></p>
 * <ul>
 * <li>Generates HPROF file named with timestamp.</li>
 * <li>Dumps only live objects by default.</li>
 * <li>Streams result as octet-stream with Content-Disposition header.</li>
 * </ul>
 * <p><strong>Usage Preconditions</strong></p>
 * Requires HotSpot JVM and sufficient file system permissions to create dump directory and file.
 * <p><strong>Null and Error Handling</strong></p>
 * On I/O errors logs exception and still attempts to respond; resulting file may be null causing runtime issues downstream.
 * <p><strong>Thread-Safety</strong></p>
 * Stateless; concurrent dumps may consume disk and memory resources. Consider rate limiting.
 * <p><strong>Usage Example</strong></p>
 * <pre>{@code
 * Response r = heapDumpEndpoint.getHeapDump();
 * }</pre>
 * <p><strong>Important Details</strong></p>
 * Heap dumps are potentially large; consumers should implement retention and cleanup to avoid disk exhaustion.
 * @author Sorina Radulescu
 * @since 2020-04-11
 */
@Path("")
@DynamicPath
public class HeapDumpEndpoint extends AbstractMonitoringEndpoint<MonitoringEndpointDefinition> {

    private static final Logger LOGGER = LoggerFactory.getLogger(HeapDumpEndpoint.class);

    public static final Integer LINE_LIMIT = 1000;
    public static final String FILE_LOCATION = "tmp/heapdump";
    public static final String FILE_NAME = "heap_dump";
    public static final String FILE_EXTENSION = "-jmap.hprof";
    public static final String FILE_ENCODING = "UTF-8";

    @Inject
    protected HeapDumpEndpoint(MonitoringEndpointDefinition endpointDefinition) {
        super(endpointDefinition);
    }

    /**
     * Generates a heap dump file and returns it as downloadable response.
     * @return response containing heap dump file as attachment
     */
    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getHeapDump() {
        // by default dump only the live objects
        // StringBuffer heapDumpData = new StringBuffer();
        File file = null;
        boolean live = true;
        try {
            File dir = new File(FILE_LOCATION);
            dir.mkdirs();
            file = new File(dir, FILE_NAME + System.currentTimeMillis() + FILE_EXTENSION);
            String currentFileName = file.getPath();
            dumpHeap(currentFileName, live);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }

        // Header setting is optional
        return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"").build();
    }

    /**
     * Invokes HotSpot MXBean to write a heap dump to the given file path.
     * @param filePath absolute or relative file path target
     * @param live if true dump only live objects
     * @throws IOException if dump cannot be written
     */
    public static void dumpHeap(String filePath, boolean live) throws IOException {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        HotSpotDiagnosticMXBean mxBean = ManagementFactory.newPlatformMXBeanProxy(server,
                "com.sun.management:type=HotSpotDiagnostic", HotSpotDiagnosticMXBean.class);
        mxBean.dumpHeap(filePath, live);
    }

}
