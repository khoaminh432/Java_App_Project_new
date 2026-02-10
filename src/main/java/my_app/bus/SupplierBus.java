package my_app.bus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import my_app.dao.SupplierDao;
import my_app.model.Supplier;

public class SupplierBus implements GeneralConfig<Supplier> {

    private static final SupplierDao supplierDao = new SupplierDao();
    public static ArrayList<Supplier> listSuppliers = new ArrayList<>();
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
        if (name == null || name.isBlank()) {
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
}
