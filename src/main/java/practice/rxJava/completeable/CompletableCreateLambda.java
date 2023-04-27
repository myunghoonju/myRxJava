package practice.rxJava.completeable;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CompletableCreateLambda {

    public static void main(String[] args) throws InterruptedException {
        Completable completable = Completable.create(emitter -> {
            int sum = 0;
            for (int i = 0; i < 100; i++) {
                sum += i;
            }
            System.out.println("sum " + sum);
            emitter.onComplete();
        });

        completable.subscribeOn(Schedulers.computation())
                .subscribe(
                   () -> System.out.println("complete"),
                    e -> System.out.println("error" + e)

                );

        Thread.sleep(1000L);
    }
}
