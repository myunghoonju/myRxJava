package practice.basic;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FreeNote {

    public static void main(String[] args) {
        reverseOrder();
    }

    private static void reverseOrder() {
        List<Integer> list = List.of(1, 2, 3, 4, 5);
        List<Integer> reversed = list.stream()
                                     .collect(Collectors.collectingAndThen(
                                                                             Collectors.toList(),
                                                                             l -> {
                                                                                     Collections.reverse(l);
                                                                                     return l;
                                                                                   }));

        reversed.forEach(numbers -> System.err.print(numbers + " "));
    }
}
