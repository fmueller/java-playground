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
