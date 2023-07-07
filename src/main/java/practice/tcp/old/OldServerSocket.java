package practice.tcp.old;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;

@Slf4j
public class OldServerSocket extends Thread {

    private Socket recSocket = null;
    private InputStream input = null;
    private InputStreamReader isr = null;
    private OutputStream output;
    private String enc;
    private String key; // 암호화 key.
    private String enData; // 암호화 string.
    private String deData; // 복호화 string.
    private BufferedInputStream br = null;
    // private BufferedInputStream bis = null;
    private int LEN_SIZE = 4;
    private int MAX_SIZE = 4096;
    private String FILE_DIR = "../data";
    // private String company = "세틀뱅크"; //기관명
    private String corpId = ""; // 기관코드
    private String company = "";
    private String inpCond = "";
    String fst_il = ""; // 첫 거래시작일
    String lst_il = ""; // 마지막 거래일
    private String DB_URL = "";
    private String DB_ID = "";
    private String DB_PWD = "";
    private String DB_INFO = "";

    public OldServerSocket(Socket rec_socket, String key, String company, String inpCond, String enc, String DB_INFO, String DB_URL, String DB_ID, String DB_PWD) {
        this.key = key;
        this.recSocket = rec_socket;
        this.company = company;
        this.inpCond = inpCond;
        this.DB_URL = DB_URL;
        this.DB_ID = DB_ID;
        this.DB_PWD = DB_PWD;
        this.DB_INFO = DB_INFO;
        this.enc = enc;

        try {
            input = recSocket.getInputStream();
            output = recSocket.getOutputStream();

            //isr = new InputStreamReader(input);
            br = new BufferedInputStream(input);

        } catch (IOException ioe) {
            log.info("error_io (EnDeServerThread.java)");
        }
    }

    /**
     * 스레드 실행
     */
    public void run() {
        log.info("Thread Start");

        String sendData = ""; // 보낼 메세지
        int rc = 0;

        //deData에 받은 메세지 저장
        rc = receive(); // 클라이언트로부터 데이터를 받은 메소드

        if (rc < 0) {
            log.info("Socket Read Error("+rc+")");
            CloseSocket();
            return;
        }
        sendData = tran_gb_do(); // 거래구분 처리

        send(sendData); // client send..

        CloseSocket();
        log.info("Thread End\n");
    }

    /**
     * 클라이언트로부터 데이터를 받은 메소드
     *
     */
    public int receive() {

        InetAddress inet =  recSocket.getInetAddress();
        String ip = String.valueOf(inet);

        if (log.isInfoEnabled()){
            log.info("접속 아이피 : " + ip);
        }

        int bytesRead = 0;
        String str = null;
        byte[] bRecvLen = new byte[LEN_SIZE];

        int mlen = 0;


        try {
            //bytesRead = Read(br, bRecvLen, LEN_SIZE);
            //str = String.valueOf(bRecvLen);
            str = "02002000001010000200905140954061100000720000011    0026938       B    10810064145031  (주)세틀뱅크        1177730000000010690홍길동              0000000000000                             011  ";

            //logger.info("Read = '" + str + "'");
            mlen = Integer.parseInt(str.substring(0, 4)); // 전문길이

            byte[] bRecv = new byte[mlen];


			if (enc.equals("0"))
				mlen -= LEN_SIZE;

        /*    if ((bytesRead = Read(br, bRecv, mlen)) < 0)
            {
                log.info("Read = '" + bytesRead + "'");
                return bytesRead;
            }*/
            //str = String.valueOf(bRecv);
            //str = new String(bRecv);

            byte buffer[] = str.getBytes();

            if (enc.equals("1")) { // 암호화
                setEnData(new String(buffer));
                byte deDataByte[] = seedDecrypt(base64Decoding(buffer), key); // Decoding..
                setDeData(new String(deDataByte));
            } else { // 비암호화
                deData = str;
            }
            corpId = deData.substring(4, 12); // 기관코드
            log.info("deData(" + mlen + ") = '" + deData + "'");
        } catch (NumberFormatException ex) {
            log.info(ex.getMessage());
            deData = null;
        }


        return bytesRead;
    }

    /**
     * 소켓 Read
     */
    public int Read(BufferedInputStream br, byte[] bRecv, int len) {

        int iLen = 0;
        int iLeft = len;

        if (br == null)
            return -1;

        try {

            while (iLeft < 0) {
                iLen = br.read(bRecv,  len - iLeft, iLeft);

                if (iLen >= 0)
                {
                    iLeft -= iLen;
                }
                else
                    return -1;
            }
        } catch (IOException ioe) {
            log.info("receive error = " + ioe.getMessage());
        }

        return 0;
    }

    /**
     * 소켓 종료
     */
    public void CloseSocket() {
        try {
            if(isr !=null )isr.close();
            if (br != null) br.close();
            recSocket.close();
        } catch (IOException ioe) {
            log.info("error_io (EnDeServerThread.java)");
        }
    }

