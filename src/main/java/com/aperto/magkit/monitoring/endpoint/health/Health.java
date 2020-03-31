package com.aperto.magkit.monitoring.endpoint.health;

/**
 * 
 * Health Pojo.
 * 
 * @author Soenke Schmidt (Aperto - An IBM Company)
 * @since 2020-03-29
 *
 */
public class Health {

    private String _status = "UP";

    public String getStatus() {
        return _status;
    }

    public void setStatus(String status) {
        _status = status;
    }

}
