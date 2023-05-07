package practice.tcp.netty.sample.client.event.action;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import practice.tcp.netty.sample.model.ReqData;
import practice.tcp.netty.sample.model.ResData;

public class ClientDataProcessor extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ReqData testValue = ReqData.builder()
                                   .anInt(123)
                                   .string("test value")
                                   .build();

        ctx.writeAndFlush(testValue);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx,
                            Object msg) throws Exception {
        ResData resData = (ResData) msg;
        System.out.println("ClientDataProcessor channelRead -> " + resData.getAnInt());
        ctx.close();
    }
}
