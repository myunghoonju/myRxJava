package practice.tcp.old;

import java.io.*;
import java.net.*;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnDeServer {
    private int port; // 서버포트
    private String key; // 키
    private String company; // 기관명
    private String inpCond; // 수취조건 1:무조건, 0:vacs_vact select
    private Logger logger; // 로그처리용
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
            logger = LoggerFactory.getLogger(EnDeServer.class);

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

            logger.info("전문수신 PORT : " + serverSocket.getLocalPort());

            while (true) {
                Socket socket = serverSocket.accept();
                OldServerSocket enDeServerThread = new OldServerSocket(socket, key, company, inpCond, enc, DB_INFO, DB_URL, DB_ID, DB_PWD);

                enDeServerThread.start();
            }
        } catch (IOException ioe){
            logger.info(ioe.getMessage());
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

        try {
            // 환경변수 로딩
            String configFile = System.getProperty("corpInfoFile");
            FileInputStream input = new FileInputStream(configFile);
            Properties prop = new Properties();
            prop.load(input);

            int server_port = Integer.parseInt(prop.getProperty("server_port"));

            String key = prop.getProperty("key");
            String company = new String(prop.getProperty("company").getBytes(
                    "8859_1"), "KSC5601");// (2)
            String inpCond = prop.getProperty("inpCond"); // 1:무조건 입금,
            // 0:vacs_vact
            String DB_INFO = prop.getProperty("DB_INFO"); /** 디비 사용 (Oracle, Mysql,Mssql2000, Mssql2005 이상)	 */
            String DB_URL = "";
            String DB_IP_PORT = prop.getProperty("DB_IP_PORT");
            String DB_NAME = prop.getProperty("DB_NAME");

            if (DB_INFO.equalsIgnoreCase("Oracle")) {
                DB_URL = "jdbc:oracle:thin:@" + DB_IP_PORT + ":" + DB_NAME;
            } else if (DB_INFO.equalsIgnoreCase("Mysql")) {
                //DB_URL = "jdbc:mysql://" + DB_IP_PORT + "/" + DB_NAME;
                //DB_URL = "jdbc:mysql://" + DB_IP_PORT + "/" + DB_NAME + "?characterEncoding=euckr";
                DB_URL = "jdbc:mysql://" + DB_IP_PORT + "/" + DB_NAME + "?useUnicode=true&characterEncoding=utf8";
            } else if (DB_INFO.equalsIgnoreCase("Mssql2000")) {
                DB_URL = "jdbc:microsoft:sqlserver://" + DB_IP_PORT
                        + ";SelectMethod=cursor;DatabaseName=" + DB_NAME;
            } else if (DB_INFO.equalsIgnoreCase("Mssql2005")) {
                DB_URL = "jdbc:sqlserver://" + DB_IP_PORT
                        + ";SelectMethod=cursor;DatabaseName=" + DB_NAME;
            }else if (DB_INFO.equalsIgnoreCase("PostgreSQL")) {
                DB_URL = "jdbc:postgresql://" + DB_IP_PORT+ "/" + DB_NAME;
            }
            else if (DB_INFO.equalsIgnoreCase("Firebird")) {
                DB_URL = "jdbc:firebirdsql:" + DB_IP_PORT + ":" + DB_NAME;
            } else {
                System.out.println("디비 정보가 맞지 않습니다.");
                System.exit(1);
            }

            System.out.println(DB_URL);
            String DB_ID = prop.getProperty("DB_ID");
            String DB_PWD = prop.getProperty("DB_PWD");
            String enc = prop.getProperty("enc");
            // log file
            //PropertyConfigurator.configure(prop.getProperty("logInfoFile"));

            EnDeServer enDeServer = new EnDeServer(server_port, key, company,
                    inpCond, enc, DB_INFO, DB_URL, DB_ID, DB_PWD);

        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }
}

