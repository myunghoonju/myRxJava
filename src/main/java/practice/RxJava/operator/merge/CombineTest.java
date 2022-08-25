package practice.RxJava.operator.merge;

import io.reactivex.rxjava3.core.Observable;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class CombineTest {

    public static void main(String[] args) throws Exception {
        combineLatestTest();
    }

    static void mergeTest() throws Exception {
        Observable<Long> obs1 = Observable.interval(200L, TimeUnit.MILLISECONDS)
                .take(5);
        Observable<Long> obs2 = Observable.interval(400L, TimeUnit.MILLISECONDS)
                .take(5)
                .map(number -> number + 1000);

        Observable.merge(obs1, obs2)
                .subscribe(data -> log.info("data {}", data));

        Thread.sleep(4000);
    }

    static void concatTest() throws Exception {
        Observable<Long> obs1 = Observable.interval(500L, TimeUnit.MILLISECONDS)
                .take(4);
        Observable<Long> obs2 = Observable.interval(300L, TimeUnit.MILLISECONDS)
                .take(5)
                .map(number -> number + 1000);

        Observable.concat(obs1, obs2)
                .subscribe(data -> log.info("data {}", data));

        Thread.sleep(3500L);
    }

    static void zipTest() throws Exception {
        Observable<Long> obs1 = Observable.interval(200L, TimeUnit.MILLISECONDS)
                .take(4);
        Observable<Long> obs2 = Observable.interval(400L, TimeUnit.MILLISECONDS)
                .take(6);

        Observable.zip(obs1, obs2, (ob1, ob2) -> ob1 + ob2)
                .subscribe(data -> log.info("data:: {}", data));

        Thread.sleep(3500L);
    }

    static void combineLatestTest() throws Exception {
        Observable<Long> obs1 = Observable.interval(500L, TimeUnit.MILLISECONDS)
                .take(4);
        Observable<Long> obs2 = Observable.interval(700L, TimeUnit.MILLISECONDS)
                .take(4);

        Observable.combineLatest(obs1, obs2, (ob1, ob2) -> "obs1: " + ob1 + "\tobs2: " + ob2)
                        .subscribe(data -> log.info("data:: {}", data));

        Thread.sleep(3500L);
    }
}
