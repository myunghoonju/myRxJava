package practice.tcp.netty;

import practice.tcp.netty.sample.server.TestServer;

public class NettyServerApplication {

    public static void main(String[] args) throws Exception {
        TestServer testServer = new TestServer();
        testServer.run();
    }
}
