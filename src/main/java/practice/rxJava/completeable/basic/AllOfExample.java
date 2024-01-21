package practice.rxJava.completeable.basic;

import java.util.concurrent.CompletableFuture;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AllOfExample {

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

        CompletableFuture<Void> voidCf = CompletableFuture.allOf(first, second);
        CompletableFuture<Integer> finalCf = voidCf.thenApply(v -> {
            Integer join = first.join();
            Integer join2 = second.join();

            return 3 + join + join2;
        });

        Integer join = finalCf.join();

        System.err.println(join);
    }
}
