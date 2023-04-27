package practice.rxJava.doFunction;


import io.reactivex.rxjava3.core.Observable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class doSubscribeExample {

    public static void main(String[] args) {
        Observable.just(1, 2, 3, 4, 5, 6, 7)
                .doOnSubscribe(disposable -> log.info("doOnSubscribe:: ready to subscribe"))
                .subscribe(
                        data -> log.info("onNext {}", data),
                        error -> log.info("onError {}", error),
                        () -> log.info("onComplete")
                );

    }
}
