package practice.rxJava.backpressure;

import io.reactivex.rxjava3.core.BackpressureOverflowStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import practice.rxJava.utility.MTimeUtil;

import java.util.concurrent.TimeUnit;

@Slf4j
public class DropLatest {
    /**
     * DROP_LATEST 전략 : 생산자쪽에서 데이터 통지 시점에 버퍼가 가득 차있으면 버퍼내에 있는 데이터 중에서 가장 최근에 버퍼
     * 안에 들어온 데이터를 삭제하고 버퍼 밖에서 대기하는 데이터를 그 자리에 채운다.
     */
    public static void main(String[] args) {
        log.info("start {}", MTimeUtil.getCurrentTimeFormatted());
        Flowable.interval(300L, TimeUnit.MILLISECONDS)
                .doOnNext(data -> log.info("#inverval doOnNext() {}", data))
                .onBackpressureBuffer(
                        2,
                        () -> log.info("overflow!"),
                        BackpressureOverflowStrategy.DROP_LATEST)
                .doOnNext(data -> log.info("#onBackpressureBuffer doOnNext() {}", data))
                .observeOn(Schedulers.computation(), false, 1)
                .subscribe(
                        data -> {
                            MTimeUtil.sleep(1000L);
                            log.info("process data :: onNext() {}", data);
                        },
                        error -> log.info("onError() {}", error)
                );

        MTimeUtil.sleep(2800L);
    }
}
