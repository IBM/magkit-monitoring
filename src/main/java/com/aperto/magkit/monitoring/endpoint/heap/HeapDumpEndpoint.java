package com.aperto.magkit.monitoring.endpoint.heap;


import java.io.IOException;
import java.lang.management.ManagementFactory;

import javax.inject.Inject;
import javax.management.MBeanServer;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.aperto.magkit.monitoring.endpoint.AbstractMonitoringEndpoint;
import com.aperto.magkit.monitoring.endpoint.MonitoringEndpointDefinition;
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

    @Inject
    protected HeapDumpEndpoint(MonitoringEndpointDefinition endpointDefinition) {
        super(endpointDefinition);
    }

    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public String getHeapDump() {
    	// default heap dump file name
        String fileName = "heap.bin";
        // by default dump only the live objects
        boolean live = true;
        try {
			dumpHeap(fileName, live);
		} catch (IOException e) {
			e.printStackTrace();
		}
        return "";  //TODO
    }
    
    public static void dumpHeap(String filePath, boolean live) throws IOException {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        HotSpotDiagnosticMXBean mxBean = ManagementFactory.newPlatformMXBeanProxy(
          server, "com.sun.management:type=HotSpotDiagnostic", HotSpotDiagnosticMXBean.class);
        mxBean.dumpHeap(filePath, live);
    }

}
