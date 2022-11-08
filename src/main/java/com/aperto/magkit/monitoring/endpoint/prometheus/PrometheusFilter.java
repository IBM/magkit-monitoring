package com.aperto.magkit.monitoring.endpoint.prometheus;

import java.io.IOException;
import java.time.Duration;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import info.magnolia.cms.filters.AbstractMgnlFilter;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.LongTaskTimer;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.apache.commons.lang3.StringUtils;

/**
 * Prometheus Filter class.
 * 
 * @author VladNacu
 *
 */
public class PrometheusFilter extends AbstractMgnlFilter {

    private static final String COUNTER_NAME = "http_requests_count";
    private static final String COUNTER_DESCRIPTION = "A counter of the total number of HTTP requests.";
    private static final String TIMER_NAME = "http_requests_durations";
    private static final String TIMER_DESCRIPTION = "A timer of the request latency";
    private static final String TAG_METHOD = "method";
    private static final String TAG_STATUS = "status";
    private static final String TAG_URI = "uri";
    private static final Duration[] SLA_BUCKETS = { Duration.ofMillis(100), Duration.ofMillis(500), Duration.ofMillis(1000), Duration.ofMillis(5000) };

    private final PrometheusMeterRegistry _registry;

    @Inject
    public PrometheusFilter(PrometheusMeterRegistry registry) {
        _registry = registry;
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        final String httpPath = StringUtils.isNotEmpty(request.getRequestURI()) ? request.getRequestURI() : "";
        final String httpStatus = String.valueOf(response.getStatus());

        LongTaskTimer httpProcessingTimer = LongTaskTimer.builder(TIMER_NAME)
            .description(TIMER_DESCRIPTION)
            .tags(TAG_METHOD, request.getMethod(), TAG_STATUS, httpStatus, TAG_URI, httpPath)
            .serviceLevelObjectives(SLA_BUCKETS)
            .register(_registry);

        LongTaskTimer.Sample currentRequestSample = httpProcessingTimer.start();

        Counter httpRequestCounter = Counter.builder(COUNTER_NAME)
            .description(COUNTER_DESCRIPTION)
            .tags(TAG_METHOD, request.getMethod(), TAG_STATUS, httpStatus, TAG_URI, httpPath)
            .register(_registry);

        httpRequestCounter.increment();

        try {
            chain.doFilter(request, response);
        } finally {
            currentRequestSample.stop();
        }

    }
}