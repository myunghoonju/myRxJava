package practice.rxJava.operator.creator;

import io.reactivex.rxjava3.core.Observable;

import java.time.LocalTime;

/**
 *
 * 데이터 생성을 미루는 효과
 * 실제 구독이 발생할 때 Observable 새로 반환 -> 데이터 흐름 생성 지연
 *
 * */
public class ObservableDiff {

    public static void main(String[] args) throws Exception {
        Observable<LocalTime> just = Observable.just(LocalTime.now());
        Observable<LocalTime> defer = Observable.defer(() -> {
            LocalTime now = LocalTime.now();
            return Observable.just(now);
        });

        just.subscribe(time -> System.out.println("just1:: time " + time));
        defer.subscribe(time -> System.out.println("defer1:: time " + time));

        Thread.sleep(3000L);


        just.subscribe(time -> System.out.println("just2:: time " + time));
        defer.subscribe(time -> System.out.println("defer2:: time " + time));
    }
}
