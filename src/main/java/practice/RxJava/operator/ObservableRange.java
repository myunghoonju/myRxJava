package practice.RxJava.operator;

import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

/**
 *
 * for, while 용도로 사용
 *
 * */
public class ObservableRange {

    public static void main(String[] args) throws Exception {
        Observable<Integer> source = Observable.range(0, 5);
        source.subscribe(num -> System.out.println(num));
    }
}
