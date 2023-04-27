package practice.rxJava.subject;

import io.reactivex.rxjava3.subjects.AsyncSubject;
import lombok.extern.slf4j.Slf4j;

// 구독시점 상관없이 가장 마지막에 통지된 데이터 전달
// 완료 시점 이후에도 데이터 전달 (behaviour 에서는 오류)
@Slf4j
public class AsyncExample {

    public static void main(String[] args) {
        AsyncSubject<Integer> asyncSubj = AsyncSubject.create();

        asyncSubj.onNext(1000);
        asyncSubj.doOnNext(price -> log.info("do on next consumer 1: {}", price))
                 .subscribe(price -> log.info("on next consumer 1: {}", price));

        asyncSubj.onNext(2000);
        asyncSubj.doOnNext(price -> log.info("do on next consumer 2: {}", price))
                 .subscribe(price -> log.info("on next consumer 2: {}", price));


        asyncSubj.onNext(3000);
        asyncSubj.doOnNext(price -> log.info("do on next consumer 3: {}", price))
                .subscribe(price -> log.info("on next consumer 3: {}", price));

        asyncSubj.onNext(4000);
        asyncSubj.onComplete();

        asyncSubj.doOnNext(price -> log.info("do on next consumer 4: {}", price))
                .subscribe(price -> log.info("on next consumer 4: {}", price));
    }
}
