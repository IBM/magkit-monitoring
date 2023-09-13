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
 *
 * Configuration bean for http metrics provided by the prometheus scraper.
 *
 * @author Soenke Schmidt - IBM iX
 * @since 04.01.2023
 *
 */
public class HttpRequestMetricsConfig {

    private List<Integer> _sloBuckets = emptyList();
    private List<String> _uris = emptyList();

    public List<Integer> getSloBuckets() {
        return _sloBuckets;
    }

    public void setSloBuckets(List<Integer> sloBuckets) {
        _sloBuckets = sloBuckets;
    }

    public List<String> getUris() {
        return _uris;
    }

    public void setUris(List<String> uris) {
        _uris = uris;
    }

    @Inject(optional = true)
    protected void provideUrisFromProps(@Named("magnolia.monitoring.prometheus.http.uris") String urisProp) {
        _uris = Arrays.asList(urisProp.split(","));
    }

    @Inject(optional = true)
    protected void provideSloBucketsFromProps(@Named("magnolia.monitoring.prometheus.http.slo") String sloProps) {
        _sloBuckets = Arrays.stream(sloProps.split(",")).map((s) -> Integer.valueOf(s)).collect(Collectors.toList());
    }
}
