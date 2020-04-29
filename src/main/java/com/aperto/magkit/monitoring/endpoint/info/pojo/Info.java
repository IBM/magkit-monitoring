package com.aperto.magkit.monitoring.endpoint.info.pojo;

/**
 * 
 * Info POJO.
 * 
 * @author CLAUDIU GONCIULEA (Aperto - An IBM Company)
 * @since 2020-04-09
 *
 */
public class Info {
    
    private Magnolia _magnolia;
    private Environment _environment;

	public Magnolia getMagnolia() {
		return _magnolia;
	}

	public void setMagnolia(Magnolia magnolia) {
		_magnolia = magnolia;
	}

	public Environment getEnvironment() {
		return _environment;
	}

	public void setEnvironment(Environment environment) {
		_environment = environment;
	}
}
