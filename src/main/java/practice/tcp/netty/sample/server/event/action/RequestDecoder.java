package practice.tcp.netty.sample.server.event.action;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class RequestDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx,
                          ByteBuf in,
                          List<Object> out) throws Exception {
        System.out.println("first isReadable " + in.isReadable());
        int contentLength = in.readableBytes();
        int capacity = in.capacity();
        int writerIndex = in.writerIndex();
        boolean direct = in.isDirect();
        System.out.println("contentLength " + contentLength + " capacity " + capacity + " writerIndex " + writerIndex + " isDirect " + direct);
        String string = in.toString(StandardCharsets.UTF_8);
        in.readerIndex(contentLength);

        System.out.println("last isReadable " + in.isReadable());
        out.add(string);
    }
}
