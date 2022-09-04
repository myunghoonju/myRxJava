 package practice.RxJava.other;

import io.reactivex.rxjava3.core.Observable;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class OnErrorResumeNextRxjava {

    public static void main(String[] args) throws InterruptedException {
        Observable.just(5).flatMap(
                num -> Observable.interval(200L, TimeUnit.MILLISECONDS)
                        .doOnNext(data -> log.info("data = {}", data))
                        .take(5)
                        .map(i -> num / i)
                        .onErrorResumeNext(ex -> {
                           log.info("notify manager about the error", ex);
                            return Observable.interval(200L, TimeUnit.MILLISECONDS)
                                    .take(5)
                                    .skip(1)
                                    .map(i -> num / i);
                        })
        ).subscribe(
                data -> log.info("data = {}", data),
                error -> log.info("error occurred", error),
                () -> log.info("complete")
        );

        Thread.sleep(5000L);
    }
}
