package practice.rxJava.operator.filter;

import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

public class ObservableSkip {

    public static void main(String[] args) throws Throwable {
        /**
         *  type 1
         * */
        Observable.range(0, 15)
                .skip(3)
                .subscribe(data -> System.out.println(data));

        System.out.println("---------------------------------`");
        /**
         * type 2
         * */
        Observable.interval(300L, TimeUnit.MILLISECONDS)
                .skip(1000L, TimeUnit.MILLISECONDS)
                .subscribe(data -> System.out.println(data));

        Thread.sleep(3000L);
    }
}
