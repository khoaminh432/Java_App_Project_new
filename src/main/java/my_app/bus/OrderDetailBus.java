package my_app.bus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import my_app.dao.OrderDetailDao;
import my_app.model.OrderDetail;

public class OrderDetailBus implements GeneralConfig<OrderDetail> {

    private static final OrderDetailDao orderDetailDao = new OrderDetailDao();
    public static ArrayList<OrderDetail> listOrderDetails = new ArrayList<>();
    private final ObservableList<OrderDetail> orderDetails;

    public OrderDetailBus() {
        this.orderDetails = FXCollections.observableArrayList();
    }

    public ObservableList<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public List<OrderDetail> fetchAllFromDb() {
        return orderDetailDao.findAll();
    }

    public void replaceAll(List<OrderDetail> newDetails) {
        listOrderDetails.clear();
        if (newDetails != null) {
            listOrderDetails.addAll(newDetails);
        }
        syncObservable();
    }

    private void syncObservable() {
        orderDetails.setAll(listOrderDetails);
    }

    private boolean containsIgnoreCase(Number value, String keyword) {
        return value != null && value.toString().toLowerCase().contains(keyword);
    }

    private List<OrderDetail> filterByKeyword(List<OrderDetail> source, String keyword) {
        if (source == null || source.isEmpty()) {
            return Collections.emptyList();
        }
        if (keyword == null || keyword.isBlank()) {
            return new ArrayList<>(source);
        }
        String needle = keyword.toLowerCase();
        return source.stream()
                .filter(detail -> containsIgnoreCase(detail.getId(), needle))
                .collect(Collectors.toList());
    }

    @Override
    public void findAll() {
        replaceAll(fetchAllFromDb());
    }

    @Override
    public void findById(int id) {
        OrderDetail detail = orderDetailDao.findById(id);
        listOrderDetails.clear();
        if (detail != null) {
            listOrderDetails.add(detail);
        }
        syncObservable();
    }

    @Override
    public void searchNameByArray(String name) {
        orderDetails.setAll(filterByKeyword(listOrderDetails, name));
    }

    @Override
    public void searchNameByDB(String name) {
        List<OrderDetail> refreshed = filterByKeyword(fetchAllFromDb(), name);
        listOrderDetails = new ArrayList<>(refreshed);
        syncObservable();
    }

    @Override
    public int create(OrderDetail obj) {
        int index = orderDetailDao.create(obj);
        findAll();
        return index;
    }

    @Override
    public int update(OrderDetail obj) {
        int index = orderDetailDao.update(obj);
        findAll();
        return index;
    }

    @Override
    public int delete(int id) {
        int index = orderDetailDao.delete(id);
        findAll();
        return index;
    }
}
