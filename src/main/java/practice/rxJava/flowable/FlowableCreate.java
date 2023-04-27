package practice.rxJava.flowable;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableEmitter;
import io.reactivex.rxjava3.core.FlowableOnSubscribe;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

@Slf4j
public class FlowableCreate {

    public static void main(String[] args) throws InterruptedException {
        Flowable<String> flowable = Flowable.create(
                new FlowableOnSubscribe<String>() {
                   @Override
                   public void subscribe(@NonNull FlowableEmitter<String> emitter) throws Throwable {
                       String[] data = {"Hello", "RxJava"};
                       for (String str : data) {
                           if (emitter.isCancelled()) {//stop if unsubscribed
                               return;
                           }
                           emitter.onNext(str);
                       }
                       emitter.onComplete();
                   }
                }, BackpressureStrategy.BUFFER // if slow then save data to buffer
        );

        flowable.observeOn(Schedulers.computation())
                .subscribe(new Subscriber<String>() {
                    // for sub/unsub
                    private Subscription s;

                    @Override
                    public void onSubscribe(Subscription s) {
                        this.s = s;
                        this.s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(String data) {
                        log.info("onNext {}", data);
                    }

                    @Override
                    public void onError(Throwable err) {
                        log.info("onError {}", err);
                    }

                    @Override
                    public void onComplete() {
                        log.info("onComplete");
                    }
                });

        Thread.sleep(500L);
    }
}
