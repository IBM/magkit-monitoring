[![Build Status](http://acid-build.aperto.de/job/MagKit/job/MagKit-Projekte/job/monitoring/job/dev/badge/icon)](http://acid-build.aperto.de/job/MagKit/job/MagKit-Projekte/job/monitoring/job/dev/)

# Magnolia Monitoring Module
The Magnolia Monitoring Module provides sever REST endpoints for monitoring the Magnolia application.

## Resources
| Resource| Link|
|-|-|
|GIT|https://acid-github.aperto.de/magkit/monitoring|
|Jira|https://jira.aperto.de/browse/MGKT-520?jql=project%20%3D%20MGKT%20AND%20component%20%3D%20Monitoring|
|Confluence|https://confluence.aperto.de/display/MAGNOLIA/Monitoring+Module|
|Build|https://acid-build.aperto.de/job/MagKit/job/MagKit-Projekte/job/monitoring/|

### TL;DR
Just add this dependency to your Magnolia installation
```xml
<dependency>
  <groupId>com.aperto.magkit</groupId>
  <artifactId>monitoring</artifactId>
  <version>1.0.0</version>
</dependency>
```

## Endpoints
|ID|Endpoint|Description|
|--|--|--|
|`monitoring`|/.rest/monitoring|Overview of available endpoints|
|`health`|/.rest/monitoring/v1/health|Provides application health information. E.g.: Status 200 if UP|
|`env`|/.rest/monitoring/v1/env|Provides information about all JVM and environment properties|
|`info`|/.rest/monitoring/v1/info|Provides general information about the Magnolia Application and environment|
|`modules`|/.rest/monitoring/v1/modules|Provides a list of all installed Magnolia Modules and their respective version|
|`metrics`|/.rest/monitoring/v1/metrics|Provides general information about the JVM runtime|
|`threaddump`|/.rest/monitoring/v1/threaddump|Provides a thread dump|
|`heapdump`|/.rest/monitoring/v1/heapdump|Provides a heap dump|
|`logs`|./monitoring/v1/logs/{logfile}|Provides the contents of the specified log file|

## Authentication
To access the monitoring endpoints requests have to be authenticated.
The authentication mechanism relies on Magnolias security configuration. Therefore use Basic Authentication with the following credentials
| Username  | Password |
| ------------- | ------------- |
| monitoring  | jFWVuw2jrkznC7pu  |

**The password should be changed in publicly available environemnts**


