package practice.rxJava.completeable.basic;

import java.util.concurrent.CompletableFuture;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AnyOfExample {

    public static void main(String[] args) {
        CompletableFuture<Integer> second = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000L);
            } catch (Exception e) {
                log.error("error ", e);
            }

            return 10;
        });

        CompletableFuture<Integer> first = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000L);
            } catch (Exception e) {
                log.error("error ", e);
            }

            return 20;
        });
        CompletableFuture<Object> any = CompletableFuture.anyOf(second, first);

        any.thenAccept(fastest -> {
            Integer fastest1 = (Integer) fastest;
            System.err.println(fastest1);
        }).join(); // wait until the fastest task finished
    }
}
