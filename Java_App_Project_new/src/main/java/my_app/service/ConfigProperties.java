package my_app.service;
import java.io.InputStream;
import java.util.Properties;

public class ConfigProperties {
    private static  final Properties props = new Properties();
    static {
        try (InputStream is = ConfigProperties.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (is == null) {
                throw new RuntimeException("Không tìm thấy file config.properties");
            }
            props.load(is);

        } catch (Exception e) {
            throw new RuntimeException("Lỗi load config.properties", e);
        }
    }
    public static String getProperty(String key) {
        return props.getProperty(key);
    }
    public static String getDbUrl() {
        String host = props.getProperty("db.host");
        String port = props.getProperty("db.port");
        String name = props.getProperty("db.name");
        String params = props.getProperty("db.params");

        return String.format(
            "jdbc:mysql://%s:%s/%s?%s",
            host, port, name, params
        );
    }

    public static String getDbUser() {
        return props.getProperty("db.user");
    }
    public static String getDbPassword() {
        return props.getProperty("db.password");
    }
    public static void main(String[] args) {
        System.out.println("DB URL: " + getDbUrl());
        System.out.println("DB User: " + getDbUser());
        System.out.println("DB Password: " + getDbPassword());
    }
}