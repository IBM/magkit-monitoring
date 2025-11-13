/*-
 * #%L
 * IBM iX Magnolia Monitoring
 * %%
 * Copyright (C) 2023 - 2025 IBM iX
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
package de.ibmix.magkit.monitoring.endpoint.health;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link Health} covering default initialization and setter behavior including null assignment.
 * Verifies default status value "UP" and that setters replace the value including acceptance of null references.
 * @author GitHub Copilot
 * @since 2025-11-12
 */
public class HealthTest {

    /**
     * Verifies default status is "UP" upon construction.
     */
    @Test
    public void testDefaultStatus() {
        Health health = new Health();
        assertNotNull(health.getStatus());
        assertEquals("UP", health.getStatus());
    }

    /**
     * Verifies setStatus updates the status value to a non-default value.
     */
    @Test
    public void testSetStatus() {
        Health health = new Health();
        health.setStatus("DOWN");
        assertEquals("DOWN", health.getStatus());
    }

    /**
     * Verifies setStatus accepts null and getter returns null afterwards.
     */
    @Test
    public void testSetNullStatus() {
        Health health = new Health();
        health.setStatus(null);
        assertNull(health.getStatus());
    }
}
