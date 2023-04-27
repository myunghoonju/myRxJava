package practice.rxJava.operator.conditional;

import io.reactivex.rxjava3.core.Observable;
import practice.rxJava.data.CarMaker;
import practice.rxJava.data.SampleData;

public class allExample {

    public static void main(String[] args) {
        Observable.fromIterable(SampleData.carList)
                .doOnNext(car -> {
                    System.out.println("car maker:: " + car.getCarMaker());
                    System.out.println("car name:: " + car.getCarName());
                })
                .map(car -> car.getCarMaker())
                .all(carMaker -> CarMaker.CHEVROLET.equals(carMaker))
                .subscribe(data -> System.out.println(data));
    }
}
