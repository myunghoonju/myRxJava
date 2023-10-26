package practice.rxJava.scheduler;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class ParallelExample {

    public static void main(String[] args) throws Exception {
        test();
    }

    public static void test() throws Exception {
        Integer[] integers = {1,3,5,7,9,11,13,15};
        Flux.fromArray(integers)
            .parallel()
            .runOn(Schedulers.parallel())
            .subscribe(r -> log.info("result {}", r));

        Thread.sleep(100L);
    }
}
