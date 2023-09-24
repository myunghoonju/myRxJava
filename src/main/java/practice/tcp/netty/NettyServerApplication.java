package practice.tcp.netty;

import io.netty.util.ResourceLeakDetector;
import practice.tcp.netty.sample.server.TestServer;

public class NettyServerApplication {

    public static void main(String[] args) throws Exception {
        TestServer testServer = new TestServer();
        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.ADVANCED);
        testServer.run();
    }
}
