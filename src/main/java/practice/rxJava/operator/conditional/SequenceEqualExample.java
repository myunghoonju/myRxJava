package practice.rxJava.operator.conditional;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import practice.rxJava.data.CarMaker;
import practice.rxJava.data.SampleData;

public class SequenceEqualExample {

    public static void main(String[] args) {
        Observable<CarMaker> obs1 = Observable.fromArray(SampleData.carMakers)
                .subscribeOn(Schedulers.computation())
                .delay(carMaker -> {
                    Thread.sleep(500L);
                    return Observable.just(carMaker);
                })
                .doOnNext(data -> System.out.println("obs1:: " + data));

        Observable<CarMaker> obs2 = Observable.fromArray(SampleData.carMakersDuplicated)
                .delay(carMaker -> {
                    Thread.sleep(1000L);
                    return Observable.just(carMaker);
                })
                .doOnNext(data -> System.out.println("obs2:: " + data));

        Observable.sequenceEqual(obs1, obs2)
                .subscribe(data -> System.out.println(data));
    }
}
