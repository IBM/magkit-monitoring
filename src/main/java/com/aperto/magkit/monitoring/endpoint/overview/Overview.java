package com.aperto.magkit.monitoring.endpoint.overview;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * Overview POJO.
 * 
 * @author Dan Olaru (IBM)
 * @since 2020-04-24
 *
 */

public class Overview {

	private Map<String, List<EndpointInfo>> _categorizedEndpoints;
	
	public Overview() {
		super();
		_categorizedEndpoints = new HashMap<>();
	}

	public Map<String, List<EndpointInfo>> getCategorizedEndpoints() {
		return _categorizedEndpoints;
	}

	public void setCategorizedEndpoints(Map<String, List<EndpointInfo>> categorizedEndpoints) {
		this._categorizedEndpoints = categorizedEndpoints;
	}

}
