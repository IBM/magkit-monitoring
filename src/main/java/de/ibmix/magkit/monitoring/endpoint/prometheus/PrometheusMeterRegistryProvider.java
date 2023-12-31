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
 * Custom Provider implementation for {@link PrometheusMeterRegistry}.
 *
 * @author Soenke Schmidt - IBM iX
 * @since 03.01.2023
 *
 */
public class PrometheusMeterRegistryProvider implements Provider<PrometheusMeterRegistry> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrometheusMeterRegistryProvider.class);

    private final MonitoringModule _monitoringModule;

    @Inject
    public PrometheusMeterRegistryProvider(MonitoringModule monitoringModule) {
        _monitoringModule = monitoringModule;
    }

    @Override
    public PrometheusMeterRegistry get() {
        PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        registry.config().meterFilter(httpWhitelistUriFilter());
        registry.config().meterFilter(requestHistogramFilter());

        bindMetrics(registry);

        return registry;
    }

    protected de.ibmix.magkit.monitoring.config.prometheus.PrometheusConfig config() {
        return _monitoringModule.getPrometheusConfig();
    }

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

    protected List<String> getMetricIds() {
        return _monitoringModule.getPrometheusConfig().getMetrics();
    }

    protected List<String> getWhitelistUris() {
        return _monitoringModule.getPrometheusConfig().getHttpRequestMetricsConfig().getUris();
    }

    protected List<Integer> getSloBuckets() {
        return _monitoringModule.getPrometheusConfig().getHttpRequestMetricsConfig().getSloBuckets();
    }

}
