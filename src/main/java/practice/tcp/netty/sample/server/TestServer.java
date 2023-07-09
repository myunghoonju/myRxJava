package practice.tcp.netty.sample.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import practice.tcp.netty.sample.server.event.TestChannelHandler;

public class TestServer {

    public void run() throws Exception {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            System.out.println("SERVER RUNNING");
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new TestChannelHandler())
                    //TODO option 확인
                    .option(ChannelOption.SO_BACKLOG, 5)
                    .option(ChannelOption.SO_LINGER, 0) // no wait time
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture sync = serverBootstrap.bind(8080).sync();
            sync.channel().closeFuture().sync();

        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
