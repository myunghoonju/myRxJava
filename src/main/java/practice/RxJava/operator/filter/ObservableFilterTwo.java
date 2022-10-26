package practice.RxJava.operator.filter;

import io.reactivex.rxjava3.core.Observable;
import practice.RxJava.data.CarMaker;
import practice.RxJava.data.SampleData;

public class ObservableFilterTwo {

    public static void main(String[] args) {
        Observable.fromIterable(SampleData.carList)
                .filter(car -> car.getCarMaker() == CarMaker.CHEVROLET)
                .filter(car -> car.getCarPrice() > 30000000)
                .subscribe(car -> System.out.println(car.getCarName()));
    }
}
