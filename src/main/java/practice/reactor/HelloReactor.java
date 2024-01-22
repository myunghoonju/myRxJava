package practice.reactor;

import lombok.extern.slf4j.Slf4j;
import practice.rxJava.utility.Log;
import reactor.core.publisher.Flux;

@Slf4j
public class HelloReactor {

    public static void main(String[] args) {
        Flux<String> seq = Flux.just("hello", "reactor"); // emit data
        seq.map(String::toUpperCase) // modify
           .subscribe(Log::onNext); // send to subscriber
    }
}
