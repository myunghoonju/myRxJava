package practice.tcp.netty.sample.client.event.action;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import practice.tcp.netty.sample.model.ReqData;

import java.nio.charset.StandardCharsets;

public class RequestDataEncoder extends MessageToByteEncoder<ReqData> {

    @Override
    protected void encode(ChannelHandlerContext ctx,
                          ReqData msg,
                          ByteBuf out) throws Exception {
        out.writeInt(msg.getAnInt());
        out.writeInt(msg.getString().length());
        out.writeCharSequence(msg.getString(), StandardCharsets.UTF_8);
        System.out.println("RequestDataEncoder send data " + msg);
    }
}
