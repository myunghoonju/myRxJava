package practice.tcp.netty.sample.server.event;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import practice.tcp.netty.sample.server.event.action.*;

public class TestChannelHandler extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) {
        ch.pipeline()
          .addLast(new RequestDecoder(),
                   new ResponseEncoder(),
                   new DataProcessor());
    }
}
