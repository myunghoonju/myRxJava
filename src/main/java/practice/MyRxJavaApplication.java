package practice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import practice.tcp.netty.sample.server.TestServer;

@SpringBootApplication
public class MyRxJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyRxJavaApplication.class, args);
		TestServer testServer = new TestServer();
		testServer.run();
	}

}
