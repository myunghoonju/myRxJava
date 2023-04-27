package practice.rxJava.operator.conditional;

import io.reactivex.rxjava3.core.Observable;
import practice.rxJava.data.SampleData;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ambExample {

    public static void main(String[] args) throws InterruptedException {
        List<Observable<Integer>> observables = Arrays.asList(
                Observable.fromIterable(SampleData.salesOfBranchA)
                        .delay(200L, TimeUnit.MILLISECONDS)
                        .doOnComplete(() -> System.out.println("a banch")),
                Observable.fromIterable(SampleData.salesOfBranchB)
                        .delay(300L, TimeUnit.MILLISECONDS)
                        .doOnComplete(() -> System.out.println("b banch")),
                Observable.fromIterable(SampleData.salesOfBranchC)
                        .delay(100L, TimeUnit.MILLISECONDS)
                        .doOnComplete(() -> System.out.println("c banch"))
        );

        Observable.amb(observables)
                .doOnComplete(() -> System.out.println("complete"))
                .subscribe(data -> System.out.println(data));

        Thread.sleep(1000L);
    }
}
