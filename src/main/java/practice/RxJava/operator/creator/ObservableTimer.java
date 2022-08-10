package practice.RxJava.operator.creator;

import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

/**
 *
 * 설정한 시간이 지난 후에
 * 특정 동작을 수행하는 용도로 사용
 *
 * */
public class ObservableTimer {

    public static void main(String[] args) throws Exception {
        Observable<String> timer = Observable.timer(2000L, TimeUnit.MILLISECONDS).map(count -> "Do Work");
        timer.subscribe(data -> System.out.println(data));

        //slow down main thread
        Thread.sleep(3000L);
    }
}
