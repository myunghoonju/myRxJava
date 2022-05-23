package practice.RxJava.maybe;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

import java.time.LocalDate;

public class MaybeSingle {

    public static void main(String[] args) {
        Single<String> single = Single.just(LocalDate.now().toString());
        Maybe.fromSingle(single)
                .subscribe(
                        data -> System.out.println("success"),
                        error -> System.out.println("failure"),
                        () -> System.out.println("complete"));
    }
}
