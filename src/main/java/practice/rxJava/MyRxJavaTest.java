package practice.rxJava;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class MyRxJavaTest {

    public static void main(String[] args) {
/*        monoJust();
        monoEmpty();
        concatWith();*/
        concat();
    }

    public static void monoJust() {
        Mono.just("MyRxJavaApplication")
                .subscribe(msg -> log.info("Result: {}", msg));
    }

    public static void monoEmpty() {
        Mono.empty()
                .subscribe(
                        data -> log.info("# emitted data: {}", data), // process data
                        err -> {}, // handle error
                        () -> log.info("# emitted onComplete") //  last step
                );
    }

    public static void concatWith() {
        Flux<Object> flux = Mono.justOrEmpty(null)
                                .concatWith(Mono.justOrEmpty("Job"));

        flux.subscribe(data -> log.info("mixed result: {}", data));
    }

    public static void concat() {
        Flux.concat(Flux.just("venus"),// same as Mono.just("venus)
                    Flux.just("earth"),
                    Flux.just("mars"))
                .collectList()// merge data as a list
                .subscribe(list -> log.info("# planets {}", list));
    }
}