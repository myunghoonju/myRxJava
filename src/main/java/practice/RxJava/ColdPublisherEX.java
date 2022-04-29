package practice.RxJava;

import io.reactivex.rxjava3.core.Flowable;

public class ColdPublisherEX {
    // Observable, Flowable
    public static void main(String[] args) {
        Flowable<Integer> obj = Flowable.just(1, 3, 5, 7);

        obj.subscribe(data -> System.out.println("subscriber1: " + data));
        obj.subscribe(data -> System.out.println("subscriber2: " + data));

    }
}
