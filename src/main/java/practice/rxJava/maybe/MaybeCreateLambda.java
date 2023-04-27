package practice.rxJava.maybe;

import io.reactivex.rxjava3.core.Maybe;

import java.time.LocalDate;

public class MaybeCreateLambda {

    public static void main(String[] args) {
        Maybe<String> stringMaybe = Maybe.create(
                emitter -> emitter.onSuccess(LocalDate.now().toString())
        );

        stringMaybe.subscribe(
                data -> System.out.println("success " + data),
                err -> System.out.println("err " + err),
                () -> System.out.println("complete")
        );
    }
}
