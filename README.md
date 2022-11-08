[![Build Status](https://jenkins.ibmix.de/job/MagKit/job/MagKit-Projekte/job/monitoring/job/dev/badge/icon?subject=DEV&style=flat-square)](https://jenkins.ibmix.de/job/MagKit/job/MagKit-Projekte/job/monitoring/job/dev/)[![Build Status](https://jenkins.ibmix.de/job/MagKit/job/MagKit-Projekte/job/monitoring/job/master/badge/icon?subject=MASTER&style=flat-square)](https://jenkins.ibmix.de/job/MagKit/job/MagKit-Projekte/job/monitoring/job/master/)

# Magnolia Monitoring Module
The Magnolia Monitoring Module provides sever REST endpoints for monitoring the Magnolia application.

> **:warning: WARNING:** This module registers a filter in the Magnolia Filter Chain. \
> When uninstalling this module make sure to remove the filter beforehand. Otherwise Magnolia might not be able to start up again.

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
  <version>1.0.1</version>
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
|`prometheus`|`/.rest/monitoring/prometheus`|Provides information from the *Prometheus Exporter*. Use this endpoint to scrape time series data for Prometheus.

## Authentication
> **:warning: WARNING:** Access to the endpoints is controlled by the Magnolia Security App! \
> Make sure that the user anonymous does not have access to paths starting with `/.rest/monitoring`

To access the monitoring endpoints requests have to be authenticated.
The authentication mechanism relies on Magnolias security configuration. Therefore use Basic Authentication with the following credentials
| Username  | Password |
| ------------- | ------------- |
| monitoring  | jFWVuw2jrkznC7pu  |

**The password should be changed after the initial setup**


