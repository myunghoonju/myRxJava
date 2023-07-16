package practice.tcp.netty.sample.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import practice.tcp.netty.sample.server.event.TestChannelHandler;

public class TestServer {

    public void run() {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(1);
        try {
            System.out.println("SERVER RUNNING");
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new TestChannelHandler())
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture sync = serverBootstrap.bind(8090)
                    .addListener(new ConnectionListener())
                    .sync();
            sync.channel().closeFuture().sync();
        }catch (Exception e) {
            System.out.println("SERVER RUNNING FAILED");
        } finally {
            Future<?> workerClose = workerGroup.shutdownGracefully();
            Future<?> bossClose = bossGroup.shutdownGracefully();
            workerClose.syncUninterruptibly();
            bossClose.syncUninterruptibly();
        }
    }
}
