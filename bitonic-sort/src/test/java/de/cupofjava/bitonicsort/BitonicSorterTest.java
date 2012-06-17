/*
 * Copyright 2012 Felix Müller <felix.mueller.berlin@googlemail.com>
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
package de.cupofjava.bitonicsort;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

public class BitonicSorterTest {

    private BitonicSorter sorter;

    @Before
    public void initializeSorter() {
        sorter = new BitonicSorter();
    }

    @Test
    public void notFailForEmptyArray() {
        assertThat(sorter.sort(new int[] {})).isEmpty();
    }

    @Test
    public void sortArrayWithOneElement() {
        assertThat(sorter.sort(new int[] { 0 })).isEqualTo(new int[] { 0 });
    }

    @Test
    public void sortArrayWithTwoElement() {
        assertThat(sorter.sort(new int[] { 34, 1 })).isEqualTo(new int[] { 1, 34 });
    }

    @Test
    public void sortArrayWithOddNumberOfElements() {
        assertThat(sorter.sort(new int[] { 34, 1, 5, 4, 65 }))
            .isEqualTo(new int[] { 1, 4, 5, 34, 65 });
    }

    @Test
    public void sortArrayWithManyElements() {
        assertThat(sorter.sort(new int[] { 201, 13, 12, 186, 34, 1, 5, 4, 65 }))
            .isEqualTo(new int[] { 1, 4, 5, 12, 13, 34, 65, 186, 201 });
    }
}
