package com.aperto.magkit.monitoring.config.prometheus;

import java.util.Arrays;
import java.util.List;

import javax.inject.Named;

import com.google.inject.Inject;

import static java.util.Collections.emptyList;

/**
 * 
 * Configuration bean for Prometheus Registry and respective metrics.
 * 
 * @author Soenke Schmidt - IBM iX
 * @since 02.01.2023
 *
 */
public class PrometheusConfig {

    private List<String> _metrics = emptyList();

    @Inject
    private HttpRequestMetricsConfig _httpRequestMetricsConfig;

    public List<String> getMetrics() {
        return _metrics;
    }

    public void setMetrics(List<String> metrics) {
        _metrics = metrics;
    }

    public HttpRequestMetricsConfig getHttpRequestMetricsConfig() {
        return _httpRequestMetricsConfig;
    }

    public void setHttpRequestMetricsConfig(HttpRequestMetricsConfig httpRequestMetricsConfig) {
        _httpRequestMetricsConfig = httpRequestMetricsConfig;
    }

    @Inject(optional = true)
    protected void provideMetricsFromProps(@Named("magnolia.monitoring.prometheus.metrics") String metricsProp) {
        _metrics = Arrays.asList(metricsProp.split(","));
    }

}
