package practice.RxJava.operator.transformer;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observables.GroupedObservable;
import lombok.extern.slf4j.Slf4j;
import practice.RxJava.operator.data.Car;
import practice.RxJava.operator.data.CarMaker;
import practice.RxJava.operator.data.SampleData;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class variousExample {

    public static void main(String[] args) {

    }

    static void sampleMap() {
        List<Integer> oddList = Arrays.asList(1, 3, 5, 7);

        Observable.fromIterable(oddList)
                .map(num -> num+1)
                .subscribe(data -> log.info("data :: {}", data));
    }

    static void uppercaseOnFirstLetter() {
        Observable.just("korea", "japan", "france", "netherlands", "china", "ireland")
                .filter(country -> country.length() > 5)
                .map(country ->
                        country.toUpperCase().charAt(0) +
                                country.substring(1))
                .subscribe(data -> log.info("country {}", data));
    }

    static void flatMapSample() {
        String originStr = "Hello";
        Observable.just(originStr)
                .flatMap(hello -> Observable.just("java", "python", "kotlin").map(lang -> originStr + ", " + lang))
                .subscribe(data -> log.info("{}", data));
    }

    static void flatMapSample2() {
        int number = 2;
        Observable.range(number, 1)
                .flatMap(num -> Observable.range(1, 9)
                        .map(row -> number + " * " + row + " = " + number * row))
                .subscribe(data -> log.info("{}", data));
    }

    static void flatMapSample3() {
        int number = 2;
        Observable.range(number, 1)
                .flatMap(num -> Observable.range(1, 9),
                        (source, changed) ->
                                source + " * " + changed + " = " + source * changed)
                .subscribe(data -> log.info("{}", data));
    }

    static void concatMapSample() {
        Observable.interval(100L, TimeUnit.MILLISECONDS)
                .take(4)
                .skip(2)
                .concatMap(number -> Observable.interval(200L, TimeUnit.MILLISECONDS)
                        .take(10)
                        .skip(1)
                        .map(row -> number + " * " + row + "=" + number * row))
                .subscribe(data -> log.info(data));

    }

    static void concatMapSample2() {
        Observable.interval(100L, TimeUnit.MILLISECONDS)
                .take(4)
                .skip(2)
                .flatMap(number -> Observable.interval(200L, TimeUnit.MILLISECONDS)
                        .take(10)
                        .skip(1)
                        .map(row -> number + " * " + row + "="+number * row))
                .subscribe(data -> log.info("aa {}", data),
                        error -> {},
                        () -> {});
    }

    static void switchMapSample() {
        Observable.interval(100L, TimeUnit.MILLISECONDS)
                .take(4)
                .skip(2)
                .doOnNext(data -> log.info("{}", data))
                .switchMap(number -> Observable.interval(300L, TimeUnit.MILLISECONDS)
                        .take(10)
                        .skip(1)
                        .map(row -> number + " * " + row + "="+number * row))
                .subscribe(data -> log.info("aa {}", data));
    }

    static void groupBySample() {
        Observable<GroupedObservable<CarMaker, Car>> obs = Observable.fromIterable(SampleData.carList).groupBy(Car::getCarMaker);
        obs.subscribe(
                groupedObs -> groupedObs.filter(car -> groupedObs.getKey().equals(CarMaker.CHEVROLET))
                        .subscribe(car -> log.info("group {}, car {}", groupedObs.getKey(), car.getCarName()))
        );
    }

    static void toListWithSingleReturn() {
        Observable.fromIterable(SampleData.carList)
                .toList()
                .subscribe(cars -> log.info("{}", cars));
    }

    static void toMapWithSingleReturn() {
        Observable.just("a-Alpha", "b-Bravo", "c-Chalie", "e-echo")
                .toMap(
                        data -> data.split("-")[0],
                        data -> data.split("-")[1]
                )
                .subscribe(val -> log.info("{}", val.toString()));
    }
}
