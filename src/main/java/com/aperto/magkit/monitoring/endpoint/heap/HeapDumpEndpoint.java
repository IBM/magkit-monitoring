package com.aperto.magkit.monitoring.endpoint.heap;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;

import javax.inject.Inject;
import javax.management.MBeanServer;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

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

	public static final Integer LINE_LIMIT 		= 1000;	
	public static final String FILE_LOCATION 	= "tmp/heapdump";
	public static final String FILE_NAME 		= "heap_dump";
	public static final String FILE_EXTENSION   = "-jmap.hprof";
	public static final String FILE_ENCODING 	= "UTF-8";
	
    @Inject
    protected HeapDumpEndpoint(MonitoringEndpointDefinition endpointDefinition) {
        super(endpointDefinition);
    }

    @SuppressWarnings("deprecation")
	@GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public String getHeapDump() {
        // by default dump only the live objects
    	StringBuffer heapDumpData = new StringBuffer();
        boolean live = true;
        try {
        	File dir = new File(FILE_LOCATION);
        	dir.mkdirs();
        	File file = new File(dir, FILE_NAME + System.currentTimeMillis() + FILE_EXTENSION);
        	String currentFileName = file.getPath();
			dumpHeap(currentFileName, live);
			LineIterator it = FileUtils.lineIterator(file, FILE_ENCODING);
			int counter = 0;
			try {
			    while (it.hasNext() && counter <= LINE_LIMIT) {
			        heapDumpData.append(it.nextLine());
			        counter++;
			    }
			} finally {
			    LineIterator.closeQuietly(it);
			}
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
