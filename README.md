
[![Build Status](https://jenkins.ibmix.de/job/MagKit/job/MagKit-Projekte/job/monitoring/job/dev/badge/icon?subject=DEV&style=flat-square)](https://jenkins.ibmix.de/job/MagKit/job/MagKit-Projekte/job/monitoring/job/dev/) [![Build Status](https://jenkins.ibmix.de/job/MagKit/job/MagKit-Projekte/job/monitoring/job/master/badge/icon?subject=MASTER&style=flat-square)](https://jenkins.ibmix.de/job/MagKit/job/MagKit-Projekte/job/monitoring/job/master/)

# Magnolia Monitoring Module
The Magnolia Monitoring Module provides sever REST endpoints for monitoring the Magnolia application.

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

## Prometheus Configuration
The prometheus endpoint exposes information as time series data identified by metric name and key/value pairs. The metrics are exposed using a simple format.
The _Monitoring Module_ provides a set of basic metrics (based on [micrometer](https://micrometer.io/)) that can be enabled or disabled via configuration. In order to configure the exposed metrics the following _optional_ properties can be used:

<table>
    <tr>
        <th>Property Name</th>
        <th>Description</th>
    </tr>
    <tr>
        <td><b>magnolia.monitoring.prometheus.metrics</b></td>
        <td>
            Comma Seperated list of metric collectors. The following metric collectors can be selected to expose their metrics via the prometheus endpoint:
            <ul>
                <li>Uptime</li>
                <li>Processor</li>
                <li>FileDescriptor</li>
                <li>JvmInfo</li>
                <li>JvmHeapPressure</li>
                <li>JvmThread</li>
                <li>JvmGc</li>
                <li>ClassLoader</li>
                <li>Log4J2</li>
                <li>Http</li>
            </ul>
            The <i>Uptime</i> Metric is enabled by default.
        </td>
    </tr>
    <tr>
        <td><b>magnolia.monitoring.prometheus.http.uris</b></td>
        <td>
            The <b><i>Http Collector</i></b> exposes information about the http requests processed by Magnolia. By default the exposed metrics <b>do not</b> distinguish by the URI of the request.<br/>
            In order to get metrics for a <b>specific URI</b>, the URI has to be added to this comma separated list.<br/>(It is also possible to use a regular expression to collect metrics for multiple URIs.) E.g.<br/>
            <pre>
/author/.magnolia/.*,/.rest/custom-endpoint
            </pre>
        </td>
    </tr>
    <tr>
        <td><b>magnolia.monitoring.prometheus.http.slo</b></td>
        <td>
            Comma separated list of <b><i>Service Level Objects</i></b> for the <i>Http Metric Collector</i>. Configuring SLOs creates exposes the http request duration as histograms, effectively sorting the request durations into buckets that are defined by the SLOs. The time unit of the SLOs is in milliseconds. E.g.:<br/>
            <pre>
100,1000,5000
            </pre>
        </td>
   </tr>
</table>

