package practice.RxJava.operator.conditional;

import io.reactivex.rxjava3.core.Observable;

public class defaultIfEmptyExample {

    public static void main(String[] args) {
        Observable.just(1, 2, 3, 4, 5)
                .filter(num -> num > 10)
                .defaultIfEmpty(10)
                .subscribe(data -> System.out.println(data));
    }
}
