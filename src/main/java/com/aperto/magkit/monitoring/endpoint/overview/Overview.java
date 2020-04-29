package com.aperto.magkit.monitoring.endpoint.overview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;

/**
 * 
 * Overview POJO.
 * 
 * @author Dan Olaru (IBM)
 * @since 2020-04-24
 *
 */

public class Overview {
	
	
	private List<EndpointsByCategory> _endpoints;

	public Overview() {
		super();
		this._endpoints = new ArrayList<EndpointsByCategory>();
	}

	public Overview(List<EndpointsByCategory> _endpoints) {
		super();
		this._endpoints = _endpoints;
	}

	@JsonProperty(" ")
	public List<EndpointsByCategory> getEndpoints() {
		return _endpoints;
	}

	public void setEndpoints(List<EndpointsByCategory> endpoints) {
		this._endpoints = endpoints;
	}
	
	public boolean categoryExists(String category) {
		
		if (_endpoints.stream().anyMatch(p -> p.getCategory().equals(category))) return true;
		return false;
	}
	
	public void insertByCategory(EndpointInfo endpointInfo, String category) {
		for (EndpointsByCategory cat : _endpoints) {
			if (cat.getCategory().equals(category)) {
				cat.getEndpoints().add(endpointInfo);
				return;
			}
		}
		
		//the specific category was not found, must make a new one and add the element
		_endpoints.add(new EndpointsByCategory(category, new ArrayList (Arrays.asList(endpointInfo))));
	}
	
}
