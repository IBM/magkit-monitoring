package de.ibmix.magkit.monitoring.endpoint.info.pojo;

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

/**
 * Root container aggregating Magnolia and environment information objects for the info endpoint response.
 * <p><strong>Key Features</strong></p>
 * <ul>
 * <li>Holds {@link Magnolia} metadata.</li>
 * <li>Holds {@link Environment} metadata.</li>
 * </ul>
 * <p><strong>Null and Error Handling</strong></p>
 * Fields may remain null until explicitly set; JSON serializers may omit or include nulls depending on configuration.
 * <p><strong>Thread-Safety</strong></p>
 * Not thread-safe; build per request.
 * <p><strong>Usage Example</strong></p>
 * <pre>{@code
 * Info info = new Info();
 * info.setMagnolia(new Magnolia());
 * }</pre>
 * @author CLAUDIU GONCIULEA (IBM iX)
 * @since 2020-04-09
 */
public class Info {

    private Magnolia _magnolia;
    private Environment _environment;

    /**
     * Returns Magnolia metadata.
     * @return magnolia descriptor; may be null
     */
    public Magnolia getMagnolia() {
        return _magnolia;
    }

    /**
     * Sets Magnolia metadata.
     * @param magnolia magnolia descriptor; may be null
     */
    public void setMagnolia(Magnolia magnolia) {
        _magnolia = magnolia;
    }

    /**
     * Returns environment descriptor.
     * @return environment descriptor; may be null
     */
    public Environment getEnvironment() {
        return _environment;
    }

    /**
     * Sets environment descriptor.
     * @param environment environment descriptor; may be null
     */
    public void setEnvironment(Environment environment) {
        _environment = environment;
    }
}
