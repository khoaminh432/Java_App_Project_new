package my_app.bus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import my_app.dao.OnlineOrderDao;
import my_app.model.OnlineOrder;

public class OnlineOrderBus implements GeneralConfig<OnlineOrder> {

    private static final OnlineOrderDao onlineOrderDao = new OnlineOrderDao();
    public static ArrayList<OnlineOrder> listOnlineOrders = new ArrayList<>();
    private final ObservableList<OnlineOrder> onlineOrders;

    public OnlineOrderBus() {
        this.onlineOrders = FXCollections.observableArrayList();
    }

    public ObservableList<OnlineOrder> getOnlineOrders() {
        return onlineOrders;
    }

    public List<OnlineOrder> fetchAllFromDb() {
        return onlineOrderDao.findAll();
    }

    public void replaceAll(List<OnlineOrder> newOrders) {
        listOnlineOrders.clear();
        if (newOrders != null) {
            listOnlineOrders.addAll(newOrders);
        }
        syncObservable();
    }

    private void syncObservable() {
        onlineOrders.setAll(listOnlineOrders);
    }

    private boolean containsIgnoreCase(String value, String keyword) {
        return value != null && value.toLowerCase().contains(keyword);
    }

    private boolean containsIgnoreCase(Number value, String keyword) {
        return value != null && value.toString().toLowerCase().contains(keyword);
    }

    private List<OnlineOrder> filterByKeyword(List<OnlineOrder> source, String keyword) {
        if (source == null || source.isEmpty()) {
            return Collections.emptyList();
        }
        if (keyword == null || keyword.isBlank()) {
            return new ArrayList<>(source);
        }
        String needle = keyword.toLowerCase();
        return source.stream()
                .filter(order -> containsIgnoreCase(order.getReceiverName(), needle))
                .collect(Collectors.toList());
    }

    @Override
    public void findAll() {
        replaceAll(fetchAllFromDb());
    }

    @Override
    public void findById(int id) {
        OnlineOrder order = onlineOrderDao.findById(id);
        listOnlineOrders.clear();
        if (order != null) {
            listOnlineOrders.add(order);
        }
        syncObservable();
    }

    @Override
    public void searchNameByArray(String name) {
        onlineOrders.setAll(filterByKeyword(listOnlineOrders, name));
    }

    @Override
    public void searchNameByDB(String name) {
        List<OnlineOrder> refreshed = filterByKeyword(fetchAllFromDb(), name);
        listOnlineOrders = new ArrayList<>(refreshed);
        syncObservable();
    }

    @Override
    public int create(OnlineOrder obj) {
        int index = onlineOrderDao.create(obj);
        findAll();
        return index;
    }

    @Override
    public int update(OnlineOrder obj) {
        int index = onlineOrderDao.update(obj);
        findAll();
        return index;
    }

    @Override
    public int delete(int id) {
        int index = onlineOrderDao.delete(id);
        findAll();
        return index;
    }
}
