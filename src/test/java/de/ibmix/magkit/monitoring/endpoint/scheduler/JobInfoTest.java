/*-
 * #%L
 * IBM iX Magnolia Monitoring
 * %%
 * Copyright (C) 2025 IBM iX
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
package de.ibmix.magkit.monitoring.endpoint.scheduler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link JobInfo} verifying bean-style getters and setters.
 * @author GitHub Copilot
 * @since 2025-11-13
 */
public class JobInfoTest {

    /**
     * Verifies getters return values set by setters.
     */
    @Test
    public void testGettersAndSetters() {
        JobInfo info = new JobInfo();
        assertNull(info.getName());
        assertNull(info.getDescription());
        assertNull(info.getCron());
        assertNull(info.getNextExecution());
        info.setName("n");
        info.setDescription("d");
        info.setCron("c");
        info.setNextExecution("e");
        assertEquals("n", info.getName());
        assertEquals("d", info.getDescription());
        assertEquals("c", info.getCron());
        assertEquals("e", info.getNextExecution());
    }
}
