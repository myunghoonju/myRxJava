package practice.tcp.netty.sample.server.event.action;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import practice.tcp.netty.sample.model.ReqData;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class RequestDecoder extends ReplayingDecoder<ReqData> {

    @Override
    protected void decode(ChannelHandlerContext ctx,
                          ByteBuf in,
                          List<Object> out) throws Exception {
        int value = in.readInt();
        String string = in.readCharSequence(in.readInt(), StandardCharsets.UTF_8).toString();
        ReqData data = ReqData.builder()
                              .anInt(value)
                              .string(string)
                              .build();

        out.add(data);
    }
}
