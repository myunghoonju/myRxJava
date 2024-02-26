package practice.reactor;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class MonoEx {

    public static void main(String[] args) {
        one();
        two();
    }

    private static void one() {
        Mono.empty().subscribe(
                data -> log.info("emitted {}", data),
                err -> {},
                () -> log.info("completed - onComplete signal")
        );
    }

    private static void two() {
        Mono.just("data string").subscribe(
                data -> log.info("emitted {}", data),
                err -> {},
                () -> log.info("completed - onComplete signal")
        );
    }
}
