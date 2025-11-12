package de.ibmix.magkit.monitoring.endpoint.prometheus;

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

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.ibmix.magkit.monitoring.MonitoringModule;

import io.micrometer.core.instrument.Meter.Id;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;

/**
 * Provider creating and configuring a singleton {@link PrometheusMeterRegistry} for the monitoring module.
 * Applies URI whitelist filtering and SLO histogram configuration, then binds selected metric binders.
 * <p><strong>Purpose</strong></p>
 * Centralizes registry instantiation to ensure consistent filters and bound meters across the application.
 * <p><strong>Main Functionality</strong></p>
 * Constructs registry with default Prometheus configuration, applies HTTP URI whitelist filter, applies request
 * latency histogram SLO buckets and registers configured meter binders based on module configuration.
 * <p><strong>Key Features</strong></p>
 * <ul>
 * <li>HTTP URI whitelist to limit cardinality.</li>
 * <li>Service level objective (SLO) latency buckets for request timing.</li>
 * <li>Dynamic metric binder selection from module configuration.</li>
 * </ul>
 * <p><strong>Usage Preconditions</strong></p>
 * Monitoring module must provide a non-null Prometheus configuration with optional HTTP metrics config; metric ids must map to {@link Metric} enum constants for successful binding.
 * <p><strong>Null and Error Handling</strong></p>
 * Missing or invalid metric ids are logged and skipped. Reflection errors during binder instantiation are logged.
 * <p><strong>Thread-Safety</strong></p>
 * Provider is stateless aside from injected module reference; registry returned is thread-safe for concurrent metric operations.
 * <p><strong>Usage Example</strong></p>
 * <pre>{@code
 * PrometheusMeterRegistry registry = provider.get();
 * }</pre>
 * @author Soenke Schmidt - IBM iX
 * @since 2023-01-03
 */
public class PrometheusMeterRegistryProvider implements Provider<PrometheusMeterRegistry> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrometheusMeterRegistryProvider.class);

    private final MonitoringModule _monitoringModule;

    /**
     * Constructs provider with monitoring module dependency.
     * @param monitoringModule monitoring module providing configuration
     */
    @Inject
    public PrometheusMeterRegistryProvider(MonitoringModule monitoringModule) {
        _monitoringModule = monitoringModule;
    }

    /**
     * Supplies configured Prometheus meter registry instance.
     * @return configured registry
     */
    @Override
    public PrometheusMeterRegistry get() {
        PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        registry.config().meterFilter(httpWhitelistUriFilter());
        registry.config().meterFilter(requestHistogramFilter());

        bindMetrics(registry);

        return registry;
    }

    /**
     * Returns monitoring module Prometheus configuration.
     * @return config bean
     */
    protected de.ibmix.magkit.monitoring.config.prometheus.PrometheusConfig config() {
        return _monitoringModule.getPrometheusConfig();
    }

    /**
     * Binds configured metric binders to the registry.
     * @param registry target registry
     */
    protected void bindMetrics(MeterRegistry registry) {
        List<String> metricIds = getMetricIds();
        for (String metricId : metricIds) {
            Optional<Metric> metric = Metric.getById(metricId);
            metric.ifPresent((m) -> {
                try {
                    m.getBinderInstance().bindTo(registry);
                } catch (Exception e) {
                    LOGGER.error("Could not register Metrics '{}' to Prometheus Registry!", metric.map(Metric::name).orElse("UNKNOWN"), e);
                }
            });
        }
    }

    /**
     * Meter filter that whitelists configured HTTP request URIs, blanking others to reduce cardinality.
     * @return meter filter
     */
    protected MeterFilter httpWhitelistUriFilter() {
        return new MeterFilter() {
            @Override
            public Id map(Id id) {
                if (id.getName().equals("http_server_requests")) {
                    List<String> whitelistUris = getWhitelistUris();
                    String uri = id.getTag("uri");
                    if (whitelistUris.stream().noneMatch((uriPattern) -> uri.matches(uriPattern))) {
                        List<Tag> tags = id.getTags().stream().filter((t) -> !"uri".equals(t.getKey())).collect(Collectors.toList());
                        tags.add(Tag.of("uri", ""));
                        return id.replaceTags(tags);
                    }
                }
                return id;
            }
        };
    }

    /**
     * Meter filter applying SLO histogram buckets to HTTP request timers.
     * @return meter filter
     */
    protected MeterFilter requestHistogramFilter() {
        return new MeterFilter() {
            @Override
            public DistributionStatisticConfig configure(Id id, DistributionStatisticConfig config) {
                List<Integer> sloBuckets = getSloBuckets();
                if (id.getName().equals("http_server_requests") && !sloBuckets.isEmpty()) {
                    return DistributionStatisticConfig.builder()
                        .percentilesHistogram(false)
                        .serviceLevelObjectives(
                            sloBuckets.stream()
                                .map(b -> Duration.ofMillis(b).toNanos())
                                .mapToDouble(x -> x.doubleValue())
                                .toArray())
                        .build()
                        .merge(config);
                }
                return config;
            }
        };
    }

    /**
     * Returns configured metric identifier list.
     * @return list of metric ids
     */
    protected List<String> getMetricIds() {
        return _monitoringModule.getPrometheusConfig().getMetrics();
    }

    /**
     * Returns whitelist URI regex patterns.
     * @return list of URI patterns
     */
    protected List<String> getWhitelistUris() {
        return _monitoringModule.getPrometheusConfig().getHttpRequestMetricsConfig().getUris();
    }

    /**
     * Returns HTTP request SLO bucket boundaries in milliseconds.
     * @return list of SLO bucket ms values
     */
    protected List<Integer> getSloBuckets() {
        return _monitoringModule.getPrometheusConfig().getHttpRequestMetricsConfig().getSloBuckets();
    }

}
