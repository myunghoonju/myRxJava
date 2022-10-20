package practice.RxJava.subject;

import io.reactivex.rxjava3.subjects.AsyncSubject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AsyncSubjExample {

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
