package practice.rxJava;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
public class MyRxJava {

    public static void main(String[] args) throws Exception {
        //monoJust();
        //monoEmpty();
        //concatWith();
        //concat();
        //colidSeq();
        hotSeq();
    }

    public static void monoJust() {
        Mono.just("MyRxJavaApplication")
                .subscribe(msg -> log.info("Result: {}", msg));
    }

    public static void monoEmpty() {
        Mono.empty()
                .subscribe(
                        data -> log.info("# emitted data: {}", data), // process data
                        err -> {
                        }, // handle error
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

    public static void colidSeq() {
        List<String> colors = new ArrayList<>();
        colors.add("RED");
        colors.add("YELLOW");
        colors.add("PINK");

        Flux<String> cold = Flux.fromIterable(colors);

        cold.subscribe(val -> log.info("# Subscriber 1 {}", val));
        log.info("=========");
        cold.subscribe(val -> log.info("# Subscriber 2 {}", val));

    }

    public static void hotSeq() throws Exception {
        Flux<String> hot = Flux.fromStream(Stream.of("a", "b", "c", "d", "e"))
                               .delayElements(Duration.ofSeconds(1))
                               .share(); // create another thread

        hot.subscribe(val -> log.info("# val 1 {}", val));
        Thread.sleep(2500L);
        hot.subscribe(val -> log.info("# val 2 {}", val));
        Thread.sleep(3000L); // prevent ending main thread first, waiting for the other thread to finish.
    }
}