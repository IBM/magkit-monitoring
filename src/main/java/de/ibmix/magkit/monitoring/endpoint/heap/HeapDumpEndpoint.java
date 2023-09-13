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
 *
 * Heap Dump Endpoint.
 *
 * @author Sorina Radulescu
 * @since 2020-04-11
 *
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

    public static void dumpHeap(String filePath, boolean live) throws IOException {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        HotSpotDiagnosticMXBean mxBean = ManagementFactory.newPlatformMXBeanProxy(server,
                "com.sun.management:type=HotSpotDiagnostic", HotSpotDiagnosticMXBean.class);
        mxBean.dumpHeap(filePath, live);
    }

}
