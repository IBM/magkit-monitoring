package com.aperto.magkit.monitoring.endpoint;

import info.magnolia.rest.EndpointDefinition;

/**
 * 
 * Definition of a Monitoring Endpoint.
 * 
 * @author Soenke Schmidt (Aperto - An IBM Company)
 * @since 2020-03-31
 *
 */
public interface MonitoringEndpointDefinition extends EndpointDefinition {

    boolean isSensitive();

}
