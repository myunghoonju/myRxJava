package practice.tcp.netty;

import practice.tcp.netty.sample.client.TestClient;

public class NettyClientApplication {

    public static void main(String[] args) throws Exception {
        TestClient testClient = new TestClient();
        for (int i = 0; i < 10; i++) {
            testClient.run();
        }
    }
}
