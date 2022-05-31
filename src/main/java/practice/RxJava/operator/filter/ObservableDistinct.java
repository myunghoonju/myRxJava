package practice.RxJava.operator.filter;

import io.reactivex.rxjava3.core.Observable;
import practice.RxJava.operator.data.CarMaker;
import practice.RxJava.operator.data.SampleData;

public class ObservableDistinct {

    public static void main(String[] args) {

        System.out.println("--------------------distinct01------------------------------------------");

        Observable.fromArray(SampleData.carMakersDuplicated)
                .distinct()
                .subscribe(carMaker -> System.out.println("result :: " + carMaker.toString()));

        System.out.println("------------------distinct02----------------------");

        Observable.fromIterable(SampleData.carList)
                .distinct(car -> car.getCarMaker())
                .subscribe(car -> System.out.println("result :: " + car.getCarName()));

        System.out.println("------------distinct---filter------------------------");

        Observable.fromArray(SampleData.carMakersDuplicated)
                .distinct()
                .filter(carMaker -> carMaker == CarMaker.CHEVROLET)
                .subscribe(carMaker -> System.out.println("result :: " + carMaker.toString()));

    }
}
