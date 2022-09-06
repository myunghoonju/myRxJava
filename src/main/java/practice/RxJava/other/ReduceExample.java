package practice.RxJava.other;

import io.reactivex.rxjava3.core.Observable;

public class ReduceExample {

    public static void main(String[] args) {
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .reduce((x, y) -> x + y)
                .subscribe(re -> System.out.println(re));

        System.out.println("--------------");

        Observable.just("A", "B", "C")
                .reduce((x, y) -> "(" + x + ", " + y + ")")
                .subscribe(re -> System.out.println(re));

        System.out.println("--------------");

        Observable.just("A", "B", "C")
                .scan((x, y) -> "(" + x + ", " + y + ")")
                .subscribe(re -> System.out.println(re));
    }
}