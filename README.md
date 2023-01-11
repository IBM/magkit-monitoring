
[![Build Status](https://jenkins.ibmix.de/job/MagKit/job/MagKit-Projekte/job/monitoring/job/dev/badge/icon?subject=DEV&style=flat-square)](https://jenkins.ibmix.de/job/MagKit/job/MagKit-Projekte/job/monitoring/job/dev/) [![Build Status](https://jenkins.ibmix.de/job/MagKit/job/MagKit-Projekte/job/monitoring/job/master/badge/icon?subject=MASTER&style=flat-square)](https://jenkins.ibmix.de/job/MagKit/job/MagKit-Projekte/job/monitoring/job/master/)

# Magnolia Monitoring Module
The Magnolia Monitoring Module provides several REST endpoints for monitoring the Magnolia application.

> **:warning: WARNING:** This module registers a filter in the Magnolia Filter Chain. \
> When uninstalling this module make sure to remove the filter beforehand. Otherwise Magnolia might not be able to start up again.\
> The filter is called **prometheus**

## Resources
| Resource| Link|
|-|-|
|GIT|https://github.ibmix.de/magkit/monitoring|
|Jira|https://jira.ibmix.de/browse/MGKT-520?jql=project%20%3D%20MGKT%20AND%20component%20%3D%20Monitoring|
|Confluence|https://confluence.ibmix.de/display/MAGNOLIA/Monitoring+Module|
|Build|https://jenkins.ibmix.de/job/MagKit/job/MagKit-Projekte/job/monitoring/|

### TL;DR
Just add this dependency to your Magnolia installation
```xml
<dependency>
  <groupId>com.aperto.magkit</groupId>
  <artifactId>monitoring</artifactId>
  <version>1.1.0</version>
</dependency>
```

## Endpoints
|ID|Endpoint|Description|
|--|--|--|
|`monitoring`|`/.rest/monitoring`|Overview of available endpoints|
|`health`|`/.rest/monitoring/v1/health`|Provides application health information. E.g.: Status 200 if UP|
|`env`|`/.rest/monitoring/v1/env`|Provides information about all JVM and environment properties|
|`info`|`/.rest/monitoring/v1/info`|Provides general information about the Magnolia Application and environment|
|`modules`|`/.rest/monitoring/v1/modules`|Provides a list of all installed Magnolia Modules and their respective version|
|`metrics`|`/.rest/monitoring/v1/metrics`|Provides general information about the JVM runtime|
|`threaddump`|`/.rest/monitoring/v1/threaddump`|Provides a thread dump|
|`heapdump`|`/.rest/monitoring/v1/heapdump`|Provides a heap dump|
|`logs`|`/.rest/monitoring/v1/logs/{logfile}`|Provides the contents of the specified log file|
|`prometheus`|`/.rest/monitoring/v1/prometheus`|Provides information from the *Prometheus Exporter*. Use this endpoint to scrape time series data for Prometheus.

## Authentication
> **:warning: WARNING:** Access to the endpoints is controlled by the Magnolia Security App! \
> Make sure that the user anonymous does not have access to paths starting with `/.rest/monitoring`

To access the monitoring endpoints requests have to be authenticated.
The authentication mechanism relies on Magnolias security configuration. Therefore use Basic Authentication with the following credentials
| Username  | Password |
| ------------- | ------------- |
| monitoring  | jFWVuw2jrkznC7pu  |

**The password should be changed after the initial setup**

## Prometheus Monitoring
The Prometheus Endpoint exposes time series data concerning different aspects of the application. This data is exposed by several metrics in simple text based format specific for prometheus.

### Configuration
The metrics exposed by this endpoint can be configured via the application's property files (i.e. _magnolia.properties_):
<table>
    <tr>
        <th>Property Name</th>
        <th>Description</th>
    </tr>
    <tr>
        <td valign="top"><tt>magnolia.monitoring.prometheus.metrics</tt></td>
        <td>
            <p>
              Comma seperated list of metric collectors. The following collectors are available:
            </p>
            <ul>
                <li>Uptime</li>
                <li>Processor</li>
                <li>FileDescriptor</li>
                <li>JvmInfo</li>
                <li>JvmMemory</li>
                <li>JvmHeapPressure</li>
                <li>JvmThread</li>
                <li>JvmGc</li>
                <li>ClassLoader</li>
                <li>Log4J2</li>
                <li>Http</li>
            </ul>
            <p>By default the following collectors are active: <tt>Uptime,Processor,JvmThread,JvmGc,JvmMemory,ClassLoader,Log4J2</tt></p>
            <p>See <b>Collectors</b> for a list of exposed metrics by the collectors.</p>
            <p><b>Example</b></p>
            <tt>magnolia.monitoring.prometheus.metrics=Uptime,Processor,JvmInfo,Log4j2</tt>
        </td>
    </tr>
    <tr>
        <td valign="top"><tt>magnolia.monitoring.prometheus.http.uris</tt></td>
        <td>
          <p>This configuration property only applies if the <i>HTTP Metric Collector</i> is active.</p>
          <p>
            The <b><i>Http Collector</i></b> exposes information about the http requests processed by Magnolia. By default the exposed metrics <b>do not</b> distinguish by the URI of the request.<br/>
            In order to get metrics for a <b>specific URI</b>, the URI has to be added to this comma separated list.<br/>(It is also possible to use a regular expression to collect metrics for multiple URIs.)
          </p>
          <p><b>Example:</b></p>
          <tt>magnolia.monitoring.prometheus.http.uris=/author/.magnolia/.*,/.rest/custom-endpoint</tt>
        </td>
    </tr>
    <tr>
        <td valign="top"><tt>magnolia.monitoring.prometheus.http.slo</tt></td>
        <td>
          <p>
            Comma separated list of <b><i>Service Level Objects</i></b> for the <i>Http Metric Collector</i>. Configuring SLOs exposes the http metrics as histogram with cumulative counters for observation buckets. This effectively sorts the requests into buckets that are defined by the SLOs. The time unit of the SLOs is in <b>milliseconds</b>.
          </p>
          <p><b>Example:</b></p>
          <tt>magnolia.monitoring.prometheus.http.slo=100,1000,5000</tt>
        </td>
   </tr>
</table>

### Collectors
The following metrics are exposed by the different Metric Collectors:

#### Uptime
|Metric|Type|Description|
|------|----|-----------|
|`process_uptime_seconds`|Gauge|The uptime of the Java virtual machine.|
|`process_start_time_seconds`|Gauge|Start time of the process since unix epoch.|

#### Processor
|Metric|Type|Description|
|------|----|-----------|
|`system_cpu_usage`|Gauge|The "recent cpu usage" of the system the application is running in.|
|`system_cpu_count`|Gauge|The number of processors available to the Java virtual machine.|
|`system_load_average_1m`|Gauge|The sum of the number of runnable entities queued to available processors and the number of runnable entities running on the available processors averaged over a period of time.|
|`process_cpu_usage`|Gauge|The "recent cpu usage" for the Java Virtual Machine process.|

#### FileDescriptor
|Metric|Type|Description|
|------|----|-----------|
|`process_files_open_files`|Gauge|The open file descriptor count.|
|`process_files_max_files`|Gauge|The maximum file descriptor count.|

#### JvmInfo
|Metric|Type|Description|
|------|----|-----------|
|`jvm_info`|Gauge|JVM version info.|

#### JvmMemory
|Metric|Type|Description|
|------|----|-----------|
|`jvm_memory_max_bytes`|Gauge|The maximum amount of memory in bytes that can be used for memory management.|
|`jvm_memory_committed_bytes`|Gauge|The amount of memory in bytes that is committed for the Java virtual machine to use.|
|`jvm_buffer_count_buffers`|Gauge|An estimate of the number of buffers in the pool.|
|`jvm_memory_used_bytes`|Gauge|The amount of used memory.|
|`jvm_buffer_total_capacity_bytes`|Gauge|An estimate of the total capacity of the buffers in this pool.|
|`jvm_buffer_memory_used_bytes`|Gauge|An estimate of the memory that the Java virtual machine is using for this buffer pool.|

#### JvmHeapPressure
|Metric|Type|Description|
|------|----|-----------|
|`jvm_memory_usage_after_gc_percent`|Gauge|The percentage of long-lived heap pool used after the last GC event, in the range [0..1].|
|`jvm_gc_overhead_percent`|Gauge|An approximation of the percent of CPU time used by GC activities over the last lookback period or since monitoring began, whichever is shorter, in the range [0..1].|

#### JvmThread
|Metric|Type|Description|
|------|----|-----------|
|`jvm_threads_peak_threads`|Gauge|The peak live thread count since the Java virtual machine started or peak was reset.|
|`jvm_threads_states_threads`|Gauge|The current number of threads.|
|`jvm_threads_daemon_threads`|Gauge|The current number of live daemon threads.|
|`jvm_threads_live_threads`|Gauge|The current number of live threads including both daemon and non-daemon threads.|

#### JvmGc
|Metric|Type|Description|
|------|----|-----------|
|`jvm_gc_pause_seconds`|Summary|Time spent in GC pause.|
|`jvm_gc_pause_seconds_max`|Gauge|Time spent in GC pause.|
|`jvm_gc_live_data_size_bytes`|Gauge|Size of long-lived heap memory pool after reclamation.|
|`jvm_gc_max_data_size_bytes`|Gauge|Max size of long-lived heap memory pool.|
|`jvm_gc_memory_promoted_bytes_total`|Counter|Count of positive increases in the size of the old generation memory pool before GC to after GC.|
|`jvm_gc_memory_allocated_bytes_total`|Counter|Incremented for an increase in the size of the (young) heap memory pool after one GC to before the next.|

#### ClassLoader
|Metric|Type|Description|
|------|----|-----------|
|`jvm_classes_unloaded_classes_total`|Counter|The total number of classes unloaded since the Java virtual machine has started execution.|
|`jvm_classes_loaded_classes`|Gauge|The number of classes that are currently loaded in the Java virtual machine.|

#### Log4J2
|Metric|Type|Description|
|------|----|-----------|
|`log4j2_events_total`|Counter|Number of level log events.|

#### Http
|Metric|Type|Description|
|------|----|-----------|
|`http_server_requests_seconds`|Summary or Histogram|Summary of http request processing duration by method and http status.<br/> ⚠️ If _SLOs_ are configured this metric becomes a _Histogram_ instead of a _Summary_|
|`http_server_requests_seconds_max`|Gauge|Maximum http request processing duration by method and http status.|