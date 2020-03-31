package com.aperto.magkit.monitoring.endpoint;

import info.magnolia.rest.AbstractEndpoint;

/**
 * Abstract Endpoint. Should be implemented by all other Monitoring Endpoints.
 * 
 * @author Soenke Schmidt (Aperto - An IBM Company)
 * @since 2020-03-29
 *
 */
public abstract class AbstractMonitoringEndpoint<D extends MonitoringEndpointDefinition> extends AbstractEndpoint<D>{

    protected AbstractMonitoringEndpoint(D endpointDefinition) {
        super(endpointDefinition);
    }
}
