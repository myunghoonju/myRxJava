package practice.RxJava.subject;

import io.reactivex.rxjava3.subjects.PublishSubject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PublishExample {

    public static void main(String[] args) {
        PublishSubject<Integer> subj = PublishSubject.create();

        subj.subscribe(price -> log.info("on next, consumer 1: {}", price));
        subj.onNext(3500);
        subj.subscribe(price -> log.info("on next, consumer 2: {}", price));
        subj.onNext(3300);
        subj.subscribe(price -> log.info("on next, consumer 3: {}", price));
        subj.onNext(3400);

        subj.subscribe(
                price -> log.info("on next consumer 4: {}", price),
                error -> log.info("on error: ", error),
                () -> log.info("complete")
        );

        subj.onComplete();
    }
}
