package practice.rxJava.maybe;

import io.reactivex.rxjava3.core.Maybe;

public class MaybeJust {

    public static void main(String[] args) {
      /*  Maybe.just(LocalDate.now().toString())
                .subscribe(
                        data -> System.out.println("success"),
                        error -> System.out.println("failure"),
                        () -> System.out.println("complete"));*/
        Maybe.empty()
                .subscribe(
                        data -> System.out.println("success"),
                        error -> System.out.println("failure"),
                        () -> System.out.println("complete"));
    }
}
