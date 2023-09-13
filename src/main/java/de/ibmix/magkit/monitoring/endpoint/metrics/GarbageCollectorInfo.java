package de.ibmix.magkit.monitoring.endpoint.metrics;

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
 * The pojo is used to provide Garbage Collector information for the JVM
 * runtime.
 *
 * @author MIHAELA PAPARETE (IBM)
 * @since 2020-04-08
 *
 */
public class GarbageCollectorInfo {

    private String _name;
    private String[] _memoryPools;
    private long _collectionCount;
    private long _collectionTime;

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public String[] getMemoryPools() {
        return _memoryPools;
    }

    public void setMemoryPools(String[] memoryPools) {
        _memoryPools = memoryPools;
    }

    public long getCollectionCount() {
        return _collectionCount;
    }

    public void setCollectionCount(long collectionCount) {
        _collectionCount = collectionCount;
    }

    public long getCollectionTime() {
        return _collectionTime;
    }

    public void setCollectionTime(long collectionTime) {
        _collectionTime = collectionTime;
    }
}
