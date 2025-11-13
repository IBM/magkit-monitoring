package de.ibmix.magkit.monitoring.config.prometheus;

/*-
 * #%L
 * IBM iX Magnolia Monitoring
 * %%
 * Copyright (C) 2023 IBM iX
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import static java.util.Collections.emptyList;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Named;

import com.google.inject.Inject;

/**
 * Configuration bean aggregating HTTP request related metric settings used by the Prometheus scraper.
 * It exposes URI patterns to be monitored and Service-Level Objective (SLO) latency buckets for histogram metrics.
 * <p><strong>Purpose</strong></p>
 * Provides externally configurable lists (URIs and latency bucket boundaries) that guide which HTTP requests are
 * measured and how response times are bucketed when exported to Prometheus.
 * <p><strong>Main Functionality</strong></p>
 * Populates internal lists from injected property values, exposes them via getters for registry filter configuration and histogram SLO setup.
 * <p><strong>Configuration Properties</strong></p>
 * <ul>
 * <li><code>magnolia.monitoring.prometheus.http.uris</code> - comma separated list of request URI patterns to include.</li>
 * <li><code>magnolia.monitoring.prometheus.http.slo</code> - comma separated list of integer latency boundaries (ms) used as histogram buckets.</li>
 * </ul>
 * <p><strong>Usage Preconditions</strong></p>
 * Property values must be present and non-null when injection methods are invoked; parsing assumes valid comma separated integers for SLO buckets.
 * <p><strong>Null and Error Handling</strong></p>
 * Lists default to empty when not configured. No internal null checks are applied on setters; passing null may lead to downstream NullPointerException.
 * <p><strong>Thread-Safety</strong></p>
 * Not thread-safe by itself; returned lists are mutable references. If concurrent modification is required, synchronize externally or provide immutable copies.
 * <p><strong>Usage Example</strong></p>
 * <pre>{@code
 * List<Integer> buckets = cfg.getSloBuckets();
 * }</pre>
 * <p><strong>Important Details</strong></p>
 * URI patterns are treated as literal matches in filter logic; regex usage should conform to Java Pattern semantics if employed.
 * @author Soenke Schmidt - IBM iX
 * @since 2023-01-04
 */
public class HttpRequestMetricsConfig {

    private List<Integer> _sloBuckets = emptyList();
    private List<String> _uris = emptyList();

    /**
     * Returns configured SLO response time bucket boundaries in milliseconds used for latency histograms.
     * @return list of bucket boundaries in milliseconds; never null but possibly empty
     */
    public List<Integer> getSloBuckets() {
        return _sloBuckets;
    }

    /**
     * Replaces the configured SLO response time bucket boundaries.
     * @param sloBuckets list of millisecond boundaries; must be non-null for safe downstream usage
     */
    public void setSloBuckets(List<Integer> sloBuckets) {
        _sloBuckets = sloBuckets;
    }

    /**
     * Returns configured HTTP request URI patterns to monitor.
     * @return list of URI patterns; never null but possibly empty
     */
    public List<String> getUris() {
        return _uris;
    }

    /**
     * Replaces the configured HTTP request URI patterns to monitor.
     * @param uris list of URI patterns; must be non-null for safe downstream usage
     */
    public void setUris(List<String> uris) {
        _uris = uris;
    }

    /**
     * Injection hook populating URI patterns from a comma separated property value.
     * @param urisProp comma separated URI patterns; must not be null
     */
    @Inject(optional = true)
    protected void provideUrisFromProps(@Named("magnolia.monitoring.prometheus.http.uris") String urisProp) {
        _uris = Arrays.asList(urisProp.split(","));
    }

    /**
     * Injection hook populating SLO latency buckets from a comma separated property value.
     * @param sloProps comma separated integer latency boundaries in milliseconds; must not be null and must contain valid integers
     */
    @Inject(optional = true)
    protected void provideSloBucketsFromProps(@Named("magnolia.monitoring.prometheus.http.slo") String sloProps) {
        _sloBuckets = Arrays.stream(sloProps.split(",")).map((s) -> Integer.valueOf(s)).collect(Collectors.toList());
    }
}
