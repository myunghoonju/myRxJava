package practice.rxJava.completeable.basic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AllOfExample2 {

    public static void main(String[] args) {
        List<Integer> loop = new ArrayList<>();
        loop.add(1);
        loop.add(1);
        loop.add(1);
        long started = System.currentTimeMillis();
        List<List<Integer>> nums = loop.parallelStream().map(v -> methodA())
                                      .map(cf -> {
                                                    CompletableFuture<Void> vcf = CompletableFuture.allOf(cf);
                                                    CompletableFuture<List<Integer>> result = vcf.thenApply(vc -> methodB(cf));

                                                    return result.join();
                                                  })
                                      .collect(Collectors.toList());

        nums.forEach(System.err::println);
        System.err.println("time spent " + (System.currentTimeMillis() - started));
    }

    private static CompletableFuture<Integer> methodA() {
        CompletableFuture<Integer> second = CompletableFuture.supplyAsync(() -> {
            int asInt = new Random().ints(1000, 2000).findFirst().getAsInt();
            try {
                System.err.println("start methodA" + asInt);

                Thread.sleep(asInt);
            } catch (Exception e) {
                log.error("error ", e);
            }
            System.err.println("about to finish methodA" + asInt);
            return asInt;
        });

        return second;
    }

    private static List<Integer> methodB(CompletableFuture<Integer> methodA) {
        List<Integer> res = new ArrayList<>();
        res.add(methodA.join());

        return res;
    }
}
