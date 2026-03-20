package my_app.bus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import my_app.dao.InvoiceDetailDao;
import my_app.model.InvoiceDetail;

public class InvoiceDetailBus implements GeneralConfig<InvoiceDetail> {

    private static final InvoiceDetailDao invoiceDetailDao = new InvoiceDetailDao();
    public static ArrayList<InvoiceDetail> listInvoiceDetails = new ArrayList<>();
    private final ObservableList<InvoiceDetail> invoiceDetails;

    public InvoiceDetailBus() {
        this.invoiceDetails = FXCollections.observableArrayList();
    }

    public ObservableList<InvoiceDetail> getInvoiceDetails() {
        return invoiceDetails;
    }

    public List<InvoiceDetail> fetchAllFromDb() {
        return invoiceDetailDao.findAll();
    }

    public void replaceAll(List<InvoiceDetail> newDetails) {
        listInvoiceDetails.clear();
        if (newDetails != null) {
            listInvoiceDetails.addAll(newDetails);
        }
        syncObservable();
    }

    private void syncObservable() {
        invoiceDetails.setAll(listInvoiceDetails);
    }

    private boolean containsIgnoreCase(Number value, String keyword) {
        return value != null && value.toString().toLowerCase().contains(keyword);
    }

    private List<InvoiceDetail> filterByKeyword(List<InvoiceDetail> source, String keyword) {
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
                || containsIgnoreCase(detail.getProductId(), needle)
                || containsIgnoreCase(detail.getQuantity(), needle))
                .collect(Collectors.toList());
    }

    @Override
    public void findAll() {
        replaceAll(fetchAllFromDb());
    }

    @Override
    public void findById(int id) {
        InvoiceDetail detail = invoiceDetailDao.findById(id);
        listInvoiceDetails.clear();
        if (detail != null) {
            listInvoiceDetails.add(detail);
        }
        syncObservable();
    }

    @Override
    public void searchNameByArray(String name) {
        invoiceDetails.setAll(filterByKeyword(listInvoiceDetails, name));
    }

    @Override
    public void searchNameByDB(String name) {
        List<InvoiceDetail> refreshed = filterByKeyword(fetchAllFromDb(), name);
        listInvoiceDetails = new ArrayList<>(refreshed);
        syncObservable();
    }

    @Override
    public int create(InvoiceDetail obj) {
        int index = invoiceDetailDao.create(obj);
        findAll();
        return index;
    }

    @Override
    public int update(InvoiceDetail obj) {
        int index = invoiceDetailDao.update(obj);
        findAll();
        return index;
    }

    @Override
    public int delete(int id) {
        int index = invoiceDetailDao.delete(id);
        findAll();
        return index;
    }
}
