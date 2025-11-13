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
package de.ibmix.magkit.monitoring.endpoint.prometheus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.micrometer.core.instrument.binder.MeterBinder;

/**
 * Unit tests for {@link Metric} enum covering lookup and binder instantiation.
 * Ensures case-insensitive id matching and reflective binder creation.
 * @author GitHub Copilot
 * @since 2025-11-13
 */
public class MetricTest {

    /**
     * Verifies getById matches case-insensitively and returns correct enum.
     */
    @Test
    public void testGetByIdCaseInsensitive() {
        assertTrue(Metric.getById("jVmMeMoRy").isPresent());
        assertEquals(Metric.JVM_MEMORY, Metric.getById("jVmMeMoRy").get());
        assertFalse(Metric.getById("doesNotExist").isPresent());
    }

    /**
     * Verifies getBinderInstance returns a new instance of the binder class.
     * @throws Exception on reflection errors
     */
    @Test
    public void testGetBinderInstance() throws Exception {
        MeterBinder binder1 = Metric.JVM_MEMORY.getBinderInstance();
        MeterBinder binder2 = Metric.JVM_MEMORY.getBinderInstance();
        assertNotNull(binder1);
        assertNotNull(binder2);
        assertEquals(binder1.getClass(), binder2.getClass());
    }
}

