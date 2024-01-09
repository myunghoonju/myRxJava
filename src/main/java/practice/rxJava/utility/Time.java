package practice.rxJava.utility;

import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

public class Time {

    public static void main(String[] args) throws Exception {
        timeOutTwo();
        Thread.sleep(4000L);
    }

    static void timeoutOne() {
        Observable.range(1, 5)
                .map(num -> {
                    long time = 1000L;
                    if (num == 4) {
                        time = 1500L;
                    }
                    Thread.sleep(time);

                    return num;
                })
                .timeout(1200L, TimeUnit.MILLISECONDS)
                .subscribe(
                        data -> System.out.println("subscribe:: " + data),
                        error -> System.out.println("error:: " + error)
                );
    }

    static void timeOutTwo() {
        Observable.just(1, 3, 5, 7, 9)
                .delay(item -> {
                    Thread.sleep(1000L);
                    return Observable.just(item);
                })
                .timeInterval()
                .subscribe(
                        integerTimed -> System.out.println("time:: " + integerTimed.time() + " data:: " + integerTimed.value())
                );
    }

    public static void sleep(long milliseconds){
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