    /**
     * 거래구분 처리 return 200byte
     */
    private String tran_gb_do() {

        int k = 0;
        byte[] strBytes = null;
        StringBuffer sbf = null;
        String sendData = null;
        String space = "";
        byte[] etemp = new byte[94];
        String newTemp = null;

        DBConnectionManager dbcm = null;
        Connection conn = null;

        try {
            int recstrLen = Integer.parseInt(deData.substring(0, 4).trim()); // 전문길이

            String orgCd = deData.substring(4,12).trim(); //기관코드
            // 전문공통부
            String tranGubun = deData.substring(12, 16).trim(); // 거래코드
            String tranDate = deData.substring(17, 25).trim(); // 거래일자
            String tranTime = deData.substring(25, 31).trim(); // 거래시간
            String tranNumber = deData.substring(33, 40).trim(); // 거래번호
            String inputBankCode = deData.substring(44, 47).trim(); // 입금은행코드


            String inpRet = "";
            String trIlSi = tranDate + tranTime;

            // 전문개별부
            String iorgCd = "";
            long amount = 0;
            long ta_amount = 0;         // 거래금액
            String virtualAccount = ""; // 계좌번호
            String snNm = "";           // 입금의뢰인명
            String cmsCd = "";
            String mediaGb = "";        // 매체구분

            // 회사이름 자리수 보정
            company = companyLen(company);

            if (recstrLen == 200) {

                // 전문개별부
                virtualAccount = deData.substring(70, 86).trim(); // 가상계좌
                String tempStr = deData.substring(86, deData.length());
                strBytes = tempStr.getBytes(); // 바이트 처리(한글)

                byte temp[] = new byte[13];
                byte temp2[] = new byte[20];
                byte temp3[] = new byte[15];
                byte temp4[] = new byte[3];
                byte temp5[] = new byte[2];   //매체구분용
                byte tempAcct[] = new byte[16];
                byte virtualAccountBytes[] = virtualAccount.getBytes();
                if (virtualAccountBytes.length <= 16) {
                    int i = 0;
                    for (i = 0; i < virtualAccountBytes.length; i++) {

                        tempAcct[i] = virtualAccountBytes[i];

                    }

                    while (i < tempAcct.length) {

                        tempAcct[i++] = (byte) ' ';

                    }
                }
                virtualAccount = new String(tempAcct);

                // 거래금액
                k = 0;
                for (int i = 26; i < 39; i++) {
                    temp[k] = strBytes[i];
                    k++;
                }
                amount = Long.parseLong(new String(temp));

                // 입금의뢰인명
                k = 0;
                for (int i = 39; i < 59; i++) {
                    temp2[k] = strBytes[i];
                    k++;
                }
                snNm = new String(temp2);

                // 수표금액
                k = 0;
                for (int i = 59; i < 72; i++) {

                    temp[k] = strBytes[i];
                    k++;
                }
                String ta_amount_temp = new String(temp);

                if (ta_amount_temp.equals("             ")) {
                    ta_amount_temp = "0000000000000";
                }

                ta_amount = Long.parseLong(ta_amount_temp);

                // cms_cd
                k = 0;
                for (int i = 72; i < 87; i++) {
                    temp3[k] = strBytes[i];
                    k++;
                }
                cmsCd = new String(temp3);

                // iorg_cd 입금은행코드
                k = 0;
                for (int i = 101; i < 104; i++) {
                    temp4[k] = strBytes[i];
                    k++;
                }
                iorgCd = new String(temp4);

                // media_gb 매체구분
                k = 0;
                for (int i = 112; i < 114; i++) {
                    temp5[k] = strBytes[i];
                    k++;
                }
                mediaGb = new String(temp5);

            }// 파싱 끝  응답전문 생성


            log.info("in db process amount=>" + amount + ",vc=>" + virtualAccount);
            log.info("in db process number=>" + deData);
            // 응답전문 생성 시작
            sbf = new StringBuffer();

            sbf.append(deData.substring(0, 13));
            sbf.append("1"); // 거래종류코드
            sbf.append(deData.substring(14, 16));
            sbf.append("3"); // 송수신코드
            sbf.append(deData.substring(17, 40));
            System.out.println("tranGubun:" + tranGubun);

            if ("2000".equals(tranGubun)) {
                /*
                 * 수취조회******************************************************
                 * 계좌체크나 금액체크를 하실 경우, 수취조회 전문 송신시 해당계좌를 관리하는 테이블 조회후 결과를 SET하는
                 * 로직 추가...
                 * ******************************************************
                 */

                log.info("수취 조회 시작 ");
                String inp_st = chkCancel(tranDate, tranNumber, virtualAccount,
                        conn, dbcm); // 0;없음, 1:입금, 2:취소, 3:정산(취소불가)

                if (inp_st.equals("2")) { // 취소된 거래인지 확인 : 이미 취소된 거래인 경우
                    inpRet = "V785";
                } else {

                    if (inpCond.equals("0")) { // 조건 확인

                        inpRet = checkVact(virtualAccount, inputBankCode,
                                trIlSi, amount, tranDate, tranNumber, tranTime,
                                conn, dbcm);

                    } else { // 무조건 정상

                        inpRet = "0000";
                    }
                    log.info("수취 조회 종료 ");
                }
                sbf.append(inpRet);
                sbf.append(deData.substring(44, 86));
                sbf.append(company); /* 기관명 SET : 20byte */

                k = 0;
                for (int i = 20; i < 114; i++) {
                    etemp[k] = strBytes[i];
                    k++;
                }
                newTemp = new String(etemp);
                sbf.append(newTemp);
                sendData = sbf.toString();

            } else if ("1001".equals(tranGubun) || "1000".equals(tranGubun)
                    || "1003".equals(tranGubun) || "3000".equals(tranGubun)
                    || "3001".equals(tranGubun) || "3003".equals(tranGubun)) {
                /**
                 * 입금처리
                 */

                /**
                 * 중복요청이면 정상응답으로 SET
                 *
                 * @전문KEY= 기준일자+거래번호
                 */

                // 입금 완료된 거래인지 확인
                log.info("입금여부 조회 시작 ");
                String inp_st = chkCancel(tranDate, tranNumber, virtualAccount,
                        conn, dbcm); // 0;없음, 1:입금, 2:취소, 3:정산(취소불가)

                if (inp_st.equals("1") || inp_st.equals("3")) { // 입금 또는 정산상태
                    // 입금완료된 거래
                    sbf.append("0000");
                    log.info("중복입금...정상응답");
                } else if (inp_st.equals("2")) { // 취소상태
                    // 취소완료된 거래
                    sbf.append("V785");
                    log.info("취소완료된 거래 재입금...오류");
                } else { // 신규입금
                    /**
                     * 입금거래내역 INSERT
                     */
                    log.info("입금처리 시작 ");
                    //매체구분추가
                    boolean result = insAhst(tranDate, tranTime, tranGubun,
                            inputBankCode, virtualAccount, snNm, amount,
                            ta_amount, tranNumber, iorgCd, cmsCd, mediaGb, "1", conn,
                            dbcm);
                    if (result == true) {
                        sbf.append("0000");
                        log.info("입금처리 완료 ");
                    } else {
                        sbf.append("V141");
                        log.info("입금처리 오류");
                    }

                }
                sbf.append(deData.substring(44, 86));
                sbf.append(company); /* 기관명 SET : 20byte */

                k = 0;
                for (int i = 20; i < 114; i++) {
                    etemp[k] = strBytes[i];
                    k++;
                }
                newTemp = new String(etemp);
                sbf.append(newTemp);
                sendData = sbf.toString();

            } else if ("1010".equals(tranGubun) || "1011".equals(tranGubun)
                    || "1013".equals(tranGubun) || "3010".equals(tranGubun)
                    || "3011".equals(tranGubun) || "3013".equals(tranGubun)) {
                /**
                 * 입금취소 처리
                 */
                log.info("입금 완료 조회 시작");
                // 입금 완료된 거래인지 확인
                String inp_st = chkCancel(tranDate, tranNumber, virtualAccount,
                        conn, dbcm); // 0;없음, 1:입금, 2:취소, 3:정산(취소불가)

                if (inp_st.equals("1")) { // 취소가능한 거래
                    boolean result = canAhst(tranDate, tranTime, tranNumber,
                            virtualAccount, inputBankCode, amount, conn, dbcm);
                    if (result == true) {
                        sbf.append("0000");
                        log.info("취소처리 완료");
                    } else {
                        sbf.append("V141");
                        log.info("취소처리 오류");
                    }
                } else if (inp_st.equals("2")) { // 기취소된 거래 - 정상응답
                    sbf.append("0000");
                    log.info("기취소된 거래");
                } else if (inp_st.equals("3")) { // 이미 업무로직을 타서 취소 불가능한 상태
                    sbf.append("V637");
                    errlog_insert(tranDate, tranNumber, tranTime,
                            inputBankCode, virtualAccount, amount, "V637",
                            conn, dbcm);
                    log.info("취소불가 거래");
                } else {

                    boolean result = insAhst(tranDate, tranTime, tranGubun,
                            inputBankCode, virtualAccount, snNm, amount,
                            ta_amount, tranNumber, iorgCd, cmsCd, mediaGb, "2", conn,
                            dbcm);
                    if (result == true) {
                        sbf.append("V601");
                        errlog_insert(tranDate, tranNumber, tranTime,
                                inputBankCode, virtualAccount, amount, "V601",
                                conn, dbcm);
                        log.info("원거래없음->vacs_ahst 취소 insert");
                    } else {
                        sbf.append("V141");
                        log.info("원거래 없음 -> 취소 insert 오류 ");
                    }
                }

                sbf.append(deData.substring(44, 86));
                sbf.append(company); /* 기관명 SET : 20byte */

                k = 0;
                for (int i = 20; i < 114; i++) {
                    etemp[k] = strBytes[i];
                    k++;
                }
                newTemp = new String(etemp);
                sbf.append(newTemp);
                sendData = sbf.toString();

            } else if ("7000".equals(tranGubun)) { // 세틀뱅크 일괄 집계 처리

                int norInpCnt = Integer.parseInt(deData.substring(70, 77));
                System.out.println("norInpCnt:" + norInpCnt);
                long norInpAmt = Long.parseLong(deData.substring(77, 90));
                System.out.println("norInpAmt:" + norInpAmt);
                int canInpCnt = Integer.parseInt(deData.substring(90, 97));
                System.out.println("canInpCnt:" + canInpCnt);
                long canInpAmt = Long.parseLong(deData.substring(97, 110));
                System.out.println("canInpAmt:" + canInpAmt);
                int norOutCnt = Integer.parseInt(deData.substring(110, 117));
                System.out.println("norOutCnt:" + norOutCnt);
                long norOutAmt = Long.parseLong(deData.substring(117, 130));
                System.out.println("norOutAmt:" + norOutAmt);
                int canOutCnt = Integer.parseInt(deData.substring(130, 137));
                System.out.println("canOutCnt:" + canOutCnt);
                long canOutAmt = Long.parseLong(deData.substring(137, 150));
                System.out.println("canOutAmt:" + canOutAmt);

                String result = insSTotl(tranDate, inputBankCode, norInpCnt,
                        norInpAmt, canInpCnt, canInpAmt, norOutCnt, norOutAmt,
                        canOutCnt, canOutAmt, conn, dbcm);
                if (!result.equals("")) {
                    sbf.append("0000");
                    sbf.append(deData.substring(44, 70));
                    sbf.append(result);
                    sbf.append(deData.substring(150, 200)); // 2010.01.18 by
                    // jhlee 추가
                } else {
                    sbf.append("V141");
                    sbf.append(deData.substring(44, 200));
                }
                sendData = sbf.toString();
            } else if ("4000".equals(tranGubun)) {
                System.out.print(".");

                /* 데이터 유효성 체크 */
                int rc[] = recData_chk(deData);

                if (rc[0] == 1) {
                    /* 파일 생성 */
                    int fr = file_receive();

                    if (fr == 1) {
                        sbf.append("0000");
                        sbf.append(deData.substring(44, 117));
                        for (int i = 0; i < 87; i++) {
                            space += " ";
                        }
                        sbf.append(space);
                        sendData = sbf.toString();
                    } else
                        log.info("file create Error....");
                } else {

                    // pageno -1 해서 client send
                    sbf.append("0000");
                    sbf.append(deData.substring(44, 98));
                    String pageno = Integer.toString(rc[1]);
                    int pag = pageno.length();
                    if (pag != 4) {
                        for (int n = 0; n < 4 - pag; n++) {
                            pageno = "0" + pageno;
                        }
                    }
                    sbf.append(pageno); // 페이지 번호
                    sbf.append(deData.substring(102, 113));
                    for (int i = 0; i < 87; i++) {
                        space += " ";
                    }
                    sbf.append(space);
                    sendData = sbf.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {

                sbf.append("V141");
                sbf.append(deData.substring(44, 86));
                sbf.append(company); /* 기관명 SET : 20byte */

                k = 0;
                for (int i = 20; i < 114; i++) {
                    etemp[k] = strBytes[i];
                    k++;
                }
                newTemp = new String(etemp);
                sbf.append(newTemp);

                sendData = sbf.toString();
                log.info("in db process cash update=>false");
            } catch (Exception ex) {
            }
        } finally {
            dbcm.releaseConnection();
        }

        return sendData;
    }

    /**
     * 데이터 유효성 체크
     */
    private int[] recData_chk(String recData) {

        try {

            int pageno = 0;
            int curcnt = 0;
            pageno = Integer.parseInt(recData.substring(98, 102).trim()); // 현재
            // page번호
            curcnt = Integer.parseInt(recData.substring(109, 113).trim()); // 현재
            // data건수

            byte buff[] = new byte[MAX_SIZE];
            byte tempByte[] = new byte[110];

            recData = recData.substring(117, recData.length());
            buff = recData.getBytes();

            int i = 1;
            int k = 0;
            int sPaceChk = 1; // 계좌번호,거래금액 값 체크 return value

            for (int c = 0; c < curcnt; c++) {

                int subS = (i - 1) * 110;
                int subE = i * 110;

                k = 0;
                // 각 110byte Data
                for (int n = subS; n < subE; n++) {
                    tempByte[k] = buff[n];
                    k++;
                }

                /* 계좌번호 */
                k = 0;
                byte temp[] = new byte[16];
                for (int j = 18; j < 34; j++) {
                    temp[k] = tempByte[j];
                    k++;
                }
                String inpAcctNo = new String(temp);
                inpAcctNo = inpAcctNo.trim();

                /* 거래금액 */
                k = 0;
                byte temp2[] = new byte[13];
                for (int j = 41; j < 54; j++) {
                    temp2[k] = tempByte[j];
                    k++;
                }
                String trAmt = new String(temp2);
                trAmt = trAmt.trim();

                // 계좌번호, 거래금액 값 체크
                // if((inpAcctNo.equals("")&& inpAcctNo.length()!=14) ||
                // trAmt.equals("")){ //만약, 바이트로 체크 시,space는 value=32

                // sPaceChk = 0;
                // break;
                // }

                i++;

            }// end while

            int[] retVal = new int[2];
            retVal[0] = sPaceChk;

            if (sPaceChk == 0)
                retVal[1] = pageno - 1;
            else
                retVal[1] = pageno;

            return retVal;

        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }

    }

    /**
     * 파일 생성
     */
    private int file_receive() {

        try {
            String SerIl = deData.substring(70, 78).trim(); // 조회시작일
            String fileName = SerIl + "11.txt"; // 파일이름

            int pageno = 0;
            int curcnt = 0;
            int pagecnt = 0;
            int totcnt = 0;
            pageno = Integer.parseInt(deData.substring(98, 102).trim()); // 현재
            // page번호
            curcnt = Integer.parseInt(deData.substring(109, 113).trim()); // 현재
            // data건수
            pagecnt = Integer.parseInt(deData.substring(113, 117).trim());
            totcnt = Integer.parseInt(deData.substring(102, 109).trim());

            // 디렉토리 존재여부 체크
            File f = new File(FILE_DIR);
            if (!f.exists()) {
                f.mkdir(); // 존재하지 않으면 생성
            }

            String fileDir = FILE_DIR + "/" + fileName;

            StringBuffer sb = new StringBuffer();

            RandomAccessFile raf = null;

            if (pageno == 1) {
                System.out.println("\n" + SerIl + "일자 거래내역 생성 시작...");
                FileWriter fw = null;
                fw = new FileWriter(fileDir);
                sb.append("거래일자|거래시간|은행|입금계좌번호|거래고유번호|거래금액|거래자|취급기관코드|취급지점코드|결과|CMS코드|                \n");
                fw.write(sb.toString());
                fw.flush();
                fw.close();
                raf = new RandomAccessFile(fileDir, "rw");
                raf.seek(121);
                sb.delete(0, sb.toString().getBytes().length);
            } else {
                int RECORD_SZ = 121;
                long offsetPosition;
                offsetPosition = (pageno * pagecnt - (pagecnt - 1)) * RECORD_SZ;
                raf = new RandomAccessFile(fileDir, "rw");
                raf.seek(offsetPosition);
            }

            byte buff[] = new byte[MAX_SIZE];
            byte tempByte[] = new byte[110];
            String trIl = ""; // 거래일자
            String trSi = ""; // 거래시간
            String bankCd = ""; // 은행
            String inpAcctNo = ""; // 입금계좌번호
            String trNo = ""; // 거래고유번호
            String trAmt = ""; // 거래금액
            String inpAcctNm = ""; // 거래자
            String handOrgCd = ""; // 취급기관코드
            String handSubCd = ""; // 취급지점코드
            String retRet = ""; // 결과
            String cmsCd = ""; // CMS코드

            String tempData = deData.substring(117, deData.length());
            buff = tempData.getBytes();

            int i = 1;
            int k = 0;
            while (curcnt > 0) {

                int subS = (i - 1) * 110;
                int subE = i * 110;

                k = 0;
                for (int n = subS; n < subE; n++) {
                    tempByte[k] = buff[n];
                    k++;
                }

                /* 거래일자 */
                k = 0;
                byte temp[] = new byte[8];
                for (int j = 0; j < 8; j++) {
                    temp[k] = tempByte[j];
                    k++;
                }
                trIl = new String(temp);

                /* 거래시간 */
                k = 0;
                byte temp2[] = new byte[6];
                for (int j = 8; j < 14; j++) {
                    temp2[k] = tempByte[j];
                    k++;
                }
                trSi = new String(temp2);

                /* 은행명 */
                k = 0;
                byte temp3[] = new byte[4];
                for (int j = 14; j < 18; j++) {
                    temp3[k] = tempByte[j];
                    k++;
                }
                bankCd = new String(temp3);

                /* 계좌번호 */
                k = 0;
                byte temp4[] = new byte[16];
                for (int j = 18; j < 34; j++) {
                    temp4[k] = tempByte[j];
                    k++;
                }
                inpAcctNo = new String(temp4);

                /* 거래고유번호 */
                k = 0;
                byte temp5[] = new byte[7];
                for (int j = 34; j < 41; j++) {
                    temp5[k] = tempByte[j];
                    k++;
                }
                trNo = new String(temp5);

                /* 거래금액 */
                k = 0;
                byte temp6[] = new byte[13];
                for (int j = 41; j < 54; j++) {
                    temp6[k] = tempByte[j];
                    k++;
                }
                trAmt = new String(temp6);

                /* 입금자명 */
                k = 0;
                byte temp7[] = new byte[20];
                for (int j = 54; j < 74; j++) {
                    temp7[k] = tempByte[j];
                    k++;
                }
                inpAcctNm = new String(temp7);

                /* 취급은행코드 */
                k = 0;
                byte temp8[] = new byte[2];
                for (int j = 74; j < 76; j++) {
                    temp8[k] = tempByte[j];
                    k++;
                }
                handOrgCd = new String(temp8);

                /* 취급은행지점코드 */
                k = 0;
                for (int j = 76; j < 80; j++) {
                    temp3[k] = tempByte[j];
                    k++;
                }
                handSubCd = new String(temp3);

                /* 결과 */
                k = 0;
                byte temp9[] = new byte[15];
                for (int j = 80; j < 95; j++) {
                    temp9[k] = tempByte[j];
                    k++;
                }
                retRet = new String(temp9);

                /* cms 코드 */
                k = 0;
                for (int j = 95; j < 110; j++) {
                    temp9[k] = tempByte[j];
                    k++;
                }
                cmsCd = new String(temp9);

                System.out.println("trIl	=	"+trIl);
                System.out.println("trSi	=	"+trSi);
                System.out.println("bankCd	=	"+bankCd);
                System.out.println("inpAcctNo	=	"+inpAcctNo);
                System.out.println("trNo	=	"+trNo);
                System.out.println("trAmt	=	"+trAmt);
                System.out.println("inpAcctNm	=	"+inpAcctNm);
                System.out.println("handOrgCd	=	"+handOrgCd);
                System.out.println("handSubCd	=	"+handSubCd);
                System.out.println("retRet	=	"+retRet);
                System.out.println("cmsCd	=	"+cmsCd);

                sb.append(trIl + "|");
                sb.append(trSi + "|");
                sb.append(bankCd + "|");
                sb.append(inpAcctNo + "|");
                sb.append(trNo + "|");
                sb.append(trAmt + "|");
                sb.append(inpAcctNm + "|");
                sb.append(handOrgCd + "|");
                sb.append(handSubCd + "|");
                sb.append(retRet + "|");
                sb.append(cmsCd + "\n");

                raf.write(sb.toString().getBytes());
                sb.delete(0, sb.toString().getBytes().length);

                curcnt--;
                i++;
            }

            raf.close(); // file close
            return 1;

        } catch (Exception e) {
            log.info(e.getMessage());
            return 0;
        }
    }

    /*
     * 입금조건 체크하는 기관 -> 계좌 조건 조회
     */
    private String checkVact(String virtualAccount, String inputBankCode,
                             String trIlSi, long trAmt, String tranDate, String tranNumber,
                             String tranTime, Connection con, DBConnectionManager dbcm) {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String inpRet = "0000"; // 응답코드
        String acct_st = ""; // 계좌상태
        String tramt_cond = ""; // 거래금액 조건
        String trbegin_il = ""; // 입금시작일
        String trend_il = ""; // 입금마감일
        String trbegin_si = ""; // 입금시작일
        String trend_si = ""; // 입금마감일
        long tr_amt = 0; // 거래금액
        String trbegin_ilsi = "";
        String trend_ilsi = "";
        String trmc_cond = ""; // 입금회차조건 0:1계좌 1건 수납, 1:1계좌 여러건 수납

        int seq_no = 0; // 수납횟수
        try {

            String chkSQL = "SELECT ACCT_ST, TRAMT_COND, TRBEGIN_IL, TREND_IL, TRBEGIN_SI, TREND_SI, TR_AMT,TRMC_COND, SEQ_NO, CMF_NM, LST_IL, FST_IL " +
                            "FROM VACS_VACT WHERE ORG_CD = ? AND ACCT_NO = ? AND BANK_CD = ? ";

            log.info("====================================================");
            log.info("orgcd[" + corpId + "]" + "virtualAccount["
                    + virtualAccount + "]" + "inputBankCode[" + inputBankCode
                    + "]");
            log.info("====================================================");

            pstmt = con.prepareStatement(chkSQL);
            pstmt.setString(1, corpId);
            pstmt.setString(2, virtualAccount);
            pstmt.setString(3, inputBankCode);
            rs = pstmt.executeQuery();


            if (rs.next()) {
                acct_st = isNull(rs.getString("acct_st")).trim();
                tramt_cond = isNull(rs.getString("tramt_cond")).trim();
                trbegin_il = isNull(rs.getString("trbegin_il")).trim();
                trend_il = isNull(rs.getString("trend_il")).trim();
                trbegin_si = isNull(rs.getString("trbegin_si")).trim();
                trend_si = isNull(rs.getString("trend_si")).trim();
                trbegin_ilsi = trbegin_il + trbegin_si;
                trend_ilsi = trend_il + trend_si;
                tr_amt = rs.getLong("tr_amt");
                trmc_cond = isNull(rs.getString("trmc_cond")).trim();
                seq_no = rs.getInt("seq_no");
                // company = new
                // String(isNull(rs.getString("cmf_nm")).getBytes("8859_1"),
                // "KSC5601");
                //company = rs.getString("cmf_nm").trim();
                //1번케이스
                company = isNull(rs.getString("cmf_nm")).trim();

                //2번케이스
				/*
				company = rs.getString("cmf_nm");

				if(company != null)
				{
					company.trim();
				}
				*/

                System.out.println("company:" + company);
                fst_il = isNull(rs.getString("fst_il")).trim();
                lst_il = isNull(rs.getString("lst_il")).trim();

                // 회사이름 자리수 보정
                company = companyLen(company);

                // 계좌상태 확인
                if (!acct_st.equals("1")) { // 계좌상태 할당 아니면
                    inpRet = "V816";
                    log.info("계좌 할당상태 아님 :" + virtualAccount);
                    errlog_insert(tranDate, tranNumber, tranTime,
                            inputBankCode, virtualAccount, trAmt, "V816", con,
                            dbcm);
                    return inpRet;
                }

                // 거래금액 확인
                if (!tramt_cond.equals("0")) { // 거래금액 조건 있을 때
                    if (tramt_cond.equals("1")) { // 금액 = 실입금액
                        if (tr_amt != trAmt) {
                            inpRet = "V713";
                            log.info("거래금액 불일치 :" + virtualAccount);
                            errlog_insert(tranDate, tranNumber, tranTime,
                                    inputBankCode, virtualAccount, trAmt,
                                    "V713", con, dbcm);
                            return inpRet;
                        }
                    } else if (tramt_cond.equals("2")) { // 금액 >=실입금액
                        if (tr_amt < trAmt) {
                            inpRet = "V713";
                            log.info("거래금액 불일치 :" + virtualAccount);
                            errlog_insert(tranDate, tranNumber, tranTime,
                                    inputBankCode, virtualAccount, trAmt,
                                    "V713", con, dbcm);
                            return inpRet;
                        }
                    } else if (tramt_cond.equals("3")) { // 금액 <= 실입금액
                        if (tr_amt > trAmt) {
                            inpRet = "V713";
                            log.info("거래금액 불일치 :" + virtualAccount);
                            errlog_insert(tranDate, tranNumber, tranTime,
                                    inputBankCode, virtualAccount, trAmt,
                                    "V713", con, dbcm);
                            return inpRet;
                        }
                    }
                }

                // 입금기간 확인
                if (!trbegin_il.equals("") && !trbegin_il.equals("00000000")
                        && !trend_il.equals("") && !trend_il.equals("00000000")) {
                    if (trIlSi.compareTo(trbegin_ilsi) < 0
                            || trIlSi.compareTo(trend_ilsi) > 0) {
                        inpRet = "V682";
                        log.info("입금기간 불일치 " + virtualAccount);
                        errlog_insert(tranDate, tranNumber, tranTime,
                                inputBankCode, virtualAccount, trAmt, "V682",
                                con, dbcm);
                        return inpRet;
                    }
                }

                // 수납회차 확인
                if (trmc_cond.equals("0")) {
                    if (seq_no > 0) {
                        inpRet = "V416";
                        log.info("불입횟수 초과 " + virtualAccount);
                        errlog_insert(tranDate, tranNumber, tranTime,
                                inputBankCode, virtualAccount, trAmt, "V416",
                                con, dbcm);
                        return inpRet;
                    }
                }
            } else { // vacs_vact 테이블에 존재하지 않는 계좌
                inpRet = "V816";
                log.info("존재하지 않는 계좌 :" + virtualAccount);
                errlog_insert(tranDate, tranNumber, tranTime, inputBankCode,
                        virtualAccount, trAmt, "V816", con, dbcm);
                return inpRet;
            }

        } catch (Exception e) {
            log.info(e.getMessage());
            dbcm.close(rs, pstmt, con);
        } finally {
            dbcm.close(rs, pstmt, null);
        }

        return inpRet;
    }

    /*
     * 해당 거래의 입금/취소 등의 상태 조회
     */
    private String chkCancel(String tranDate, String tranNumber,
                             String virtualAccount, Connection con, DBConnectionManager dbcm)
            throws Exception {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String inp_st = "0";
        try {

            String selSql = "SELECT INP_ST FROM VACS_AHST WHERE ORG_CD = ? AND TR_IL = ? AND TR_NO = ? AND IACCT_NO = ? ";

            pstmt = con.prepareStatement(selSql);
            pstmt.setString(1, corpId);
            pstmt.setString(2, tranDate);
            pstmt.setString(3, tranNumber);
            pstmt.setString(4, virtualAccount);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                inp_st = isNull(rs.getString("inp_st"));
            }

        } catch (Exception e) {
            log.info("chkCancel:" + e);
            dbcm.close(rs, pstmt, con);
            throw e;
        } finally {
            dbcm.close(rs, pstmt, null);
        }
        return inp_st;
    }

    /*
     * 입금 처리 (vacs_ahst insert, vacs_totl update, vacs_vact update)
     */
    private boolean insAhst(String tranDate, String tranTime, String tranGubun,
                            String inputBankCode, String virtualAccount, String snNm,
                            long amount, long ta_amount, String tranNumber, String iorgCd,
                            String cmsCd, String mediaGb, String gubun, Connection con, DBConnectionManager dbcm)
            throws Exception {

        PreparedStatement pstmt = null;
        boolean result = false;
        ResultSet rs = null;
        String exist = "";
        // 201903 by shcho 로그 추가
        // 각 테이블 별 업데이트 결과 변수 설정 0=실패 , 1= 정상
        int row1 = 0;  //AHST INSERT 결과
        int row2 = 0;  //TOTL UPDATE or INSERT 결과
        int row3 = 0;  //VACT UPDATE 결과

        try {

            if (inpCond.equals("0")) { //테이블 조회
                cmsCd = "";
                company = "";
                String selCms = "SELECT CMS_CD, CMF_NM, FST_IL FROM VACS_VACT WHERE ORG_CD = ? AND BANK_CD= ? AND ACCT_NO = ?";
                pstmt = con.prepareStatement(selCms);
                pstmt.setString(1, corpId);
                pstmt.setString(2, inputBankCode);
                pstmt.setString(3, virtualAccount);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    cmsCd = isNull(rs.getString("cms_cd")).trim();
                    company = isNull(rs.getString("cmf_nm"));
                    fst_il = isNull(rs.getString("fst_il"));
                    exist = "1";
                }
            }

            // 회사이름 자리수 보정
            company = companyLen(company);

            // 거래내역 insert
            // 매체추가(2014.02)
            String insAhst = "INSERT INTO VACS_AHST(ORG_CD, TR_IL, TR_SI, TR_CD, BANK_CD, IACCT_NO, IACCT_NM, TR_AMT, MITA_AMT, TR_NO, INP_ST, INP_SI, CANINP_SI, IORG_CD, CMS_CD, MEDIA_GB) VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            // long trAmt = amount + ta_amount;

            pstmt = con.prepareStatement(insAhst);
            pstmt.setString(1, corpId);
            pstmt.setString(2, tranDate);
            pstmt.setString(3, tranTime);
            pstmt.setString(4, tranGubun);
            pstmt.setString(5, inputBankCode);
            pstmt.setString(6, virtualAccount);
            //pstmt.setString(7, new String(snNm.getBytes("euc-kr"), "UTF-8"));
            pstmt.setString(7, snNm);
            //System.out.println("snNm.getBytes KCS5601 -> 8859_1" + new String(snNm.getBytes("KSC5601"), "8859_1"));
            //System.out.println("snNm.getBytes KCS5601 -> euc-kr" + new String(snNm.getBytes("KSC5601"), "euc-kr"));
            //System.out.println("snNm.getBytes KCS5601 -> UTF-8" + new String(snNm.getBytes("KSC5601"), "UTF-8"));
            //System.out.println("snNm.getBytes euc-kr -> UTF-8" + new String(snNm.getBytes("euc-kr"), "UTF-8"));
            pstmt.setLong(8, amount);
            pstmt.setLong(9, ta_amount);
            pstmt.setString(10, tranNumber);
            pstmt.setString(11, gubun);               // 입금/취소 구분
            pstmt.setString(12, currentTimeString()); // insert 시간
            pstmt.setString(13, "");                  // cancel_si
            pstmt.setString(14, iorgCd);              // 취급은행
            pstmt.setString(15, cmsCd);               // cms_cd
            pstmt.setString(16, mediaGb);             // 매체구분
            row1 = pstmt.executeUpdate();

            if (gubun.equals("1")) {
                // 집계내역 update
                String upTotl = "UPDATE VACS_TOTL SET NORINP_CNT = NORINP_CNT + 1, NORINP_AMT = NORINP_AMT + ?, LST_IL = ?  WHERE ORG_CD = ? AND TR_IL = ? AND BANK_CD = ?";
                pstmt = con.prepareStatement(upTotl);
                pstmt.setLong(1, amount);
                pstmt.setString(2, curDateString());
                pstmt.setString(3, corpId);
                pstmt.setString(4, tranDate);
                pstmt.setString(5, inputBankCode);
                row2 = pstmt.executeUpdate();
                // 오늘자 은행별 집계내역없으면 insert 한다.
                if (row2 == 0) {
                    String insTotl = "INSERT INTO VACS_TOTL(ORG_CD, TR_IL, BANK_CD, NORINP_CNT, NORINP_AMT, CANINP_CNT, CANINP_AMT, NOROUT_CNT, NOROUT_AMT, CANOUT_CNT, CANOUT_AMT, NORQUE_CNT, UMS_CNT, AUTH_CNT, NAME_CNT, LST_IL) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    pstmt = con.prepareStatement(insTotl);
                    pstmt.setString(1, corpId);
                    pstmt.setString(2, tranDate);
                    pstmt.setString(3, inputBankCode);
                    pstmt.setInt(4, 1);
                    pstmt.setLong(5, amount);
                    pstmt.setInt(6, 0);
                    pstmt.setLong(7, 0);
                    pstmt.setInt(8, 0);
                    pstmt.setLong(9, 0);
                    pstmt.setInt(10, 0);
                    pstmt.setLong(11, 0);
                    pstmt.setInt(12, 0);
                    pstmt.setLong(13, 0);
                    pstmt.setInt(14, 0);
                    pstmt.setLong(15, 0);
                    pstmt.setString(16, curDateString());
                    row2 = pstmt.executeUpdate();
                }

                if (inpCond.equals("0") && exist.equals("1")) { // 조건 확인

                    String upVact = "";

                    if (fst_il.equals("00000000")||fst_il.equals("")){
                        log.info("최초 입금거래 일자 세팅");
                        upVact = "UPDATE VACS_VACT SET SEQ_NO = SEQ_NO + 1, FST_IL = ?, LST_IL = ? WHERE ORG_CD = ? AND ACCT_NO = ? AND BANK_CD = ?";
                        pstmt = con.prepareStatement(upVact);
                        pstmt.setString(1, curDateString());
                        pstmt.setString(2, curDateString());
                        pstmt.setString(3, corpId);
                        pstmt.setString(4, virtualAccount);
                        pstmt.setString(5, inputBankCode);
                    }else{
                        upVact = "UPDATE VACS_VACT SET SEQ_NO = SEQ_NO + 1, LST_IL = ? WHERE ORG_CD = ? AND ACCT_NO = ? AND BANK_CD = ?";
                        pstmt = con.prepareStatement(upVact);
                        pstmt.setString(1, curDateString());
                        pstmt.setString(2, corpId);
                        pstmt.setString(3, virtualAccount);
                        pstmt.setString(4, inputBankCode);
                    }

                    row3 = pstmt.executeUpdate();

                    // 모두 1이어야 정상
                    if (row1 == 1 && row2 == 1 && row3 == 1) {
                        result = true;
                        dbcm.commit();
                    } else {
                        dbcm.rollback();
                    }
                } else {
                    if (row1 == 1 && row2 == 1) {
                        result = true;
                        dbcm.commit();
                    } else {
                        dbcm.rollback();
                    }
                }
            } else if (gubun.equals("2")) { // 취소가 입금보다 먼저들어온 경우
                result = true;
                dbcm.commit();
            }



        } catch (Exception e) {
            log.info("insAhst:" + e);
            dbcm.rollback();
            dbcm.close(null, pstmt, con);
            throw e;
        } finally {
            log.info("VACS_AHST[" + row1+"] / VACS_TOTL["+row2+"] / VACS_VACT["+row3+"]");
            dbcm.close(null, pstmt, null);
        }
        return result;
    }

