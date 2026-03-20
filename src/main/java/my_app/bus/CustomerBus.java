package my_app.bus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import my_app.dao.CustomerDao;
import my_app.model.Customer;

public class CustomerBus implements GeneralConfig<Customer> {

    private static final CustomerDao customerDao = new CustomerDao();
    public static ArrayList<Customer> listCustomers = new ArrayList<>();
    private final ObservableList<Customer> customers;

    public CustomerBus() {
        this.customers = FXCollections.observableArrayList();
    }

    public ObservableList<Customer> getCustomers() {
        return customers;
    }

    public List<Customer> fetchAllFromDb() {
        return customerDao.findAll();
    }

    public void replaceAll(List<Customer> newCustomers) {
        listCustomers.clear();
        if (newCustomers != null) {
            listCustomers.addAll(newCustomers);
        }
        syncObservable();
    }

    private void syncObservable() {
        customers.setAll(listCustomers);
    }

    private List<Customer> filterByName(List<Customer> source, String name) {
        if (source == null || source.isEmpty()) {
            return Collections.emptyList();
        }
        if (name == null || name.isBlank()) {
            return new ArrayList<>(source);
        }
        String keyword = name.toLowerCase();
        return source.stream()
                .filter(customer -> {
                    String target = customer.getFullName();
                    return target != null && target.toLowerCase().contains(keyword);
                })
                .collect(Collectors.toList());
    }

    @Override
    public void findAll() {
        replaceAll(fetchAllFromDb());
    }

    @Override
    public void findById(int id) {
        Customer customer = customerDao.findById(id);
        listCustomers.clear();
        if (customer != null) {
            listCustomers.add(customer);
        }
        syncObservable();
    }

    @Override
    public void searchNameByArray(String name) {
        customers.setAll(filterByName(listCustomers, name));
    }

    @Override
    public void searchNameByDB(String name) {
        List<Customer> refreshed = filterByName(customerDao.findAll(), name);
        listCustomers = new ArrayList<>(refreshed);
        syncObservable();
    }

    @Override
    public int create(Customer obj) {
        int index = customerDao.create(obj);
        findAll();
        return index;
    }

    @Override
    public int update(Customer obj) {
        int index = customerDao.update(obj);
        findAll();
        return index;
    }

    @Override
    public int delete(int id) {
        int index = customerDao.delete(id);
        findAll();
        return index;
    }
}
