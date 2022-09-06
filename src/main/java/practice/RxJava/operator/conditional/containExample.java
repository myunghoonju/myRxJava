package practice.RxJava.operator.conditional;

import io.reactivex.rxjava3.core.Observable;
import practice.RxJava.operator.data.CarMaker;
import practice.RxJava.operator.data.SampleData;

public class containExample {

    public static void main(String[] args) {
        Observable.fromArray(SampleData.carMakersDuplicated)
                .doOnNext(data -> System.out.println(data))
                .contains(CarMaker.HYUNDAE)
                .subscribe(data -> System.out.println(data));
    }
}
