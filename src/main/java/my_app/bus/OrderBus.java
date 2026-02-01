package my_app.bus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import my_app.dao.OrderDao;
import my_app.model.Order;

public class OrderBus implements GeneralConfig<Order> {

    private final OrderDao orderDao = new OrderDao();
    public static ArrayList<Order> listOrders = new ArrayList<>();
    private final ObservableList<Order> orders;

    public OrderBus() {
        this.orders = FXCollections.observableArrayList();
    }

    public ObservableList<Order> getOrders() {
        return orders;
    }

    public List<Order> fetchAllFromDb() {
        return orderDao.findAll();
    }

    public void replaceAll(List<Order> newOrders) {
        listOrders.clear();
        if (newOrders != null) {
            listOrders.addAll(newOrders);
        }
        syncObservable();
    }

    private void syncObservable() {
        orders.setAll(listOrders);
    }

    private boolean containsIgnoreCase(String value, String keyword) {
        return value != null && value.toLowerCase().contains(keyword);
    }

    private boolean containsIgnoreCase(Integer value, String keyword) {
        return value != null && value.toString().toLowerCase().contains(keyword);
    }

    private List<Order> filterByKeyword(List<Order> source, String keyword) {
        if (source == null || source.isEmpty()) {
            return Collections.emptyList();
        }
        if (keyword == null || keyword.isBlank()) {
            return new ArrayList<>(source);
        }
        String needle = keyword.toLowerCase();
        return source.stream()
                .filter(order -> containsIgnoreCase(order.getStatus(), needle)
                || containsIgnoreCase(order.getId(), needle)
                || containsIgnoreCase(order.getCustomerId(), needle))
                .collect(Collectors.toList());
    }

    @Override
    public void findAll() {
        replaceAll(fetchAllFromDb());
    }

    @Override
    public void findById(int id) {
        Order order = orderDao.findById(id);
        listOrders.clear();
        if (order != null) {
            listOrders.add(order);
        }
        syncObservable();
    }

    @Override
    public void searchNameByArray(String name) {
        orders.setAll(filterByKeyword(listOrders, name));
    }

    @Override
    public void searchNameByDB(String name) {
        List<Order> refreshed = filterByKeyword(orderDao.findAll(), name);
        listOrders = new ArrayList<>(refreshed);
        syncObservable();
    }

    @Override
    public int create(Order obj) {
        int index = orderDao.create(obj);
        findAll();
        return index;
    }

    @Override
    public int update(Order obj) {
        int index = orderDao.update(obj);
        findAll();
        return index;
    }

    @Override
    public int delete(int id) {
        int index = orderDao.delete(id);
        findAll();
        return index;
    }
}
