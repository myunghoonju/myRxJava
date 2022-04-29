package practice.RxJava;

import io.reactivex.rxjava3.core.Observable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HelloRxJava {

    public static void main(String[] args) {
        Observable<String> obs = Observable.just("HELLO", "RxJava");
        obs.subscribe(data -> log.info("print data = {}", data));
    }
}