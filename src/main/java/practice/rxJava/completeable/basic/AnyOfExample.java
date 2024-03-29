package practice.rxJava.completeable.basic;

import java.util.concurrent.CompletableFuture;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AnyOfExample {

    public static void main(String[] args) {
        CompletableFuture<Integer> first = CompletableFuture.supplyAsync(() -> 3 / 0) // throws error
                                                             .handle((result, err) -> {
                                                                                        if (err != null) {
                                                                                            return 0; // always return it
                                                                                        }
                                                                                        return result;
                                                                                       });

        CompletableFuture<Integer> second = CompletableFuture.supplyAsync(() -> {
                                                                                 try {
                                                                                     System.err.println("start second");
                                                                                     Thread.sleep(2000L);
                                                                                 } catch (Exception e) {
                                                                                     log.error("error ", e);
                                                                                 }
                                                                                 System.err.println("about to finish second"); // can not be reached
                                                                                 return 20;
                                                                                });

        CompletableFuture<Object> any = CompletableFuture.anyOf(first, second);
        Integer result = any.thenApply(fastest -> {
            int i = (Integer) fastest;
            return i == 0 ? 1 : i * 2;
        }).join();// wait until the fastest task finished, unchecked exception related, so no need to wrap it with try-catch

        System.err.println("expected result: 1, actual -> " + result);
    }
}
