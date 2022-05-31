package practice.RxJava.operator;

import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

/**
 *
 * polling 용도로 사용
 *
 * */
public class ObservableInterval {

    public static void main(String[] args) throws Exception {
        Observable.interval(0L, 1000L, TimeUnit.MILLISECONDS)
                .map(num -> num + "count")
                .subscribe(data -> System.out.println("-> " + data));

        Thread.sleep(3000L);
    }
}
