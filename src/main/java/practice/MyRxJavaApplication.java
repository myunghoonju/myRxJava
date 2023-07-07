package practice;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class MyRxJavaApplication {

    public static void main(String[] args) {
        Mono.just("MyRxJavaApplication")
            .subscribe(msg -> log.info("Result: {}", msg));
    }
}
