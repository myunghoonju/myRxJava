package practice.tcp.netty.sample.client.event.action;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import practice.tcp.netty.sample.model.ResData;

import java.util.List;

public class ResponseDecoder extends ReplayingDecoder<ResData> {

    @Override
    protected void decode(ChannelHandlerContext ctx,
                          ByteBuf in,
                          List<Object> out) throws Exception {
        int anInt = in.readInt();
        ResData res = ResData.builder()
                             .anInt(anInt)
                             .build();
        out.add(res);
    }
}
