package de.ibmix.magkit.monitoring.endpoint.scheduler;

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
 * Value object holding descriptive data for an enabled Magnolia scheduled job.
 * <p><strong>Purpose</strong></p>
 * Serves as a simple DTO for transporting scheduler job metadata to REST clients.
 * <p><strong>Main Functionality</strong></p>
 * Provides standard bean-style getters/setters for name, description, cron expression and formatted next execution timestamp enabling JSON serialization.
 * <p><strong>Key Features</strong></p>
 * <ul>
 * <li>Simple JavaBean with standard accessor methods.</li>
 * <li>Fields nullable to allow partial population.</li>
 * </ul>
 * <p><strong>Null and Error Handling</strong></p>
 * All properties are nullable; setters perform direct assignment without validation. The producing service ensures values are populated.
 * <p><strong>Side Effects</strong></p>
 * None – pure data container.
 * <p><strong>Thread-Safety</strong></p>
 * Not thread-safe for concurrent mutations; intended for per-request creation and read-only usage after population.
 * <p><strong>Important Details</strong></p>
 * The next execution timestamp formatting is performed by the service; clients should not infer timezone adjustments beyond what the service provides.
 *
 * @author MIHAELA PAPARETE (IBM)
 * @since 2020-04-23
 */
public class JobInfo {

    private String _name;
    private String _description;
    private String _cron;
    private String _nextExecution;

    /**
     * <p>Returns the job name.</p>
     *
     * @return job name or null if not set.
     */
    public String getName() {
        return _name;
    }

    /**
     * <p>Sets the job name.</p>
     *
     * @param name descriptive unique job name.
     */
    public void setName(String name) {
        _name = name;
    }

    /**
     * <p>Returns the job description.</p>
     *
     * @return textual job description or null.
     */
    public String getDescription() {
        return _description;
    }

    /**
     * <p>Sets the job description.</p>
     *
     * @param description human readable job description.
     */
    public void setDescription(String description) {
        _description = description;
    }

    /**
     * <p>Returns the cron expression defining execution schedule.</p>
     *
     * @return cron expression or null.
     */
    public String getCron() {
        return _cron;
    }

    /**
     * <p>Sets the cron expression.</p>
     *
     * @param cron valid Quartz-compatible cron expression.
     */
    public void setCron(String cron) {
        _cron = cron;
    }

    /**
     * <p>Returns the formatted next execution timestamp.</p>
     *
     * @return ISO-like timestamp string or null.
     */
    public String getNextExecution() {
        return _nextExecution;
    }

    /**
     * <p>Sets the next execution timestamp.</p>
     *
     * @param nextExecution formatted timestamp of next scheduled run.
     */
    public void setNextExecution(String nextExecution) {
        _nextExecution = nextExecution;
    }
}
