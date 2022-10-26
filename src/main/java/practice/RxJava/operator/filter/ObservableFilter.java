package practice.RxJava.operator.filter;

import io.reactivex.rxjava3.core.Observable;
import practice.RxJava.data.CarMaker;
import practice.RxJava.data.SampleData;

public class ObservableFilter {

    public static void main(String[] args) {
        Observable.fromIterable(SampleData.carList)
                .filter(car -> car.getCarMaker() == CarMaker.CHEVROLET)
                .subscribe(car -> System.out.println(car.getCarMaker() + " : " + car.getCarName()));
    }
}
