package my_app.bus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import my_app.dao.GoodsReceiptDao;
import my_app.dao.GoodsReceiptDetailDao;
import my_app.dao.SupplierDao;
import my_app.model.Supplier;
import my_app.model.GoodsReceipt;
import my_app.model.GoodsReceiptDetail;

public class SupplierBus implements GeneralConfig<Supplier> {

    private static final SupplierDao supplierDao = new SupplierDao();
    private static final GoodsReceiptDetailDao receiptsDao = new GoodsReceiptDetailDao();
    private static final GoodsReceiptDao receiptDao = new GoodsReceiptDao();
    public ArrayList<Supplier> listSuppliers = new ArrayList<>();
    private final ObservableList<Supplier> suppliers;

    public SupplierBus() {
        this.suppliers = FXCollections.observableArrayList();
    }

    public ObservableList<Supplier> getSuppliers() {
        return suppliers;
    }

    public List<Supplier> fetchAllFromDb() {
        return supplierDao.findAll();
    }

    public List<Supplier> getTheDB() {
       return supplierDao.All();
    }

    public void replaceAll(List<Supplier> newSuppliers) {
        listSuppliers.clear();
        if (newSuppliers != null) {
            listSuppliers.addAll(newSuppliers);
        }
        syncObservable();
    }

    private void syncObservable() {
        suppliers.setAll(listSuppliers);
    }

    private List<Supplier> filterByName(List<Supplier> source, String name) {
        if (source == null || source.isEmpty()) {
            return Collections.emptyList();
        }
        if (name == null || name.trim().isEmpty()) {
            return new ArrayList<>(source);
        }
        String keyword = name.toLowerCase();
        List<Supplier> filtered = new ArrayList<>();
        for (Supplier supplier : source) {
            String target = supplier.getSupplierName();
            if (target != null && target.toLowerCase().contains(keyword)) {
                filtered.add(supplier);
            }
        }
        return filtered;
    }

    @Override
    public void findAll() {
        replaceAll(fetchAllFromDb());
    }

    @Override
    public void findById(int id) {
        Supplier supplier = supplierDao.findById(id);
        listSuppliers.clear();
        if (supplier != null) {
            listSuppliers.add(supplier);
        }
        syncObservable();
    }

    @Override
    public void searchNameByArray(String name) {
        suppliers.setAll(filterByName(listSuppliers, name));
    }

    @Override
    public void searchNameByDB(String name) {
        List<Supplier> refreshed = filterByName(fetchAllFromDb(), name);
        listSuppliers = new ArrayList<>(refreshed);
        syncObservable();
    }

    @Override
    public int create(Supplier obj) {
        int index = supplierDao.create(obj);
        findAll();
        return index;
    }

    @Override
    public int update(Supplier obj) {
        int index = supplierDao.update(obj);
        findAll();
        return index;
    }

    @Override
    public int delete(int id) {
        int index = supplierDao.delete(id);
        findAll();
        return index;
    }
    
    public int hardDelete(int id) {
    try {
        List<GoodsReceipt> allReceipts = receiptDao.findAll();
        List<GoodsReceiptDetail> allDetails = receiptsDao.findAll();

        List<GoodsReceipt> supplierReceipts = new ArrayList<>();
        for (GoodsReceipt r : allReceipts) {
            if (r.getSupplierId() == id) {
                supplierReceipts.add(r);
            }
        }

        List<GoodsReceiptDetail> receiptDetails = new ArrayList<>();
        for (GoodsReceiptDetail d : allDetails) {
            for (GoodsReceipt r : supplierReceipts) {
                if (d.getReceiptId() == r.getId()) {
                    receiptDetails.add(d);
                    break;
                }
            }
        }

        for (GoodsReceiptDetail d : receiptDetails) {
            receiptsDao.delete(d.getId());
        }

        for (GoodsReceipt r : supplierReceipts) {
            receiptDao.delete(r.getId());
        }

        return supplierDao.hardDelete(id);
    } catch (Exception e) {
        e.printStackTrace();
        return 0;
     }
    }
}
