package de.cupofjava.bitonicsort;

import java.util.Arrays;

/**
 * This class implements the bitonic sort algorithm. The main idea and most code was
 * taken from: <a href="http://www.iti.fh-flensburg.de/lang/algorithmen/sortieren/bitonic/oddn.htm">FH Flensburg - Bitonic Sort</a>.
 *
 * @author Felix Müller
 */
public class BitonicSorter {

    private int[] sortedArray;

    /**
     * Sorts a given array of integers by using the bitonic sort algorithm.
     * @param array the array to sort
     * @return the sorted array
     */
    public int[] sort(int[] array) {
        sortedArray = Arrays.copyOf(array, array.length);
        bitonicSort(0, sortedArray.length, true);
        return sortedArray;
    }

    private void bitonicSort(int lo, int n, boolean isAscending) {
        if (n > 1) {
            int m = n / 2;
            bitonicSort(lo, m, !isAscending);
            bitonicSort(lo + m, n - m, isAscending);
            bitonicMerge(lo, n, isAscending);
        }
    }

    private void bitonicMerge(int lo, int n, boolean dir) {
        if (n > 1) {
            int m = greatestPowerOfTwoLessThan(n);
            for (int i = lo; i < lo + n - m; i++) {
                compare(i, i + m, dir);
            }
            bitonicMerge(lo, m, dir);
            bitonicMerge(lo + m, n - m, dir);
        }
    }

    private void compare(int i, int j, boolean isSortingDirection) {
        if (isSortingDirection == (sortedArray[i] > sortedArray[j]))
            exchange(i, j);
    }

    private void exchange(int i, int j) {
        final int t = sortedArray[i];
        sortedArray[i] = sortedArray[j];
        sortedArray[j] = t;
    }

    private int greatestPowerOfTwoLessThan(int n) {
        int k = 1;
        while (k < n)
            k = k << 1;
        return k >> 1;
    }
}
