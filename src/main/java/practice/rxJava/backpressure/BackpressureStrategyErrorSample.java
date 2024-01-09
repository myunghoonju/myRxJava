package practice.rxJava.backpressure;

import java.time.Duration;

import practice.rxJava.utility.Log;
import practice.rxJava.utility.Time;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class BackpressureStrategyErrorSample {

    public static void main(String[] args) {
        Flux.interval(Duration.ofMillis(1L))
            .onBackpressureError()
            .doOnNext(Log::doOnNext)
            .publishOn(Schedulers.parallel())
            .subscribe(data -> {
                Time.sleep(5L);
                Log.onNext(data);
            }, error -> Log.onError(error));

        Time.sleep(2000L);
    }
}
