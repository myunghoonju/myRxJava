package practice.tcp.netty.sample.server;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConnectionListener implements ChannelFutureListener {

    @Override
    public void operationComplete(ChannelFuture future) {
        if (future.isSuccess()) {
            log.info("server bound");
        } else {
            log.info("fail to attempt to bind ", future.cause());
        }
    }
}
