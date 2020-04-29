package com.aperto.magkit.monitoring.endpoint.info.pojo;

/**
 * 
 * Magnolia POJO.
 * 
 * @author CLAUDIU GONCIULEA (Aperto - An IBM Company)
 * @since 2020-04-09
 *
 */
public class Magnolia {

	private String _edition;
	private String _version;
	private String _instance;
	
	private License _license;

	public String getEdition() {
		return _edition;
	}

	public void setEdition(String edition) {
		_edition = edition;
	}

	public String getVersion() {
		return _version;
	}

	public void setVersion(String version) {
		_version = version;
	}

	public String getInstance() {
		return _instance;
	}

	public void setInstance(String instance) {
		_instance = instance;
	}

	public License getLicense() {
		return _license;
	}

	public void setLicense(License license) {
		_license = license;
	}
}
