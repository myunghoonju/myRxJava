package practice.rxJava.utility;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.Arrays;

public class MaterialUtil {

    public static void main(String[] args) throws Exception {
        materialTwo();
        Thread.sleep(1000L);
    }

    static void material() {
        Observable.just(1, 3, 5, 7, 9)
                .materialize()
                .subscribe(integerNotification -> {
                    boolean isOnNext = integerNotification.isOnNext();
                    boolean isOnComplete = integerNotification.isOnComplete();
                    boolean isOnError = integerNotification.isOnError();
                    Integer value = integerNotification.getValue();
                    System.out.println("isOnNext " + isOnNext);
                    System.out.println("isOnComplete " + isOnComplete);
                    System.out.println("isOnError" + isOnError);
                    System.out.println("value " + value);
                });
    }

    static void materialTwo() {
        Observable.concatEager(
                Observable.just(
                        getDbUser().subscribeOn(Schedulers.io()),
                        getApiUser()
                                .subscribeOn(Schedulers.io())
                                .materialize()
                                .map(stringNotification -> {
                                    if (stringNotification.isOnError()) {
                                        System.out.println("api error occurred");
                                    }
                                    return stringNotification;
                                })
                                .filter(stringNotification -> !stringNotification.isOnError())
                                .dematerialize(stringNotification -> stringNotification)
                )
        ).subscribe(
                data -> System.out.println("subscribe:: " + data),
                error -> System.out.println("error:: " + error),
                () -> System.out.println("complete")
        );
    }

    static Observable<String> getDbUser() {
        return Observable.fromIterable(Arrays.asList(
                "db user1",
                "db user2",
                "db user3",
                "db user4",
                "db user5"
        ));
    }

    static Observable<String> getApiUser() {
        return Observable.just(
                "api user1",
                "api user2",
                "api user3",
                "not user",
                "api user5")
                .map(user -> {
                    if ("not user".equals(user)) {
                        throw new RuntimeException("not user");
                    }

                    return user;
                });
    }
}
