package practice.rxJava.backpressure;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class MissingBackPressureException {

    public static void main(String[] args) throws InterruptedException {
        Flowable.interval(1L, TimeUnit.MILLISECONDS)
                .doOnNext(data -> log.info("data doOnNext {}", data))
                .observeOn(Schedulers.computation())// data consume
                .subscribe(data -> {
                    log.info("waint til consume");
                    Thread.sleep(1000L); // interval * 1000 :: very lazy...
                    log.info("process data :: data {}", data);
                },
                error -> {
                    log.info("error {]", error);

                },
                        () -> {
                            log.info("complete");
                        }
                );
        Thread.sleep(2000L);
    }
}
