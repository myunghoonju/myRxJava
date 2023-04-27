package practice.rxJava.doFunction;

import io.reactivex.rxjava3.core.Observable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class doOnCompleteExample {

    public static void main(String[] args) {
        Observable.range(1, 5)
                .doOnComplete(() -> log.info("doOnComplete, data published"))
                .subscribe(
                        data -> log.info("onNext: {}", data),
                        error -> log.info("onError {}", error),
                        () -> log.info("onComplete")
                );
    }
}
