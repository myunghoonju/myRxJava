package practice.RxJava.doFunction;

import io.reactivex.rxjava3.core.Observable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class doOnNextExample {

    public static void main(String[] args) {
        Observable.just(1, 3, 5, 7, 9, 10, 11, 12, 13)
                .doOnNext(data -> log.info("doOnNext, origin data {}", data))
                .filter(data -> data < 10)
                .doOnNext(data -> log.info("filtered data {}", data))
                .map(data -> "## filtered :: " + data)
                .doOnNext(data -> log.info("after mapped {}", data))
                .subscribe(data -> log.info("onNext, output {}", data));
    }
}
