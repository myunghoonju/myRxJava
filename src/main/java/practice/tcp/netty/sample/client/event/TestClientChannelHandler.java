package practice.tcp.netty.sample.client.event;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import practice.tcp.netty.sample.client.event.action.ClientDataProcessor;
import practice.tcp.netty.sample.client.event.action.RequestDataEncoder;
import practice.tcp.netty.sample.client.event.action.ResponseDecoder;

public class TestClientChannelHandler extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
          .addLast(new RequestDataEncoder(),
                   new ResponseDecoder(),
                   new ClientDataProcessor());
    }
}
