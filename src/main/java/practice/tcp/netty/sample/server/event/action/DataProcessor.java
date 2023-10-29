package practice.tcp.netty.sample.server.event.action;

import java.util.HashMap;

import org.springframework.scheduling.annotation.Async;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import practice.okhttp.okhttp.MockApiService;
import practice.okhttp.okhttp.RetrofitService;
import practice.okhttp.okhttp.TestModel;
import practice.tcp.netty.sample.model.ResData;

@Slf4j
public class DataProcessor extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //String string = (String) msg;
        String string = null;
        if (string == null) {
            System.out.println("channelRead release");
            ReferenceCountUtil.release(msg);
            return;
        }

        ResData resData = ResData.builder().anInt(2).build();
        test();
        ChannelFuture future = ctx.writeAndFlush(resData);
        future.addListener(ChannelFutureListener.CLOSE);
        System.out.println("DataProcessor channelRead  " + string);
    }

    public void test() throws Exception {
        TestModel testModel = TestModel.builder().key("a").val("111").build();
        MockApiService mockApiService = RetrofitService.getCli("http://localhost:8085")
                                                       .create(MockApiService.class);

        mockApiService.mockPost(new HashMap<>(), testModel).execute();
    }

    public static TestModel test2() {
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

    public static TestModel test3() {
        try {
            TestModel testModel = TestModel.builder().key("b").val("222").build();
            MockApiService mockApiService = RetrofitService.getCli("http://localhost:8085")
                                                           .create(MockApiService.class);

            mockApiService.mockPost(new HashMap<>(), testModel).execute().body();
            return null;
        } catch (Exception e) {
            //
        }

        return null;
    }

    public TestModel test4() {
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


    public TestModel test5() {
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
