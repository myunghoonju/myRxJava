package practice.tcp.netty.sample.server.event.action;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyHttpHandler extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) {
        ch.pipeline().addLast(new HttpClientCodec());
    }
}
