package practice.rxJava.scheduler;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadExample {

    public static void main(String[] args) throws InterruptedException {
        Observable<String> obs = Observable.just("1", "2", "3", "4", "5");
        //oneEx(obs);
        //twoEx(obs);
        threeEx(obs);
        Thread.sleep(1000L);

    }

    //create every single thread
    static void oneEx(Observable<String> input) {
        input.subscribeOn(Schedulers.newThread())
                .map(data -> "# " + data + " #")
                .subscribe(data -> log.info("onNext {}", data));

        input.subscribeOn(Schedulers.newThread())
                .map(data -> "# " + data + " #")
                .subscribe(data -> log.info("onNext {}", data));
    }

    //using queue without creating a thread
    static void twoEx(Observable<String> input) {
        input.subscribeOn(Schedulers.trampoline())
                .map(data -> "# " + data + " #")
                .subscribe(data -> log.info("onNext {}", data));

        input.subscribeOn(Schedulers.trampoline())
                .map(data -> "$ " + data + " $")
                .subscribe(data -> log.info("onNext {}", data));
    }

    //create only one thread
    static void threeEx(Observable<String> input) {
        input.subscribeOn(Schedulers.single())
                .map(data -> "# " + data + " #")
                .subscribe(data -> log.info("onNext {}", data));

        input.subscribeOn(Schedulers.single())
                .map(data -> "$ " + data + " $")
                .subscribe(data -> log.info("onNext {}", data));
    }
}
