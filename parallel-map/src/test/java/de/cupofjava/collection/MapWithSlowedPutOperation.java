/*
 * Copyright 2011 Felix Müller <felix.mueller.berlin@googlemail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cupofjava.collection;

import java.util.HashMap;
import java.util.Map;

public class MapWithSlowedPutOperation<K, V> extends HashMap<K, V> {

    private static final long serialVersionUID = 2053940291870020777L;

    private final long waitForStartWriting;

    private boolean isInitialization = true;

    public MapWithSlowedPutOperation(Map<K, V> map, long waitForStartWriting) {
        this.waitForStartWriting = waitForStartWriting;
        putAll(map);
        isInitialization = false;
    }

    @Override
    public V put(K key, V value) {
        if (!isInitialization) {
            try {
                Thread.sleep(waitForStartWriting);
            } catch (InterruptedException ie) {
                throw new RuntimeException(
                        "waiting for start write operation was interrupted", ie);
            }
        }

        return super.put(key, value);
    }
}