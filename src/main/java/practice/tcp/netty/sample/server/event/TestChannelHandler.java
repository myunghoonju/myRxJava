package practice.tcp.netty.sample.server.event;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import practice.tcp.netty.sample.server.event.action.DataProcessor;
import practice.tcp.netty.sample.server.event.action.RequestDecoder;
import practice.tcp.netty.sample.server.event.action.ResponseEncoder;

public class TestChannelHandler extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
          .addLast(new RequestDecoder(),
                   new ResponseEncoder(),
                   new DataProcessor());
    }
}
