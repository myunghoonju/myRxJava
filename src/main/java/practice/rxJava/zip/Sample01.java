package practice.rxJava.zip;

import java.time.Duration;

import practice.rxJava.utility.Log;
import practice.rxJava.utility.MTimeUtil;
import reactor.core.publisher.Flux;

public class Sample01 {

    public static void main(String[] args) {
        test02();
    }

    private static void test01() {
        Flux.zip(
                Flux.just(1, 2, 3).delayElements(Duration.ofMillis(300L)),
                Flux.just(4, 5, 6).delayElements(Duration.ofMillis(500L))
        ).subscribe(Log::onNext);

        MTimeUtil.sleep(2500L);
    }

    private static void test02() {
        Flux.zip(
                Flux.just(1, 2, 3).delayElements(Duration.ofMillis(300L)),
                Flux.just(4, 5, 6).delayElements(Duration.ofMillis(500L)),
                (n1, n2) -> n1 * n2
        ).subscribe(Log::onNext);

        MTimeUtil.sleep(2500L);
    }
}
