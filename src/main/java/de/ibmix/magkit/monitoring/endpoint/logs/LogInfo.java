package de.ibmix.magkit.monitoring.endpoint.logs;

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
 * LogInfo POJO.
 *
 * @author Dan Olaru (IBM)
 * @since 2020-04-09
 *
 */

public class LogInfo {

    private String _name = "";

    private String _path = "";

    public LogInfo(String logName, String logPath) {
        super();
        _name = logName;
        _path = logPath;
    }

    public String getName() {
        return _name;
    }

    public void setName(String logName) {
        _name = logName;
    }

    public String getPath() {
        return _path;
    }

    public void setPath(String logPath) {
        _path = logPath;
    }

}
