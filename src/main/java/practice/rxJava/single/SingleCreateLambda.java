package practice.rxJava.single;

import io.reactivex.rxjava3.core.Single;

import java.time.LocalTime;

public class SingleCreateLambda {

    public static void main(String[] args) {
        Single<String> single = Single.create(emitter -> emitter.onSuccess(LocalTime.now().toString()));

        single.subscribe(
                data -> System.out.println("success " + data),
                error -> System.out.println("failure " + error));
    }
}
