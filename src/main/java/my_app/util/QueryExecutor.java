package my_app.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

public class QueryExecutor {

    private static final DBConnection dbConnectionInstance = DBConnection.getInstance();
    private final Connection dbConnection;

    public QueryExecutor() {
        this.dbConnection = dbConnectionInstance.connect();
    }

    public ArrayList<HashMap<String, Object>> ExecuteQuery(String query) {

        ArrayList<HashMap<String, Object>> list = new ArrayList<>();

        try {
            PreparedStatement preteble = dbConnection.prepareStatement(query);
            ResultSet rs = preteble.executeQuery();
            while (rs.next()) {
                HashMap<String, Object> result = new HashMap<>();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    result.put(rs.getMetaData().getColumnName(i), rs.getObject(i));
                }
                list.add(result);
            }
            rs.close();
            preteble.close();
            return list;
        } catch (Exception e) {
            System.err.println("Chi tiết lỗi truy vấn: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi thực thi truy vấn", e);
        }
    }

    // các bảng của sql khi vào java sẽ là HashMap<String,Object> và dạng arraylist để lưu nhiều bản ghi
    public ArrayList<HashMap<String, Object>> ExecuteQuery(String query, Object... params) {

        ArrayList<HashMap<String, Object>> list = new ArrayList<>();

        try {
            PreparedStatement preteble = dbConnection.prepareStatement(query);
            setParams(preteble, params);
            ResultSet rs = preteble.executeQuery();
            while (rs.next()) {
                HashMap<String, Object> result = new HashMap<>();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    result.put(rs.getMetaData().getColumnName(i), rs.getObject(i));
                }
                list.add(result);
            }
            return list;
        } catch (Exception e) {
            System.err.println("Chi tiết lỗi truy vấn (với params): " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi thực thi truy vấn", e);
        }
    }

    public int ExecuteUpdate(String query, Object... params) {
        try (PreparedStatement preteble = dbConnection.prepareStatement(query)) {
            setParams(preteble, params);
            return preteble.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi thực thi cập nhật", e);
        }
    }

    private void setParams(PreparedStatement ps, Object... params) throws Exception {
        if (params == null) {
            return;
        }
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
    }

    public int NextID(String tableName) {
        String query = "SELECT 1 AS next_missing_id\r\n"
                + //
                "WHERE NOT EXISTS (SELECT 1 FROM " + tableName + " WHERE id = 1)\r\n"
                + //
                "\r\n"
                + //
                "UNION\r\n"
                + //
                "\r\n"
                + //
                "SELECT t1.id + 1\r\n"
                + //
                "FROM " + tableName + " t1\r\n"
                + //
                "WHERE NOT EXISTS (\r\n"
                + //
                "    SELECT 1 FROM " + tableName + " t2 WHERE t2.id = t1.id + 1\r\n"
                + //
                ")\r\n"
                + //
                "ORDER BY next_missing_id\r\n"
                + //
                "LIMIT 1;";
        try (PreparedStatement preteble = dbConnection.prepareStatement(query); ResultSet rs = preteble.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("next_missing_id"); // Trả về ID tiếp theo
            } else {
                return 1; // Nếu không có bản ghi nào, bắt đầu từ ID 1
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy ID tiếp theo", e);
        }
    }
}
