package practice.RxJava;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observers.TestObserver;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import practice.RxJava.data.Car;
import practice.RxJava.data.SampleObservable;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

import static practice.RxJava.data.CarMaker.HYUNDAE;
import static practice.RxJava.data.CarMaker.SAMSUNG;

@Slf4j
public class AssertTest {
    /**
     * assertEmpty
     * */
    @Test
    public void assertEmpty_fail_test() {
        Observable<Car> observable = SampleObservable.getCarStream();
        TestObserver<Car> test = observable.test();

        //test.awaitDone(100L, TimeUnit.MILLISECONDS).assertEmpty();
    }

    @Test
    public void assertEmpty_success_test() {
        //when
        Observable<Car> observable = SampleObservable.getCarStream();
        TestObserver<Car> test = observable.delay(1000L, TimeUnit.MILLISECONDS).test();
        //then
        test.awaitDone(100L, TimeUnit.MILLISECONDS).assertEmpty();
    }

    /**
     * assertValue, assertValues
     * */
    @Test
    public void assertValue_test() {
        Observable.just("a")
                  .test()
                  .assertValue("a");
    }
    
    @Test
    public void assertValue_test_two() {
        SampleObservable.getCarMakerStream()
                .filter(SAMSUNG::equals)
                .test()
                .awaitDone(1L, TimeUnit.MILLISECONDS)
                .assertValue(SAMSUNG);
    }

    @Test
    public void assertValue_test_three() {
        SampleObservable.getDuplicatedCarMakerStream()
                .filter(HYUNDAE::equals)
                .test()
                .awaitDone(1L, TimeUnit.MILLISECONDS)
                .assertValues(HYUNDAE, HYUNDAE);
    }

    @Test
    public void assertValue_test_four() {
        Observable.interval(200L, TimeUnit.MILLISECONDS)
                .doOnNext(data -> log.info("doOnNext:: {}", data))
                .filter(data -> data > 5)
                .test()
                .awaitDone(1000L, TimeUnit.MILLISECONDS)
                .assertNoValues();
    }

    @Test
    public void assertResult_fail_test() throws Exception {
        Observable.interval(200L, TimeUnit.MILLISECONDS)
                .doOnNext(data -> log.info("doOnNext:: {}", data))
                .filter(data -> data > 3)
                .test()
                .awaitDone(1100L, TimeUnit.MILLISECONDS);
                //.assertResult(4L); //uncompleted
    }

    @Test
    public void assertResult_test() throws Exception {
        Observable.interval(200L, TimeUnit.MILLISECONDS)
                .doOnNext(data -> log.info("doOnNext:: {}", data))
                .take(5) //completed
                .filter(data -> data > 3)
                .test()
                .awaitDone(1100L, TimeUnit.MILLISECONDS)
                .assertResult(4L);
    }
    
    @Test
    public void assertError_test() throws Exception {
        Observable.interval(100L, TimeUnit.MILLISECONDS)
                .map(data -> {
                    long val;
                    if (data == 4) {
                        val = data / 0;
                    } else {
                        val = data / 2;
                    }

                    return val;
                })
                .test()
                .awaitDone(1000L, TimeUnit.MILLISECONDS)
                //.assertError(Throwable.class);
                .assertError(ArithmeticException.class::isInstance);
    }

    @Test
    public void assertComplete_test() throws Exception {
        SampleObservable.getSalesOfBranchA()
                .zipWith(SampleObservable.getSalesOfBranchB(),
                            (a, b) -> {
                                Thread.sleep(100L);
                                return a + b;
                            }
                )
                .test()
                .awaitDone(3000L, TimeUnit.MILLISECONDS)
                .assertComplete();
    }

    @Test
    public void assertNotComplete_test() throws Exception {
        SampleObservable.getSalesOfBranchA()
                .zipWith(SampleObservable.getSalesOfBranchB(),
                        (a, b) -> {
                            Thread.sleep(10000L);
                            return a + b;
                        }
                )
                .test()
                .awaitDone(3000L, TimeUnit.MILLISECONDS)
                .assertNotComplete();
    }
}
