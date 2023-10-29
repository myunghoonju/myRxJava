package practice.web;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import practice.okhttp.okhttp.MockApiService;
import practice.okhttp.okhttp.RetrofitService;
import practice.okhttp.okhttp.TestModel;
import practice.tcp.netty.sample.server.event.action.DataProcessor;

@Service
public class WebService {

    public List<TestModel> myBoot() {
        return Stream.of(m1(), m2())
                     .map(CompletableFuture::join)
                     .filter(Objects::nonNull)
                     .collect(Collectors.toList());
    }

    public List<TestModel> myBoot2() {
        return Stream.of(m3(), m4()).map(TestModel::of).collect(Collectors.toList());
    }

    public CompletableFuture<TestModel> m1() {
        DataProcessor dataProcessor = new DataProcessor();
        return CompletableFuture.supplyAsync(dataProcessor::test4);
    }

    public CompletableFuture<TestModel> m2() {
        DataProcessor dataProcessor = new DataProcessor();
        return CompletableFuture.supplyAsync(dataProcessor::test5);
    }

    public CompletableFuture<TestModel> m3() {
        return CompletableFuture.completedFuture(test6());
    }


    public CompletableFuture<TestModel> m4() {
        return CompletableFuture.completedFuture(test7());
    }

    @Async(value = "asyncExecutor")
    public TestModel test6() {
        try {
            TestModel testModel = TestModel.builder().key("a").val("111").build();
            MockApiService mockApiService = RetrofitService.getCli("http://localhost:8085")
                                                           .create(MockApiService.class);

            return mockApiService.mockPost(new HashMap<>(), testModel).execute().body();
        } catch (Exception e) {
            //
        }

        return null;
    }


    @Async(value = "asyncExecutor")
    public TestModel test7() {
        try {
            TestModel testModel = TestModel.builder().key("b").val("222").build();
            MockApiService mockApiService = RetrofitService.getCli("http://localhost:8085")
                                                           .create(MockApiService.class);

            return mockApiService.mockPost(new HashMap<>(), testModel).execute().body();
        } catch (Exception e) {
            //
        }

        return null;
    }
}
