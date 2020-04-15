package com.aperto.magkit.monitoring.endpoint.heap;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Paths;

import javax.inject.Inject;
import javax.management.MBeanServer;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;

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
        // by default dump only the live objects
    	StringBuffer heapDumpData = new StringBuffer();
        boolean live = true;
        try {
        	File dir = new File("tmp/heapdump");
        	dir.mkdirs();
        	File file = new File(dir, "heap_dump" + System.currentTimeMillis() + "-jmap.hprof");
        	String currentFileName = file.getPath();
        	System.out.println("currentFileName= " + currentFileName);
			dumpHeap(currentFileName, live);
			//heapDumpData = FileUtils.readFileToString(file, "UTF-8");
			LineIterator it = FileUtils.lineIterator(file, "UTF-8");
			int counter = 0;
			try {
			    while (it.hasNext() && counter <= 1000) {
			        heapDumpData.append(it.nextLine());
			        counter++;
			    }
			} finally {
			    LineIterator.closeQuietly(it);
			}
			System.out.println("heapDumpData= " + heapDumpData);
		} catch (IOException e) {
			e.printStackTrace();
		}
        return heapDumpData.toString();  
    }
    
    public static void dumpHeap(String filePath, boolean live) throws IOException {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        HotSpotDiagnosticMXBean mxBean = ManagementFactory.newPlatformMXBeanProxy(
          server, "com.sun.management:type=HotSpotDiagnostic", HotSpotDiagnosticMXBean.class);
        mxBean.dumpHeap(filePath, live);
    }

}
