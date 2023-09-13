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
 *
 * This pojo is used to provide information about the recurring jobs configured
 * in Magnolia.
 *
 * @author MIHAELA PAPARETE (IBM)
 * @since 2020-04-23
 *
 */
public class JobInfo {

    private String _name;
    private String _description;
    private String _cron;
    private String _nextExecution;

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        _description = description;
    }

    public String getCron() {
        return _cron;
    }

    public void setCron(String cron) {
        _cron = cron;
    }

    public String getNextExecution() {
        return _nextExecution;
    }

    public void setNextExecution(String nextExecution) {
        _nextExecution = nextExecution;
    }
}
