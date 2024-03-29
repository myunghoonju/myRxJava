package practice.rxJava.operator.creator;

import io.reactivex.rxjava3.core.Observable;

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
