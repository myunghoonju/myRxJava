package practice.RxJava.errorHandle;

import io.reactivex.rxjava3.core.Observable;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class OnErrorRxjava {

    public static void main(String[] args) throws InterruptedException {
        Observable.just(5).flatMap(
                num -> Observable.interval(200L, TimeUnit.MILLISECONDS)
                        .doOnNext(data -> log.info("data = {}", data))
                        .take(5)
                        .map(i -> num / i)
        ).subscribe(
                data -> log.info("data = {}", data),
                error -> log.info("error occurred", error),
                () -> log.info("complete")
        );

        Thread.sleep(5000L);
    }
}
