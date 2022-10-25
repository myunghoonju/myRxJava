package practice.RxJava.doFunction;

import io.reactivex.rxjava3.core.Observable;
import lombok.extern.slf4j.Slf4j;

//mix of doOnNext, doOnComplete, doOnError
@Slf4j
public class doOnEachExample {

    public static void main(String[] args) {
        Observable.range(1, 5)
                .doOnEach(noti -> {
                    if (noti.isOnNext()) {
                        log.info("doOnNext, publish data {}", noti.getValue());
                    }
                    if (noti.isOnError()) {
                        log.info("doOnError {}", noti.getError().getMessage());
                    }
                    if (noti.isOnComplete()) {
                        log.info("doOnComplete");
                    }
                })
                .subscribe(
                  data -> log.info("onNext {}", data),
                  error -> log.info("onError {}", error.getMessage()),
                  () -> log.info("onComplete")
                );
    }
}
