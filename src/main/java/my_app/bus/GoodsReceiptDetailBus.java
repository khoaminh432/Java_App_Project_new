package my_app.bus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import my_app.dao.GoodsReceiptDetailDao;
import my_app.model.GoodsReceiptDetail;

public class GoodsReceiptDetailBus implements GeneralConfig<GoodsReceiptDetail> {

    private static final GoodsReceiptDetailDao goodsReceiptDetailDao = new GoodsReceiptDetailDao();
    public static ArrayList<GoodsReceiptDetail> listGoodsReceiptDetails = new ArrayList<>();
    private final ObservableList<GoodsReceiptDetail> goodsReceiptDetails;

    public GoodsReceiptDetailBus() {
        this.goodsReceiptDetails = FXCollections.observableArrayList();
    }

    public ObservableList<GoodsReceiptDetail> getGoodsReceiptDetails() {
        return goodsReceiptDetails;
    }

    public List<GoodsReceiptDetail> fetchAllFromDb() {
        return goodsReceiptDetailDao.findAll();
    }

    public void replaceAll(List<GoodsReceiptDetail> newDetails) {
        listGoodsReceiptDetails.clear();
        if (newDetails != null) {
            listGoodsReceiptDetails.addAll(newDetails);
        }
        syncObservable();
    }

    private void syncObservable() {
        goodsReceiptDetails.setAll(listGoodsReceiptDetails);
    }

    private boolean containsIgnoreCase(Number value, String keyword) {
        return value != null && value.toString().toLowerCase().contains(keyword);
    }

    private List<GoodsReceiptDetail> filterByKeyword(List<GoodsReceiptDetail> source, String keyword) {
        if (source == null || source.isEmpty()) {
            return Collections.emptyList();
        }
        if (keyword == null || keyword.isBlank()) {
            return new ArrayList<>(source);
        }
        String needle = keyword.toLowerCase();
        return source.stream()
                .filter(detail -> containsIgnoreCase(detail.getId(), needle)
                || containsIgnoreCase(detail.getReceiptId(), needle)
                || containsIgnoreCase(detail.getIngredientId(), needle)
                || containsIgnoreCase(detail.getNetWeight(), needle)
                || containsIgnoreCase(detail.getQuantity(), needle))
                .collect(Collectors.toList());
    }

    @Override
    public void findAll() {
        replaceAll(fetchAllFromDb());
    }

    @Override
    public void findById(int id) {
        GoodsReceiptDetail detail = goodsReceiptDetailDao.findById(id);
        listGoodsReceiptDetails.clear();
        if (detail != null) {
            listGoodsReceiptDetails.add(detail);
        }
        syncObservable();
    }

    @Override
    public void searchNameByArray(String name) {
        goodsReceiptDetails.setAll(filterByKeyword(listGoodsReceiptDetails, name));
    }

    @Override
    public void searchNameByDB(String name) {
        List<GoodsReceiptDetail> refreshed = filterByKeyword(fetchAllFromDb(), name);
        listGoodsReceiptDetails = new ArrayList<>(refreshed);
        syncObservable();
    }

    @Override
    public int create(GoodsReceiptDetail obj) {
        int index = goodsReceiptDetailDao.create(obj);
        findAll();
        return index;
    }

    @Override
    public int update(GoodsReceiptDetail obj) {
        int index = goodsReceiptDetailDao.update(obj);
        findAll();
        return index;
    }

    @Override
    public int delete(int id) {
        int index = goodsReceiptDetailDao.delete(id);
        findAll();
        return index;
    }
}
