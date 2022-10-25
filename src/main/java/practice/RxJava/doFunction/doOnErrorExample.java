package practice.RxJava.doFunction;

import io.reactivex.rxjava3.core.Observable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class doOnErrorExample {

    public static void main(String[] args) {
        Observable.just(3, 6, 9, 12, 15, 20)
                .zipWith(Observable.just(1, 2, 3, 4, 0, 5), (just, zipWith) -> just / zipWith)
                .doOnError(error -> log.info("doOnError message: {}", error.getMessage()))
                .subscribe(
                        data -> log.info("onNext {}", data),
                        error -> log.info("onError {}", error.getMessage())
                );
    }
}
