package com.aperto.magkit.monitoring.endpoint.info.pojo;

/**
 * 
 * License POJO.
 * 
 * @author CLAUDIU GONCIULEA (Aperto - An IBM Company)
 * @since 2020-04-09
 *
 */
public class License {

	private String _owner;
	private String _expirationDate;
	
	public String getOwner() {
		return _owner;
	}
	
	public void setOwner(String owner) {
		_owner = owner;
	}
	
	public String getExpirationDate() {
		return _expirationDate;
	}
	
	public void setExpirationDate(String expirationDate) {
		_expirationDate = expirationDate;
	}
}
