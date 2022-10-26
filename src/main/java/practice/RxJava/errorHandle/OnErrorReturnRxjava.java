package practice.RxJava.errorHandle;

import io.reactivex.rxjava3.core.Observable;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class OnErrorReturnRxjava {

    public static void main(String[] args) throws InterruptedException {
        Observable
                .just(5)
                .flatMap(num -> Observable.interval(200L, TimeUnit.MILLISECONDS)
                        .take(5)
                        .map(i -> num / i)
                        .onErrorReturn(ex -> {
                            if (ex instanceof ArithmeticException) {
                                log.info("error occurred {}", ex.getMessage());
                            }
                            return -1L;
                        })
                ).subscribe(
                        data -> {
                            if (data == -1) {
                                log.info("error value returned");
                            } else {
                                log.info("success");
                            }
                        },
                        error -> log.info("error occurred", error),
                        () -> log.info("complete")
                );

        Thread.sleep(5000L);
    }
}
