package practice.rxJava.operator.creator;

import io.reactivex.rxjava3.core.Observable;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

public class ObservableFuture {

    public static void main(String[] args) throws Exception {
        System.out.println("start" + LocalDateTime.now());

        CompletableFuture<Double> future = longTimeWork();

        shortTimeWork();

        Observable.fromFuture(future)
                .subscribe(data -> System.out.println("job done :: " + data));
    }

    public static CompletableFuture<Double> longTimeWork() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return calc();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static Double calc() throws InterruptedException {
        System.out.println("Work Taking a long time");
        Thread.sleep(8000L);
        return 10000.1;
    }

    private static void shortTimeWork() throws InterruptedException {
        Thread.sleep(3000L);
        System.out.println("short time work done");
    }
}
