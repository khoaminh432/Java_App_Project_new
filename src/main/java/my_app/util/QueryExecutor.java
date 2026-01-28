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
    public ArrayList<HashMap<String,Object>>  ExecuteQuery(String query)  {
        
        ArrayList<HashMap<String,Object>> list = new ArrayList<>();

        try {
            PreparedStatement preteble = dbConnection.prepareStatement(query);
            ResultSet rs = preteble.executeQuery();
            while (rs.next()) {
                HashMap<String,Object> result = new HashMap<>();
                for(int i=1;i<=rs.getMetaData().getColumnCount();i++)
                    result.put(rs.getMetaData().getColumnName(i),rs.getObject(i));
                list.add(result);
            }
        rs.close();
        preteble.close();
        return list;   
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi thực thi truy vấn", e);
        }
    }
    // các bảng của sql khi vào java sẽ là HashMap<String,Object> và dạng arraylist để lưu nhiều bản ghi
    public ArrayList<HashMap<String,Object>>  ExecuteQuery(String query,Object... params)  {
        
        ArrayList<HashMap<String,Object>> list = new ArrayList<>();

        try {
            PreparedStatement preteble = dbConnection.prepareStatement(query);
            setParams(preteble, params);
            ResultSet rs = preteble.executeQuery();
            while (rs.next()) {
                HashMap<String,Object> result = new HashMap<>();
                for(int i=1;i<=rs.getMetaData().getColumnCount();i++)
                    result.put(rs.getMetaData().getColumnName(i),rs.getObject(i));
                list.add(result);
            }
        return list;   
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi thực thi truy vấn", e);
        }
    }
    public int ExecuteUpdate(String query,Object... params){
        try(PreparedStatement preteble = dbConnection.prepareStatement(query)){
            setParams(preteble,params);
            return preteble.executeUpdate();
        }catch(Exception e){
            throw new RuntimeException("Lỗi khi thực thi cập nhật", e);
        }
    }
    private void setParams(PreparedStatement ps, Object... params) throws Exception {
        if (params == null) return;
        for (int i = 0; i < params.length; i++) 
            ps.setObject(i + 1, params[i]);
    }
}