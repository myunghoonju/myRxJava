package practice.RxJava.flowable;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FlowableCreateLambda {

    public static void main(String[] args) throws InterruptedException {
        Flowable<String> flowable = Flowable.create(
                emitter -> {
                    String[] data = {"Hello", "RxJava"};
                    for (String str : data) {
                        if (emitter.isCancelled()) {//stop if unsubscribed
                            return;
                        }
                        emitter.onNext(str);
                    }
                    emitter.onComplete();
                }, BackpressureStrategy.BUFFER // if slow then save data to buffer
        );

        flowable.observeOn(Schedulers.computation())
                .subscribe(
                        data -> log.info("onNext {}", data),
                        error -> log.info("onError {}", error),
                        () -> log.info("onComplete")
                );
        Thread.sleep(500L);
    }
}
