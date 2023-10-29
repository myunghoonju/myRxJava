package practice.web;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;
import practice.okhttp.okhttp.TestModel;
import practice.tcp.netty.sample.server.event.action.DataProcessor;

@Slf4j
class WebServiceTest {

    @Test
    void future1() throws Exception {
        CompletableFuture<String> res = CompletableFuture.supplyAsync(() -> "Hell");
        log.info("result {}", res.get());
    }

    @Test
    void future2() throws Exception {
        CompletableFuture<String> res = CompletableFuture.supplyAsync(() -> "Hell");
        CompletableFuture<String> res2 = res.thenApply(s -> s + "_no");

        log.info("result {}", res2.get());
    }
    @Test
    void future3() throws Exception {
        CompletableFuture<String> res = CompletableFuture.supplyAsync(() -> "Hell")
                                                         .thenCompose(s -> CompletableFuture.supplyAsync(() -> s + "_no!"));
        log.info("result {}", res.get());
    }

    @Test
    void future4() throws Exception {
        CompletableFuture<String> res = CompletableFuture.supplyAsync(() -> "Hell")
                                                         .thenCombine(CompletableFuture.supplyAsync(() -> "_no!!!"), (s1, s2) -> s1 + s2);
        log.info("result {}", res.get());
    }

    @Test
    void future5_multiple() {
        List<TestModel> collect = Stream.of(m1(), m2()).map(CompletableFuture::join).filter(Objects::nonNull).collect(Collectors.toList());
        for (TestModel t : collect) {
            System.out.println(t.getKey());
            System.out.println(t.getVal());
        }
    }

    private CompletableFuture<TestModel> m1() {
       return CompletableFuture.supplyAsync(DataProcessor::test2);
    }

    private CompletableFuture<TestModel> m2() {
       return CompletableFuture.supplyAsync(DataProcessor::test3);
    }
}