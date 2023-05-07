package practice.tcp.netty;

import practice.tcp.netty.sample.client.TestClient;

public class NettyClientApplication {

    public static void main(String[] args) throws Exception {
        TestClient testClient = new TestClient();
        testClient.run();
    }
}
