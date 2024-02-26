package practice.reactor;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class FluxEx {

    public static void main(String[] args) {
        one();
        two();
        three();
        four();
    }

    private static void one() {
        Flux.just(6, 12, 31)
            .map(n -> n % 2)
            .subscribe(
                    remainder -> log.info("emitted {}", remainder),
                    err -> {
                    },
                    () -> log.info("completed - onComplete signal")
            );
    }

    private static void two() {
        Flux.fromArray(new Integer[] { 3, 6, 7, 9 })
            .filter(num -> num > 6)
            .map(n -> n * 2)
            .subscribe(mul -> log.info("num {}", mul));
    }

    private static void three() {
        Flux<Object> flux = Mono.justOrEmpty(null).concatWith(Mono.just("Job"));
        flux.subscribe(d -> log.info("result {}", d));
    }

    private static void four() {
        Flux.concat(
                    Flux.just("one", "two"),
                    Flux.just("three"),
                    Flux.just("four"))
            .collectList().subscribe(l -> log.info("l - {}", l));
    }
}
