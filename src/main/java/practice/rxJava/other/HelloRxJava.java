package practice.rxJava.other;

import io.reactivex.rxjava3.core.Observable;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class HelloRxJava {

    public static void main(String[] args) {
        Observable<String> obs = Observable.just("HELLO", "RxJava");
        obs.subscribe(data -> log.info("print data = {}", data));
        concurrency();
    }

    static void flux() {
        List<Integer> data = new ArrayList<>();
        Flux.just(1, 2, 3, 4)
                .log()
                .subscribe(data::add);
        for (Integer num : data) {
            log.info("num {}", num);
        }
    }

    static void mono() {
        Mono<Integer> just = Mono.just(1);
        log.info("mono.just {}", just);
    }

    static void backPressure() {
        List<Integer> data = new ArrayList<>();
        Flux.just(1, 2, 3, 4)
                .log()
                .subscribe(new Subscriber<Integer>() {

                    private Subscription sub;
                    private int onNextAmt;

                    @Override
                    public void onSubscribe(Subscription s) {
                        sub = s;
                        sub.request(2);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        data.add(integer);
                        onNextAmt++;
                        if (onNextAmt % 2 == 0) {
                            log.info("onNExtAmt {}", onNextAmt);
                            sub.request(2);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    static void mapping() {
        List<Integer> data = new ArrayList<>();
        Flux.just(1, 2, 3, 4)
                .log()
                .map(num -> num * 2)
                .subscribe(data::add);

        for (Integer num : data) {
            log.info("num {}", num);
        }
    }

    static void combiningStreams() {
        List<Object> data = new ArrayList<>();
        Flux.just(1, 2, 3, 4)
                .log()
                .map(i -> i * 2)
                .log()
                .zipWith(
                        Flux.range(0, Integer.MAX_VALUE),
                        (one, two) -> String.format("First flux: %d, second flux: %d", one, two))
                .log()
                .subscribe(s -> data.add(s));

        for (Object num : data) {
            log.info("num {}", num);
        }
    }

    static void hotStream() {
        ConnectableFlux<Object> publish = Flux.create(fluxSink -> {
            while (true) {
                fluxSink.next(System.currentTimeMillis());
            }
        }).publish();

        publish.subscribe(System.out::println);
        publish.subscribe(System.out::println);


        publish.sample(Duration.ofSeconds(2));
        publish.connect();
    }

    static void concurrency() {
        List<Integer> data = new ArrayList<>();
        Flux.just(1, 2, 3, 4)
                .log()
                .map(i -> i * 2)
                .subscribeOn(Schedulers.parallel())
                .subscribe(data::add);

        for (Integer num : data) {
            log.info("num {}", num);
        }
    }
}