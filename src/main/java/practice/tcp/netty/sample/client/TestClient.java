package practice.tcp.netty.sample.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import practice.tcp.netty.sample.client.event.TestClientChannelHandler;

public class TestClient {

    public void run() throws Exception {
        NioEventLoopGroup worker = new NioEventLoopGroup();

        try {
            System.out.println("CLIENT RUN");
            Bootstrap clientBootStrap = new Bootstrap();
            clientBootStrap.group(worker)
                           .channel(NioSocketChannel.class)
                           .option(ChannelOption.SO_KEEPALIVE, true)
                           .handler(new TestClientChannelHandler());

            ChannelFuture future = clientBootStrap.connect("localhost", 8090).sync();
            future.channel().closeFuture().sync();
        } finally {
            worker.shutdownGracefully();
        }
    }
}
