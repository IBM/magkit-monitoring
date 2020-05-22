package com.aperto.magkit.monitoring.endpoint.prometheus;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import info.magnolia.cms.filters.AbstractMgnlFilter;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.util.StringUtils;
import io.micrometer.prometheus.PrometheusMeterRegistry;

/**
 * Prometheus Filter class.
 * 
 * @author VladNacu
 *
 */
public class PrometheusFilter extends AbstractMgnlFilter {

    private final PrometheusMeterRegistry _registry;

    @Inject
    public PrometheusFilter(PrometheusMeterRegistry registry) {
        _registry = registry;

    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String httpPath = StringUtils.isNotEmpty(request.getRequestURI()) ? request.getRequestURI() : "";
        String httpMethod = StringUtils.isNotEmpty(request.getMethod()) ? request.getMethod() : "";
        String httpStatus = String.valueOf(response.getStatus());

        Counter httpRequestsTotal = Counter.builder("http_requests_count")
                .description("A counter of the total number of HTTP requests.")
                .tags("method", request.getMethod(), "status", httpStatus, "uri", httpPath).register(_registry);

        /*
         * DistributionSummary histogram =
         * DistributionSummary.builder("http_requests_duration_bucket") .tags("method",
         * request.getMethod(), "status", httpStatus, "uri",
         * httpPath).publishPercentileHistogram() .register(_registry);
         */

        /*
         * DistributionSummary histogramSla =
         * DistributionSummary.builder("http_requests_duration_bucket_ms")
         * .tags("method", request.getMethod(), "status", httpStatus, "uri", httpPath,
         * "le", "10") .sla(100, 500, 1000,
         * 10000).publishPercentileHistogram().register(_registry);
         */

        /*
         * DistributionSummary.builder("my.ratio").scale(100).sla(70, 80,
         * 90).register(_registry);
         */

        /*
         * Timer myTimer = (Timer) Timer.builder("my.timer").publishPercentiles(0.5,
         * 0.95).publishPercentileHistogram()
         * .sla(Duration.ofMillis(100)).minimumExpectedValue(Duration.ofMillis(1))
         * .maximumExpectedValue(Duration.ofSeconds(10)).register(_registry);
         */

        httpRequestsTotal.increment();
        chain.doFilter(request, response);

    }

}
