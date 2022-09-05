package practice.RxJava.other.utility;

import io.reactivex.rxjava3.core.Observable;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class DelayUtil {

    public static void main(String[] args) throws InterruptedException {
        delayThree();
        Thread.sleep(12500L);
    }

    static void delayOne() {
        Observable.just(1, 3, 4, 6)
                .doOnNext(data -> System.out.println(data))
                .delay(10000L, TimeUnit.MILLISECONDS)
                .subscribe(data -> System.out.println(data));

    }

    static void delayTwo() {
        Observable.just(1, 3, 4, 6)
                .delay(item -> {
                    Thread.sleep(2000L);
                    return Observable.just(item);
                })
                .subscribe(data -> System.out.println(data));

    }

    static void delayThree() {
        Observable.just(1, 3, 4, 6)
                .doOnNext(data -> System.out.println("doOnNext:: " + data))
                .delaySubscription(2000L, TimeUnit.MILLISECONDS)
                .subscribe(data -> System.out.println("subscribe:: " + data));

    }
}