    private boolean canAhst(String tranDate, String tranTime,
                            String tranNumber, String virtualAccount, String inputBankCode,
                            long amount, Connection con, DBConnectionManager dbcm)
            throws Exception {

        PreparedStatement pstmt = null;
        PreparedStatement pstmt2 = null;
        boolean result = false;
        ResultSet rs = null;
        String exist = "";
        // 202005 by shcho 로그 추가
        // 각 테이블 별 업데이트 결과 변수 설정 0=실패 , 1= 정상
        int row1 = 0;  //AHST INSERT 결과
        int row2 = 0;  //TOTL UPDATE or INSERT 결과
        int row3 = 0;  //VACT UPDATE 결과

        try {
            if (inpCond.equals("0")) {
                String selCms = "SELECT COUNT(*) FROM VACS_VACT WHERE ORG_CD = ? and BANK_CD= ? AND ACCT_NO = ?";
                pstmt = con.prepareStatement(selCms);
                pstmt.setString(1, corpId);
                pstmt.setString(2, inputBankCode);
                pstmt.setString(3, virtualAccount);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    if(rs.getInt(1) > 0){
                        exist = "1";
                    }
                }
            }
            String upAhst = "UPDATE VACS_AHST SET INP_ST = '2' ,CANINP_SI = ? WHERE ORG_CD = ? AND TR_IL = ? AND TR_NO = ? AND IACCT_NO = ?";
            pstmt = con.prepareStatement(upAhst);
            pstmt.setString(1, tranTime); // 취소시간
            pstmt.setString(2, corpId);
            pstmt.setString(3, tranDate);
            pstmt.setString(4, tranNumber);
            pstmt.setString(5, virtualAccount);
            row1 = pstmt.executeUpdate();

            // 집계내역 update
            String upTotl = "UPDATE VACS_TOTL SET CANINP_CNT = CANINP_CNT + 1, CANINP_AMT = CANINP_AMT + ?, LST_IL = ?  WHERE ORG_CD = ? AND  TR_IL = ? AND BANK_CD = ?";
            pstmt = con.prepareStatement(upTotl);
            pstmt.setLong(1, amount);
            pstmt.setString(2, curDateString());
            pstmt.setString(3, corpId);
            pstmt.setString(4, tranDate);
            pstmt.setString(5, inputBankCode);
            row2 = pstmt.executeUpdate();

            if (inpCond.equals("0") && exist.equals("1")) { // 조건 확인
                String upVact = "UPDATE VACS_VACT SET SEQ_NO = SEQ_NO - 1 WHERE ORG_CD = ? AND BANK_CD = ? AND ACCT_NO = ? ";
                pstmt = con.prepareStatement(upVact);
                pstmt.setString(1, corpId);
                pstmt.setString(2, inputBankCode);
                pstmt.setString(3, virtualAccount);
                row3 = pstmt.executeUpdate();
                if (row1 == 1 && row2 == 1 && row3 == 1) {
                    result = true;
                    dbcm.commit();
                } else {
                    dbcm.rollback();
                }
            } else {
                if (row1 == 1 && row2 == 1) {
                    result = true;
                    dbcm.commit();
                } else {
                    dbcm.rollback();
                }
            }

        } catch (Exception e) {
            log.info("canAhst:" + e);
            dbcm.close(null, pstmt, con);
            throw e;
        } finally {
            log.info("VACS_AHST[" + row1 +"] / VACS_TOTL["+row2+"] / VACS_VACT["+row3+"]");
            dbcm.close(null, pstmt, null);
        }
        return result;
    }

    private String insSTotl(String tranDate, String inputBankCode,
                            int norInpCnt, long norInpAmt, int canInpCnt, long canInpAmt,
                            int norOutCnt, long norOutAmt, int canOutCnt, long canOutAmt,
                            Connection con, DBConnectionManager dbcm) throws Exception {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String result = "";
        try {

            String selTotl = "SELECT * FROM VACS_TOTL WHERE ORG_CD = ? AND TR_IL = ?  AND BANK_CD = '000'";
            String insSTotl = "INSERT INTO VACS_TOTL(ORG_CD, TR_IL, BANK_CD, NORINP_CNT, NORINP_AMT, CANINP_CNT, CANINP_AMT, NOROUT_CNT, NOROUT_AMT, CANOUT_CNT, CANOUT_AMT, NORQUE_CNT, UMS_CNT, AUTH_CNT, NAME_CNT, LST_IL) "
                    + " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
            String upTotl = "UPDATE VACS_TOTL "
                    + "SET NORINP_CNT = ?, NORINP_AMT = ?, CANINP_CNT = ?, CANINP_AMT = ? "
                    + " ,NOROUT_CNT = ?, NOROUT_AMT = ?, CANOUT_CNT = ?, CANOUT_AMT = ? "
                    + " WHERE ORG_CD = ? AND TR_IL = ? AND BANK_CD ='000' ";
            String selTotl2 = "SELECT SUM(NORINP_CNT) AS NORINP_CNT, SUM(NORINP_AMT) AS NORINP_AMT, SUM(CANINP_CNT) AS CANINP_CNT, SUM(CANINP_AMT) AS CANINP_AMT, SUM(NOROUT_CNT) AS NOROUT_CNT, SUM(NOROUT_AMT) AS NOROUT_AMT, SUM(CANOUT_CNT) AS CANOUT_CNT, SUM(CANOUT_AMT) AS CANOUT_AMT FROM VACS_TOTL WHERE ORG_CD = ? AND TR_IL = ? AND BANK_CD <> '000'";
            pstmt = con.prepareStatement(selTotl);
            pstmt.setString(1, corpId);
            pstmt.setString(2, tranDate);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                pstmt = con.prepareStatement(upTotl);
                pstmt.setInt(1, norInpCnt);
                pstmt.setLong(2, norInpAmt);
                pstmt.setInt(3, canInpCnt);
                pstmt.setLong(4, canInpAmt);
                pstmt.setInt(5, norOutCnt);
                pstmt.setLong(6, norOutAmt);
                pstmt.setInt(7, canOutCnt);
                pstmt.setLong(8, canOutAmt);
                pstmt.setString(9, corpId);
                pstmt.setString(10, tranDate);

                if (pstmt.executeUpdate() == 1) {
                    // result = true;
                    dbcm.commit();
                } else
                    dbcm.rollback();
                pstmt.close();
            } else {

                pstmt = con.prepareStatement(insSTotl);
                pstmt.setString(1, corpId);
                pstmt.setString(2, tranDate);
                pstmt.setString(3, inputBankCode);
                pstmt.setInt(4, norInpCnt);
                pstmt.setLong(5, norInpAmt);
                pstmt.setInt(6, canInpCnt);
                pstmt.setLong(7, canInpAmt);
                pstmt.setInt(8, norOutCnt);
                pstmt.setLong(9, norOutAmt);
                pstmt.setInt(10, canOutCnt);
                pstmt.setLong(11, canOutAmt);
                pstmt.setInt(12, 0);
                pstmt.setLong(13, 0);
                pstmt.setInt(14, 0);
                pstmt.setLong(15, 0);
                pstmt.setString(16, curDateString());
                if (pstmt.executeUpdate() == 1) {
                    // result = true;
                    dbcm.commit();
                } else
                    dbcm.rollback();
                pstmt.close();
            }

            pstmt = con.prepareStatement(selTotl2);
            pstmt.setString(1, corpId);
            pstmt.setString(2, tranDate);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = fixLen(Integer.toString(rs.getInt("norinp_cnt")), 7)
                        + fixLen(Long.toString(rs.getLong("norinp_amt")), 13)
                        + fixLen(Integer.toString(rs.getInt("caninp_cnt")), 7)
                        + fixLen(Long.toString(rs.getLong("caninp_amt")), 13)
                        + fixLen(Integer.toString(rs.getInt("norout_cnt")), 7)
                        + fixLen(Long.toString(rs.getLong("norout_amt")), 13)
                        + fixLen(Integer.toString(rs.getInt("canout_cnt")), 7)
                        + fixLen(Long.toString(rs.getLong("canout_amt")), 13);

            }

        } catch (Exception e) {
            log.info("insSTotl:" + e);
            result = "";
            dbcm.close(rs, pstmt, con);
            throw e;
        } finally {
            dbcm.close(rs, pstmt, null);
        }
        return result;
    }

    private void errlog_insert(String tranDate, String tranNumber,
                               String tranTime, String inputBankCode, String virtualAccount,
                               long amount, String errCd, Connection con, DBConnectionManager dbcm)
            throws Exception {

        PreparedStatement pstmt = null;
        boolean result = false;
        try {

            String insErr = "INSERT INTO VACS_ERRLOG(ORG_CD, TR_IL, TR_NO, TR_SI, BANK_CD, ACNT_NO, TR_AMT, ERR_CD) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = con.prepareStatement(insErr);
            pstmt.setString(1, corpId);
            pstmt.setString(2, tranDate);
            pstmt.setString(3, tranNumber);
            pstmt.setString(4, tranTime);
            pstmt.setString(5, inputBankCode);
            pstmt.setString(6, virtualAccount);
            pstmt.setLong(7, amount);
            pstmt.setString(8, errCd);
            pstmt.executeUpdate();
            dbcm.commit();

        } catch (Exception e) {
            log.info("errlog_insert:" + e);
            dbcm.close(null, pstmt, con);
            throw e;
        } finally {
            dbcm.close(null, pstmt, null);
        }
    }

    private static String isNull(String str) {

        if (str == null)
            return "";

        else
            return str;

    }

    private static String fixLen(String str, int size) {
        int L = str.getBytes().length;
        for (int i = 0; i < size - L; i++) {
            str = "0" + str;
        }

        return str;
    }

    private static String companyLen(String company) {
        // 회사이름 자리수 보정

        byte tempCorp[] = new byte[20];
        byte corpNameBytes[] = company.getBytes();
        if (corpNameBytes.length <= 20) {
            int i = 0;
            for (i = 0; i < corpNameBytes.length; i++) {
                tempCorp[i] = corpNameBytes[i];
            }
            while (i < tempCorp.length) {
                tempCorp[i++] = (byte) ' ';
            }
        } else {
            for (int i = 0; i < 20; i++) {
                tempCorp[i] = corpNameBytes[i];
            }
        }
        company = new String(tempCorp);
        return company;
    }

    /**
     * 현재 시간을 'hhmmss'형식으로 리턴
     */

    public static String currentTimeString() {

        Calendar cal = Calendar.getInstance();// TimeZone.getTimeZone("JST") );

        int h = cal.get(Calendar.HOUR_OF_DAY);

        int m = cal.get(Calendar.MINUTE);

        int s = cal.get(Calendar.SECOND);

        String hour = String.valueOf(h);

        String minute = String.valueOf(m);

        String second = String.valueOf(s);

        if (h < 10)
            hour = "0" + hour;

        if (m < 10)
            minute = "0" + minute;

        if (s < 10)
            second = "0" + second;

        return hour + minute + second;

    }

    /**
     * 현재 날짜를 'yyyymmdd' 형식으로 리턴
     *
     * @return String
     */

    public static String curDateString() {

        Calendar cal = Calendar.getInstance();// TimeZone.getTimeZone("JST") );

        int y = cal.get(Calendar.YEAR);

        int m = cal.get(Calendar.MONTH) + 1;

        int d = cal.get(Calendar.DAY_OF_MONTH);

        String month = String.valueOf(m);

        String day = String.valueOf(d);

        if (m < 10)
            month = "0" + month;

        if (d < 10)
            day = "0" + day;

        return "" + y + month + day;

    }

    /**
     * 클라이언트에게 데이터 전송
     *
     * @param sendData
     *            전송데이터
     */
    public boolean send(String sendData) {

        try {
            log.info("sendData:" + sendData);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(output));

            if (enc.equals("1")) {
                byte enDataByte[] = base64Encoding(seedEncrypt(sendData, key));
                setEnData(new String(enDataByte));
            } else {
                enData = sendData;
            }

            int len = enData.getBytes().length; // 전체 전문길이
            String mlen = Integer.toString(len);
            for (int i = 0; i < (4 - mlen.length()); i++)
                mlen = "0" + mlen;

            bw.write(mlen + enData);
            bw.flush();
            recSocket.close();

        } catch (IOException ioe) {
            log.info("send error = " + ioe);
            return false;
        }
        return true;
    }

    /**
     * SEED 복호화
     *
     * @param data
     *            복호화처리용 데이터
     * @param key
     *            키값
     * @return seed decrypt 처리된 데이터
     */
    public byte[] seedDecrypt(byte[] data, String key) {
        //return SymmetricCipher.SEED_CBC_DECRYPT(data, key.getBytes());
        return new byte[10];
    }

    /**
     * SEED 암호화
     *
     * @param data
     *            암호화 처리용 데이터
     * @param key
     *            키값
     * @return seed encrypt 처리된 데이터
     */
    public byte[] seedEncrypt(String data, String key) {
        //return SymmetricCipher.SEED_CBC_ENCRYPT(data.getBytes(), key.getBytes());
        return new byte[10];
    }

    /**
     * Base64 Decoding
     *
     * @param enData
     *            인코딩 데이터
     * @return 디코딩 데이터
     */
    public byte[] base64Decoding(byte[] enData) {
        //return Base64.base64Decode(enData);
        return enData;
    }

    /**
     * Base64 Encoding
     *
     * @param deData
     *            입력 데이터
     * @return 인코딩 데이터
     */
    public byte[] base64Encoding(byte[] deData) {
        //return Base64.base64Encode(deData);
        return deData;
    }

    /**
     * 디코딩 데이터 조회
     *
     * @return 디코딩 데이터
     */
    public String getDeData() {
        return deData;
    }

    /**
     * 인코딩 데이터 조회
     *
     * @return 인코딩 데이터
     */
    public String getEnData() {
        return enData;
    }

    /**
     * 디코딩 데이터 설정
     *
     * @param string
     *            입력데이터
     */
    public void setDeData(String string) {
        deData = string;
    }

    /**
     * 인코딩 데이터 설정
     *
     * @param string
     *            입력데이터
     */
    public void setEnData(String string) {
        enData = string;
    }
}

