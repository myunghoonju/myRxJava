package practice.RxJava.operator;

import io.reactivex.rxjava3.core.Observable;

import java.util.Arrays;
/**
 *
 *  List Iterate
 *
 * */
public class ObservableFromIterable {

    public static void main(String[] args) {
        Observable.fromIterable(Arrays.asList("Korea", "Canada", "USA", "UK"))
                .subscribe(country -> System.out.println("--> " + country));
    }
}
