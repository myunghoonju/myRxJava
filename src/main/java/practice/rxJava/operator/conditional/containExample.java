package practice.rxJava.operator.conditional;

import io.reactivex.rxjava3.core.Observable;
import practice.rxJava.data.CarMaker;
import practice.rxJava.data.SampleData;

public class containExample {

    public static void main(String[] args) {
        Observable.fromArray(SampleData.carMakersDuplicated)
                .doOnNext(data -> System.out.println(data))
                .contains(CarMaker.HYUNDAE)
                .subscribe(data -> System.out.println(data));
    }
}
