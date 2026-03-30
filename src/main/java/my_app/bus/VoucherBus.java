package my_app.bus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import my_app.dao.VoucherDao;
import my_app.model.Voucher;

public class VoucherBus implements GeneralConfig<Voucher> {

    private static final VoucherDao voucherDao = new VoucherDao();
    public static ArrayList<Voucher> listVouchers = new ArrayList<>();
    private final ObservableList<Voucher> vouchers;

    public VoucherBus() {
        this.vouchers = FXCollections.observableArrayList();
    }

    public ObservableList<Voucher> getVouchers() {
        return vouchers;
    }

    public List<Voucher> fetchAllFromDb() {
        return voucherDao.findAll();
    }

    public void replaceAll(List<Voucher> newVouchers) {
        listVouchers.clear();
        if (newVouchers != null) {
            listVouchers.addAll(newVouchers);
        }
        syncObservable();
    }

    private void syncObservable() {
        vouchers.setAll(listVouchers);
    }

    private boolean containsIgnoreCase(String value, String keyword) {
        return value != null && value.toLowerCase().contains(keyword);
    }

    private boolean containsIgnoreCase(Number value, String keyword) {
        return value != null && value.toString().toLowerCase().contains(keyword);
    }

    private List<Voucher> filterByKeyword(List<Voucher> source, String keyword) {
        if (source == null || source.isEmpty()) {
            return Collections.emptyList();
        }
        if (keyword == null || keyword.isBlank()) {
            return new ArrayList<>(source);
        }
        String needle = keyword.toLowerCase();
        return source.stream()
                .filter(voucher -> containsIgnoreCase(voucher.getPromotionName(), needle)
                || containsIgnoreCase(voucher.getId(), needle)
                || containsIgnoreCase(String.valueOf(voucher.getStartDate()), needle)
                || containsIgnoreCase(String.valueOf(voucher.getEndDate()), needle))
                .collect(Collectors.toList());
    }

    @Override
    public void findAll() {
        replaceAll(fetchAllFromDb());
    }

    @Override
    public void findById(int id) {
        Voucher voucher = voucherDao.findById(id);
        listVouchers.clear();
        if (voucher != null) {
            listVouchers.add(voucher);
        }
        syncObservable();
    }

    @Override
    public void searchNameByArray(String name) {
        vouchers.setAll(filterByKeyword(listVouchers, name));
    }

    @Override
    public void searchNameByDB(String name) {
        List<Voucher> refreshed = filterByKeyword(fetchAllFromDb(), name);
        listVouchers = new ArrayList<>(refreshed);
        syncObservable();
    }

    @Override
    public int create(Voucher obj) {
        int index = voucherDao.create(obj);
        findAll();
        return index;
    }

    @Override
    public int update(Voucher obj) {
        int index = voucherDao.update(obj);
        findAll();
        return index;
    }

    @Override
    public int delete(int id) {
        int index = voucherDao.delete(id);
        findAll();
        return index;
    }
}
