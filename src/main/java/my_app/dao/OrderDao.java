package my_app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import my_app.dto.OrderDTO;
import my_app.util.DBConnection;

public class OrderDao implements GenericDao<Order, Integer> {

    private static final String BASE_QUERY = "SELECT * FROM `order`";
    private final QueryExecutor qe = new QueryExecutor();
    private final static String TABLE_NAME = "order";

    public List<OrderDTO> getAll() {
        List<OrderDTO> list = new ArrayList<>();
        try {
            Connection conn = DBConnection.getInstance().connect();
            String sql = "SELECT * FROM orders";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                OrderDTO o = new OrderDTO();
                o.setId(rs.getInt("id"));
                o.setCustomer(rs.getString("customer"));
                o.setDate(rs.getString("order_date"));
                o.setStatus(rs.getString("status"));
                o.setTotal(rs.getString("total"));
                list.add(o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void insert(OrderDTO o) {
        try {
            Connection conn = DBConnection.getInstance().connect();
            String sql = "INSERT INTO orders(customer, order_date, status, total) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, o.getCustomer());
            ps.setString(2, o.getDate());
            ps.setString(3, o.getStatus());
            ps.setString(4, o.getTotal());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        try {
            Connection conn = DBConnection.getInstance().connect();
            String sql = "DELETE FROM orders WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void update(OrderDTO o) {
    try {
        Connection conn = DBConnection.getInstance().connect();
        String sql = "UPDATE orders SET customer=?, order_date=?, status=?, total=? WHERE id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, o.getCustomer());
        ps.setString(2, o.getDate());
        ps.setString(3, o.getStatus());
        ps.setString(4, o.getTotal());
        ps.setInt(5, o.getId());
        ps.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}