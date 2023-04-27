package practice.rxJava.subject;

import io.reactivex.rxjava3.subjects.ReplaySubject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReplayExample {

    public static void main(String[] args) {
     getSize();
    }

    static void getSize() {
        ReplaySubject<Integer> subject = ReplaySubject.createWithSize(2);

        subject.onNext(3000);
        subject.onNext(2500);

        subject.subscribe(price -> log.info("on next #consumer1 {}", price));
        subject.onNext(3500);
        subject.subscribe(price -> log.info("on next #consumer2 {}", price));
        subject.onNext(3300);
        subject.subscribe(price -> log.info("on next #consumer3 {}", price));
        subject.onNext(3400);

        subject.onComplete();

        subject.subscribe(price -> log.info("on next #consumer4 {}", price));
    }

     static void getValues() {
        ReplaySubject<Integer> subject = ReplaySubject.create();

        subject.onNext(3000);
        subject.onNext(2500);

        subject.subscribe(price -> log.info("on next #consumer1 {}", price));
        subject.onNext(3500);
        subject.subscribe(price -> log.info("on next #consumer2 {}", price));
        subject.onNext(3300);

        subject.onComplete();

        subject.subscribe(price -> log.info("on next #consumer3 {}", price));
    }


}
