package practice.tcp.old;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class EnDeServer {
    private int port; // 서버포트
    private String key; // 키
    private String company; // 기관명
    private String inpCond; // 수취조건 1:무조건, 0:vacs_vact select
    private String DB_INFO;
    private String DB_URL;
    private String DB_ID;
    private String DB_PWD;
    private String enc;

    /**
     * 생성자
     */
    public EnDeServer(int port, String key, String company, String inpCond, String enc, String DB_INFO, String DB_URL, String DB_ID, String DB_PWD) {
        try {

            this.port = port;
            this.key = key;
            this.company	=	company;
            this.inpCond	=	inpCond;
            this.DB_INFO = DB_INFO;
            this.DB_URL = DB_URL;
            this.DB_ID = DB_ID;
            this.DB_PWD = DB_PWD;
            this.enc	=	enc;

            ServerSocket serverSocket = new ServerSocket(port);

            log.info("전문수신 PORT : " + serverSocket.getLocalPort());

            while (true) {
                Socket socket = serverSocket.accept();
                OldServerSocket enDeServerThread = new OldServerSocket(socket, key, company, inpCond, enc, DB_INFO, DB_URL, DB_ID, DB_PWD);

                enDeServerThread.start();
            }
        } catch (IOException ioe){
            log.info(ioe.getMessage());
        }
    }

    /**
     * 사용법 설명
     */
    public static void usage() {
        System.out.println("java EnDeServer [port] [key]");
        System.exit(1);
    }

    /**
     * 실행
     * @param args	커맨드라인 인자
     */
    public static void main (String args[]) {
            String DB_ID = "";
            String DB_PWD = "";
            String enc = "0";

            EnDeServer enDeServer = new EnDeServer(8080,
                                                   "12345678",
                                                   "myCompany",
                                                   "1",
                                                   enc,
                                                   "",
                                                   "",
                                                   "",
                                                   "");

    }
}

