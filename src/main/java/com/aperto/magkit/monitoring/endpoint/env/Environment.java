package com.aperto.magkit.monitoring.endpoint.env;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Environment Class
 *
 */
public class Environment {

	private List<String> _jvmArgs = new ArrayList<>();

	private Map<String, String> _sysProp = new HashMap<>();

	/**
	 * default no args constructor
	 */
	public Environment() {

	}

	public List<String> getJvmArgs() {
		return _jvmArgs;
	}

	public void setJvmArgs(List<String> jvmArgs) {
		this._jvmArgs = jvmArgs;
	}

	public Map<String, String> getSysProp() {
		return _sysProp;
	}

	public void setSysProp(Map<String, String> sysProp) {
		this._sysProp = sysProp;
	}

}
