package com.aperto.magkit.monitoring.endpoint.overview;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * Endpoints by Category POJO.
 * 
 * 
 * @author Dan Olaru (IBM)
 * @since 2020-04-24
 *
 */

public class EndpointsByCategory {
	
	//the name of the category of endpoints this object collects, e.g. "v1, ..." or "custom"
	private String _category = "";
	
	//the endpoints belonging to this category themselves
	private List<EndpointInfo> _endpoints;
	
	public EndpointsByCategory() {
		this._endpoints = new ArrayList<EndpointInfo>();
	}
	
	public EndpointsByCategory(String categoryName, ArrayList<EndpointInfo> endpointList) {
		this._category = categoryName;
		this._endpoints = endpointList;
	}

	//@JsonProperty(" ")
	public String getCategory() {
		return _category;
	}

	public void setCategory(String category) {
		this._category = category;
	}

	//@JsonProperty(" ")
	public List<EndpointInfo> getEndpoints() {
		return _endpoints;
	}

	public void setEndpoints(List<EndpointInfo> endpoints) {
		this._endpoints = endpoints;
	}
	
	

}
