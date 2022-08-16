package practice.RxJava.other;


import io.reactivex.rxjava3.processors.PublishProcessor;

public class HotPublisherEX {
    //processor, subject
    public static void main(String[] args) {
        PublishProcessor<Object> processor = PublishProcessor.create();

        processor.subscribe(data -> System.out.println("subscriber1: " + data));
        processor.onNext(1);
        processor.onNext(3);


        processor.subscribe(data -> System.out.println("subscriber2: " + data));
        processor.onNext(5);
        processor.onNext(7);

        processor.onComplete();
    }
}
