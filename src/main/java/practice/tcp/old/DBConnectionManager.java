package practice.tcp.old;

import java.sql.*;

public class DBConnectionManager {

    private Connection con;
    private String DB_URL;
    private String DB_ID;
    private String DB_PWD;
    private String DB_INFO;

    public DBConnectionManager() {
    }

    public Connection getConnection(String DB_INFO, String DB_URL,
                                    String DB_ID, String DB_PWD) {
        this.DB_INFO = DB_INFO;
        this.DB_URL = DB_URL;
        this.DB_ID = DB_ID;
        this.DB_PWD = DB_PWD;
        try {

            if (DB_INFO.equalsIgnoreCase("Oracle")) { /* oracle 용 */
                Class.forName("oracle.jdbc.driver.OracleDriver");
            } else if (DB_INFO.equalsIgnoreCase("Mysql")) {/* mysql 용 */
                Class.forName("com.mysql.jdbc.Driver");
            } else if (DB_INFO.equalsIgnoreCase("Mssql2000")) {/* mssql 2000용 */
                Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");
            } else if (DB_INFO.equalsIgnoreCase("Mssql2005")) {/* mssql 2005용 */
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            }

            con = DriverManager.getConnection(DB_URL, DB_ID, DB_PWD);
            con.setAutoCommit(false);
            if (con == null)
                return null;

        } catch (Exception e) {
            System.out.println(e);
        }

        return con;
    }

    public void releaseConnection() {
        try {
            con.close();
        } catch (Exception exception) {
        }
        con = null;
    }

    public void begin() throws Exception {
        con.setAutoCommit(false);
    }

    public void commit() {
        try {
            con.commit();
            con.setAutoCommit(true);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void rollback() {
        try {
            con.rollback();
        } catch (Exception exception) {
        }
        try {
            con.setAutoCommit(true);
        } catch (Exception exception1) {
        }
    }

    public void finalize() {
        if (con != null) {
            releaseConnection();
        }

    }

    public void close(ResultSet rs, PreparedStatement pstmt, Connection con) {
        try {
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
            if (con != null) {
                con.close();
                con = null;
            }
        } catch (Exception e) {
        }
    }

}
