package practice.RxJava.doFunction;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;
import practice.RxJava.operator.data.CarMaker;
import practice.RxJava.operator.data.SampleData;

import java.util.concurrent.TimeUnit;

@Slf4j
public class doOnDisposeExample {

    public static void main(String[] args) throws InterruptedException {
        Observable.fromArray(SampleData.carMakers)
                .zipWith(Observable.interval(300L, TimeUnit.MILLISECONDS), (carMaker, aLong) -> carMaker)
                //when consumer cancel subscription
                .doOnDispose(() -> log.info("doOnDispose, producer:: cancel subscription"))
                .subscribe(new Observer<>() {
                    private Disposable disposable;
                    private long startTime;

                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                        this.disposable = disposable;
                        this.startTime = System.currentTimeMillis();
                    }

                    @Override
                    public void onNext(@NonNull CarMaker carMaker) {
                        log.info("onNext {}", carMaker);
                        if (System.currentTimeMillis() - startTime > 1000L) {
                            log.info("consumer cancel subscription");
                            disposable.dispose(); //cancel subscription
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        log.info("onError {}", e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        log.info("onComplete");
                    }
                });

        Thread.sleep(2000L);
    }
}
