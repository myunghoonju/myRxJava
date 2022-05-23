package practice.RxJava.maybe;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.MaybeEmitter;
import io.reactivex.rxjava3.core.MaybeObserver;
import io.reactivex.rxjava3.core.MaybeOnSubscribe;
import io.reactivex.rxjava3.disposables.Disposable;

import java.time.LocalDate;

public class MaybeCreate {

    public static void main(String[] args) {
        Maybe<String> stringMaybe = Maybe.create(new MaybeOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull MaybeEmitter<String> emitter) throws Throwable {
                //emitter.onSuccess(LocalDate.now().toString());
                emitter.onComplete();
            }
        });

        stringMaybe.subscribe(new MaybeObserver<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull String s) {
                System.out.println("success " + s);

            }

            @Override
            public void onError(@NonNull Throwable e) {
                System.out.println("error " + e);
            }

            @Override
            public void onComplete() {
                System.out.println("complete");
            }
        });
    }
}
