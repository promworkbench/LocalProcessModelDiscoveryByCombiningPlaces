package org.processmining.placebasedlpmdiscovery.utils;

import java.util.List;
import java.util.function.BiFunction;

public class GeneralUtils {

    public static <T> void quickSort(List<T> list, int begin, int end, BiFunction<T, T, Boolean> swapElementsFunction) {
        if (begin < end) {
            int partitionIndex = partition(list, begin, end, swapElementsFunction);

//            if (end == partitionIndex - 1 || begin == partitionIndex + 1) {
//                System.out.println("repeating");
//                System.out.println(list);
//            }

            quickSort(list, begin, partitionIndex - 1, swapElementsFunction);
            quickSort(list, partitionIndex + 1, end, swapElementsFunction);
        }
    }

    private static <T> int partition(List<T> list, int begin, int end, BiFunction<T, T, Boolean> swapElementsFunction) {
        T pivot = list.get(end);
        int i = begin - 1;

        for (int j = begin; j < end; j++) {
            if (!swapElementsFunction.apply(list.get(j), pivot)) {
                i++;

                T temp = list.get(j);
                list.set(j, list.get(i));
                list.set(i, temp);
            }
        }
        i++;
        T temp = list.get(end);
        list.set(end, list.get(i));
        list.set(i, temp);
        return i;
    }
}