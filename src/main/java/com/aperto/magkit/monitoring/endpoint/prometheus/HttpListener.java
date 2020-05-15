package com.aperto.magkit.monitoring.endpoint.prometheus;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.util.StringUtils;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;

/**
 * Test Hook.
 * 
 * @author VladNacu
 *
 */
@WebListener
public class HttpListener implements ServletRequestListener {

    private static final PrometheusMeterRegistry REGISTRY = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        HttpServletRequest httpRequest = (HttpServletRequest) sre.getServletRequest();
       
        String httpPath = StringUtils.isNotEmpty(httpRequest.getRequestURI()) ? httpRequest.getRequestURI() : "";
        String httpMethod = StringUtils.isNotEmpty(httpRequest.getMethod()) ? httpRequest.getMethod() : "";

        Counter httpRequestsTotal = Counter.builder("http_request_total")
                .description("A counter of the total number of HTTP requests.")
                .tags("method", httpRequest.getMethod(), "path", httpPath)
                .register(REGISTRY);

        httpRequestsTotal.increment();
    }

    public static PrometheusMeterRegistry getRegistry() {
        return REGISTRY;
    }

}
