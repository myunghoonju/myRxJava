package practice.RxJava.other;

import io.reactivex.rxjava3.core.Observable;
import practice.RxJava.operator.data.SampleData;

import java.util.Arrays;

public class CountExample {

    public static void main(String[] args) {
        Observable.fromIterable(SampleData.carList)
                .count()
                .subscribe(data -> System.out.println(data));

        Observable.concat(
                Arrays.asList(
                        Observable.fromIterable(SampleData.seoulPM10List),
                        Observable.fromIterable(SampleData.busanPM10List),
                        Observable.fromIterable(SampleData.incheonPM10List))
        ).count()
         .subscribe(data -> System.out.println(data));
    }
}
