package my_app.util;
import java.sql.Connection;
import java.sql.DriverManager;

import my_app.service.ConfigProperties;
public class DBConnection  {
    private static DBConnection instance;
    
    private final String url = ConfigProperties.getDbUrl();
    private final String user = ConfigProperties.getDbUser();
    private final String password = ConfigProperties.getDbPassword();
    private Connection conn;
    private Connection getConnection() throws Exception {
        return DriverManager.getConnection(this.url, this.user, this.password);
    }
    private DBConnection() {
        try {
            this.conn = getConnection();
            System.out.println("Kết nối DB thành công (Singleton)");
        } catch (Exception e) {
            throw new RuntimeException("Không thể kết nối DB", e);
        }
    }
    public Connection connect() {
        return this.conn;
    }
    /** Lấy instance duy nhất */
    public static synchronized DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }
    public static void close() {
        try {
            if (instance.conn != null && !instance.conn.isClosed()) {
                instance.conn.close();
                System.out.println("Đóng kết nối DB thành công!");
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi đóng kết nối cơ sở dữ liệu", e);
        }
    }

    public static void main(String[] args) {
        DBConnection dbConn = DBConnection.getInstance();
        // Sử dụng kết nối
        Connection conn = dbConn.connect();
        // Đóng kết nối khi không còn sử dụng
        dbConn.close();
    }
}