 package practice.RxJava.other.errorHandle;

import io.reactivex.rxjava3.core.Observable;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

 @Slf4j
 public class OnErrorRetryRxjava {

     public static void main(String[] args) throws InterruptedException {
         retryEx2();
         Thread.sleep(6000L);
     }

     static void retryEx1() {
         Observable.just(5).flatMap(
                 num -> Observable.interval(200L, TimeUnit.MILLISECONDS)
                         .map(i -> {
                             long result;
                             try {
                                 result = num / i;
                             } catch (ArithmeticException ex) {
                                 log.info("error", ex);
                                 throw ex;
                             }

                             return result;
                         })
                         .retry(3)
                         .onErrorReturn(throwable -> -1L)

         ).subscribe(
                 data -> log.info("data = {}", data),
                 error -> log.info("error occurred", error),
                 () -> log.info("complete")
         );
     }

     static void retryEx2() {
         Observable.just(5).flatMap(
                 num -> Observable.interval(200L, TimeUnit.MILLISECONDS)
                         .map(i -> {
                             long result;
                             try {
                                 result = num / i;
                             } catch (ArithmeticException ex) {
                                 log.info("error", ex);
                                 throw ex;
                             }

                             return result;
                         })
                         .retry((cnt, ex) -> {
                            log.info("cnt {}", cnt);
                            Thread.sleep(1000L);
                             return (cnt < 5);
                         })
                         .onErrorReturn(throwable -> -1L)

         ).subscribe(
                 data -> log.info("----> {}", data),
                 error -> log.info("error occurred", error),
                 () -> log.info("complete")
         );
     }
 }
