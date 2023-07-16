package practice.tcp.netty.sample.server.event.action;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import practice.okhttp.okhttp.MockApiService;
import practice.okhttp.okhttp.RetrofitService;
import practice.okhttp.okhttp.TestModel;
import practice.tcp.netty.sample.model.ResData;

import java.util.HashMap;

@Slf4j
public class DataProcessor extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String string = (String) msg;
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
}
