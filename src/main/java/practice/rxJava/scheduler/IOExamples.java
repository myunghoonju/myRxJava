package practice.rxJava.scheduler;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import practice.rxJava.data.SampleData;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;

@Slf4j
public class IOExamples {

    public static void main(String[] args) throws Exception {
        File[] files = new File("src/main/java/practice/RxJava/").listFiles();
        //one(files);
        two(files);
        //three(files);
        //four();
        Thread.sleep(1000L);
    }

    static void one(File[] input) {
        Observable.fromArray(input)
                .doOnNext(data -> log.info("doOnNext, file name {}", data.getName()))
                .filter(data -> data.isDirectory())
                .map(dir -> dir.getName())
                .subscribeOn(Schedulers.io())
                .subscribe(data -> log.info("subscribe {}", data));
    }

    static void two(File[] input) {
        Observable.fromArray(input)
                .doOnNext(data -> log.info("doOnNext {}", data))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .filter(data -> data.isDirectory())
                .map(dir -> dir.getName())
                .subscribe(data -> log.info("subscribe {}", data));
    }

    static void three(File[] input) {
        Observable.fromArray(input)
                .doOnNext(data -> log.info("doOnNext, publish data {}", data))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .filter(data -> data.isDirectory())
                .doOnNext(data -> log.info("doOnNext, passing filter"))
                .observeOn(Schedulers.computation())
                .map(dir -> dir.getName())
                .doOnNext(data -> log.info("doOnNext, passing map"))
                .observeOn(Schedulers.computation())
                .subscribe(data -> log.info("subscribe {}", data));
    }

    static void four() {
        Observable<Integer> obs1 = Observable.fromIterable(SampleData.seoulPM10List);
        Observable<Integer> obs2 = Observable.fromIterable(SampleData.busanPM10List);
        Observable<Integer> obs3 = Observable.fromIterable(SampleData.incheonPM10List);
        Observable<Integer> obs4 = Observable.range(1, 24);

        Observable<String> source = Observable.zip(obs1, obs2, obs3, obs4,
                (data1, data2, data3, hour) -> hour + "hour: " + Collections.max(Arrays.asList(data1, data2, data3)));

        source.subscribeOn(Schedulers.computation())
                .subscribe(data -> log.info("onNext {}", data));
        source.subscribeOn(Schedulers.computation())
                .subscribe(data -> log.info("onNext {}", data));
    }
}
