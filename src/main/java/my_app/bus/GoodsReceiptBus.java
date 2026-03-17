package my_app.bus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import my_app.dao.GoodsReceiptDao;
import my_app.model.GoodsReceipt;

public class GoodsReceiptBus implements GeneralConfig<GoodsReceipt> {

    private static final GoodsReceiptDao goodsReceiptDao = new GoodsReceiptDao();
    public static ArrayList<GoodsReceipt> listGoodsReceipts = new ArrayList<>();
    private final ObservableList<GoodsReceipt> goodsReceipts;

    public GoodsReceiptBus() {
        this.goodsReceipts = FXCollections.observableArrayList();
    }

    public ObservableList<GoodsReceipt> getGoodsReceipts() {
        return goodsReceipts;
    }

    public List<GoodsReceipt> fetchAllFromDb() {
        return goodsReceiptDao.findAll();
    }

    public void replaceAll(List<GoodsReceipt> newReceipts) {
        listGoodsReceipts.clear();
        if (newReceipts != null) {
            listGoodsReceipts.addAll(newReceipts);
        }
        syncObservable();
    }

    private void syncObservable() {
        goodsReceipts.setAll(listGoodsReceipts);
    }

    private boolean containsIgnoreCase(String value, String keyword) {
        return value != null && value.toLowerCase().contains(keyword);
    }

    private boolean containsIgnoreCase(Number value, String keyword) {
        return value != null && value.toString().toLowerCase().contains(keyword);
    }

    private List<GoodsReceipt> filterByKeyword(List<GoodsReceipt> source, String keyword) {
        if (source == null || source.isEmpty()) {
            return Collections.emptyList();
        }
        if (keyword == null || keyword.isBlank()) {
            return new ArrayList<>(source);
        }
        String needle = keyword.toLowerCase();
        return source.stream()
                .filter(receipt -> containsIgnoreCase(receipt.getId(), needle)
                || containsIgnoreCase(receipt.getSupplierId(), needle)
                || containsIgnoreCase(receipt.getTotalQuantity(), needle)
                || containsIgnoreCase(receipt.getTotalPrice(), needle)
                || containsIgnoreCase(String.valueOf(receipt.getReceivedDate()), needle))
                .collect(Collectors.toList());
    }

    @Override
    public void findAll() {
        replaceAll(fetchAllFromDb());
    }

    @Override
    public void findById(int id) {
        GoodsReceipt receipt = goodsReceiptDao.findById(id);
        listGoodsReceipts.clear();
        if (receipt != null) {
            listGoodsReceipts.add(receipt);
        }
        syncObservable();
    }

    @Override
    public void searchNameByArray(String name) {
        goodsReceipts.setAll(filterByKeyword(listGoodsReceipts, name));
    }

    @Override
    public void searchNameByDB(String name) {
        List<GoodsReceipt> refreshed = filterByKeyword(fetchAllFromDb(), name);
        listGoodsReceipts = new ArrayList<>(refreshed);
        syncObservable();
    }

    @Override
    public int create(GoodsReceipt obj) {
        int index = goodsReceiptDao.create(obj);
        findAll();
        return index;
    }

    @Override
    public int update(GoodsReceipt obj) {
        int index = goodsReceiptDao.update(obj);
        findAll();
        return index;
    }

    @Override
    public int delete(int id) {
        int index = goodsReceiptDao.delete(id);
        findAll();
        return index;
    }
}
