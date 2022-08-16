package practice.RxJava.backpressure;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import practice.RxJava.other.MTimeUtil;

import java.util.concurrent.TimeUnit;

@Slf4j
public class BackPressureDrop {
    /**
     * 버퍼가 가득차면 버퍼 바깥쪽에서 통지 대기중인 데이터들은 계속 파기(DROP)하고
     * 버퍼를 비운 시점에 Drop 되지 않고 대기중인 데이터부터 버퍼에 담는다.
     */
    public static void main(String[] args) {
        Flowable.interval(300L, TimeUnit.MILLISECONDS)
                .doOnNext(data -> log.info("#inverval doOnNext() {}", data))
                .onBackpressureDrop(dropData -> log.info("onPrint {}", dropData + " Drop!"))
                .observeOn(Schedulers.computation(), false, 1)
                .subscribe(
                        data -> {
                            MTimeUtil.sleep(1000L);
                            log.info("process data :: onNext {}", data);
                        },
                        error -> log.info("onError {}", error)
                );

        MTimeUtil.sleep(5500L);
    }
}
