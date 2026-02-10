package my_app.bus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import my_app.dao.InvoiceVoucherDetailDao;
import my_app.model.InvoiceVoucherDetail;

public class InvoiceVoucherDetailBus implements GeneralConfig<InvoiceVoucherDetail> {

    private static final InvoiceVoucherDetailDao invoiceVoucherDetailDao = new InvoiceVoucherDetailDao();
    public static ArrayList<InvoiceVoucherDetail> listInvoiceVoucherDetails = new ArrayList<>();
    private final ObservableList<InvoiceVoucherDetail> invoiceVoucherDetails;

    public InvoiceVoucherDetailBus() {
        this.invoiceVoucherDetails = FXCollections.observableArrayList();
    }

    public ObservableList<InvoiceVoucherDetail> getInvoiceVoucherDetails() {
        return invoiceVoucherDetails;
    }

    public List<InvoiceVoucherDetail> fetchAllFromDb() {
        return invoiceVoucherDetailDao.findAll();
    }

    public void replaceAll(List<InvoiceVoucherDetail> newDetails) {
        listInvoiceVoucherDetails.clear();
        if (newDetails != null) {
            listInvoiceVoucherDetails.addAll(newDetails);
        }
        syncObservable();
    }

    private void syncObservable() {
        invoiceVoucherDetails.setAll(listInvoiceVoucherDetails);
    }

    private boolean containsIgnoreCase(Number value, String keyword) {
        return value != null && value.toString().toLowerCase().contains(keyword);
    }

    private List<InvoiceVoucherDetail> filterByKeyword(List<InvoiceVoucherDetail> source, String keyword) {
        if (source == null || source.isEmpty()) {
            return Collections.emptyList();
        }
        if (keyword == null || keyword.isBlank()) {
            return new ArrayList<>(source);
        }
        String needle = keyword.toLowerCase();
        return source.stream()
                .filter(detail -> containsIgnoreCase(detail.getId(), needle)
                || containsIgnoreCase(detail.getInvoiceId(), needle)
                || containsIgnoreCase(detail.getVoucherId(), needle))
                .collect(Collectors.toList());
    }

    @Override
    public void findAll() {
        replaceAll(fetchAllFromDb());
    }

    @Override
    public void findById(int id) {
        InvoiceVoucherDetail detail = invoiceVoucherDetailDao.findById(id);
        listInvoiceVoucherDetails.clear();
        if (detail != null) {
            listInvoiceVoucherDetails.add(detail);
        }
        syncObservable();
    }

    @Override
    public void searchNameByArray(String name) {
        invoiceVoucherDetails.setAll(filterByKeyword(listInvoiceVoucherDetails, name));
    }

    @Override
    public void searchNameByDB(String name) {
        List<InvoiceVoucherDetail> refreshed = filterByKeyword(fetchAllFromDb(), name);
        listInvoiceVoucherDetails = new ArrayList<>(refreshed);
        syncObservable();
    }

    @Override
    public int create(InvoiceVoucherDetail obj) {
        int index = invoiceVoucherDetailDao.create(obj);
        findAll();
        return index;
    }

    @Override
    public int update(InvoiceVoucherDetail obj) {
        int index = invoiceVoucherDetailDao.update(obj);
        findAll();
        return index;
    }

    @Override
    public int delete(int id) {
        int index = invoiceVoucherDetailDao.delete(id);
        findAll();
        return index;
    }
}
