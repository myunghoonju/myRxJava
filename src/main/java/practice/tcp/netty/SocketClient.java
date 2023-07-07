package practice.tcp.netty;

import java.io.*;
import java.net.Socket;

public class SocketClient {

    public static void main(String[] args) {
        SocketClient socketClient = new SocketClient();
        socketClient.run();
    }

    public void run() {
        startConnection("localhost", 8080);
        System.out.println(sendMessage());
        stopConnection();
    }

    private Socket clientSocket;
    private BufferedWriter out;
    private BufferedReader in;

    public void startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public String sendMessage() {
        try {
            String s = "0200 20000010 1000 0200905140954061100000720000011    0026938       B    10810064145031  (주)세틀뱅크        1177730000000010690홍길동              0000000000000                             011  ";
            System.out.println("send length " + s.getBytes().length);
            out.write(s);
            out.flush();
         return s;
        } catch (Exception e) {
            return null;
        }
    }

    public void stopConnection() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
}
