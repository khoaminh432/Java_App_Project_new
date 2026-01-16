package my_app.util;
import java.sql.Connection;
import java.sql.DriverManager;
import my_app.service.ConfigProperties;
public class DBConnection  {
    private final String url = ConfigProperties.getDbUrl();
    private final String user = ConfigProperties.getDbUser();
    private final String password = ConfigProperties.getDbPassword();
    private Connection conn;
    public Connection getConnection() throws Exception {
        return DriverManager.getConnection(this.url, this.user, this.password);
    }
    public DBConnection() {
        try {
            this.conn = getConnection();
            System.out.println("Kết nối DB thành công!");
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi kết nối đến cơ sở dữ liệu", e);
        }
    }
    public Connection getConn() {

        return this.conn;
    }
    public void close() {
        try {
            if (this.conn != null && !this.conn.isClosed()) {
                this.conn.close();
                System.out.println("Đóng kết nối DB thành công!");
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi đóng kết nối cơ sở dữ liệu", e);
        }
    }

    public static void main(String[] args) {
        DBConnection dbConn = new DBConnection();
        System.out.println("DB Connection: " + dbConn.getConn());
        dbConn.close();
    }
}