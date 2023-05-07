package practice.tcp.netty.sample.server.event.action;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import practice.tcp.netty.sample.model.ResData;

public class ResponseEncoder extends MessageToByteEncoder<ResData> {

    @Override
    protected void encode(ChannelHandlerContext ctx,
                          ResData msg,
                          ByteBuf out) throws Exception {
        System.out.println("ResponseEncoder " + msg.getAnInt());
        out.writeInt(msg.getAnInt());
    }
}
