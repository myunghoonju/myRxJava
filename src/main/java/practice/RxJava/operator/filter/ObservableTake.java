package practice.RxJava.operator.filter;


import io.reactivex.rxjava3.core.Observable;
import practice.RxJava.data.Car;
import practice.RxJava.data.SampleData;

import java.util.concurrent.TimeUnit;

public class ObservableTake {

    public static void main(String[] args) throws Throwable {

        /**
         * 지정한 갯수만큼 데이터 발행
         * */
        Observable.just("a", "b", "c", "d")
                .take(2)
                .subscribe(data -> System.out.println(data));


        System.out.println("----------------------------------");

        /**
         *
         * 지정한 시간동안 데이터 발행
         * */
        Observable.interval(1000L, TimeUnit.MILLISECONDS)
                .take(3500L, TimeUnit.MILLISECONDS)
                .subscribe(data -> System.out.println(data));

        Thread.sleep(3500L);

        /**
         * takeUntil 1 :: 인자로 지정한 조건이 참이 될 때까지 발행
         * */
        Observable.fromIterable(SampleData.carList)
                .takeUntil((Car car) -> car.getCarName().equals("트랙스"))
                .subscribe(data -> System.out.println(data));


        /**
         * takeUntil 2 :: 파라미터로 받은 Flowable/Observable 최초로 데이터를 발행할 때까지 계속 데이터 발행
         * timer 와 함께 사용하여 특정 시점이 되기 전까지 데이터를 발행하곤 함
         * */
        Observable.interval(1000L, TimeUnit.MILLISECONDS)
                .takeUntil(Observable.timer(5500L, TimeUnit.MILLISECONDS))
                .subscribe(data -> System.out.println(data));
    }
}
