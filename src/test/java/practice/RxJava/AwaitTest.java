package practice.RxJava;

import io.reactivex.rxjava3.core.Observable;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class AwaitTest {

    @Test
    public void awaitDone_test() throws Exception {
        Observable.interval(200L, TimeUnit.MILLISECONDS)
                .doOnNext(data -> log.info("doOnNext:: {}", data))
                .take(5)
                .doOnComplete(() -> log.info("doOnComplete"))
                .doOnError(e -> log.info("doOnError:: {}", e.getMessage()))
                .test()
                .awaitDone(500L, TimeUnit.MILLISECONDS)
                .assertNotComplete()
                .assertValueCount(2);
    }

    @Test
    public void awaitDone_test2() throws Exception {
        Observable.interval(200L, TimeUnit.MILLISECONDS)
                .doOnNext(data -> log.info("doOnNext:: {}", data))
                .take(5)
                .doOnComplete(() -> log.info("doOnComplete"))
                .doOnError(e -> log.info("doOnError:: {}", e.getMessage()))
                .test()
                .awaitDone(1500L, TimeUnit.MILLISECONDS)
                .assertComplete()
                .assertValueCount(5);
    }

    @Test
    public void awaitTest_one() throws Exception {
        boolean result = Observable.interval(1000L, TimeUnit.MILLISECONDS)
                .doOnNext(data -> log.info("doOnNext:: {}", data))
                .take(5)
                .doOnComplete(() -> log.info("doOnComplete"))
                .doOnError(e -> log.info("doOnError:: {}", e.getMessage()))
                .test()
                .await(2000L, TimeUnit.MILLISECONDS);

        assertThat(result).isFalse();
    }

    @Test
    public void awaitTest_two() throws Exception {
        Observable.interval(100L, TimeUnit.MILLISECONDS)
                .doOnNext(data -> log.info("doOnNext:: {}", data))
                .take(5)
                .doOnComplete(() -> log.info("doOnComplete"))
                .doOnError(e -> log.info("doOnError:: {}", e.getMessage()))
                .test()
                .await()
                .assertComplete()
                .assertValueCount(5);
    }

    @Test
    public void awaitCount_test() throws Exception {
        Observable.interval(200L, TimeUnit.MILLISECONDS)
                .doOnNext(data -> log.info("doOnNext:: {}", data))
                .take(5)
                .doOnComplete(() -> log.info("doOnComplete"))
                .doOnError(e -> log.info("doOnError:: {}", e.getMessage()))
                .test()
                .awaitCount(3)
                .assertNotComplete()
                .assertValueCount(3)
                .assertValues(0L, 1L, 2L);
    }
}
