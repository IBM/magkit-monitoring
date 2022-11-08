package com.aperto.magkit.monitoring.endpoint.overview;

/**
 * 
 * Endpoint POJO contains the relevant info for each individual endpoint.
 * 
 * @author Dan Olaru (IBM)
 * @since 2020-04-24
 *
 */

public class EndpointInfo {

    private String _name = "";
    private String _path = "";

    public EndpointInfo(String name, String path) {
        super();
        _name = name;
        _path = path;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getPath() {
        return _path;
    }

    public void setPath(String path) {
        _path = path;
    }

}
