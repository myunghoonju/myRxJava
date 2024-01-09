package practice.rxJava.backpressure;

import org.reactivestreams.Subscription;

import lombok.extern.slf4j.Slf4j;
import practice.rxJava.utility.Log;
import practice.rxJava.utility.Time;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

@Slf4j
public class BackPressureSample01 {

    public static void main(String[] args) {
        Flux.range(1, 5)
            .doOnNext(Log::doOnNext) // publisher
            .doOnRequest(Log::doOnRequest) // subscriber
            .subscribe(new BaseSubscriber<>() {
                @Override
                protected void hookOnSubscribe(Subscription subscription) {
                    request(1);
                }

                @Override
                protected void hookOnNext(Integer value) {
                    Time.sleep(2000L);
                    Log.onNext(value);
                    request(1);
                }
            });
    }
}
