package my_app.bus;

import java.util.List;

import my_app.dao.OrderDAO;
import my_app.dto.OrderDTO;

public class OrderBUS {
    private OrderDAO dao = new OrderDAO();

    public List<OrderDTO> getAll() {
        return dao.getAll();
    }

    public void insert(OrderDTO o) {
        dao.insert(o);
    }

    public void delete(int id) {
        dao.delete(id);
    }
    public void update(OrderDTO o) {
    dao.update(o);
}
}