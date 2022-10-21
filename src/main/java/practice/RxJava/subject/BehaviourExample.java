package practice.RxJava.subject;

import io.reactivex.rxjava3.subjects.BehaviorSubject;
import lombok.extern.slf4j.Slf4j;

//구독 시점에 발행된 마지막 데이터를 포함하여 이후 발행되는 데이터를 전달한다
@Slf4j
public class BehaviourExample {

    public static void main(String[] args) {
        BehaviorSubject<Integer> subj = BehaviorSubject.createDefault(3000);

        subj.subscribe(price -> log.info("on next consumer 1: {}", price));
        subj.onNext(3500);
        subj.subscribe(price -> log.info("on next consumer 2: {}", price));
        subj.onNext(3300);
        subj.subscribe(price -> log.info("on next consumer 3: {}", price));
        subj.onNext(3400);
    }
}
