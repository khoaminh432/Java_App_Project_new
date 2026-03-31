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

    public static ArrayList<Customer> listCustomers = new ArrayList<>();
    private final ObservableList<Customer> customers;
    private final CustomerDao customerDao;

    public CustomerBus() {
        this.customers = FXCollections.observableArrayList();
        this.customerDao = new CustomerDao();
    }

    public ObservableList<Customer> getCustomers() {
        return customers;
    }

    // Hàm lấy tất cả khách hàng từ database
    private List<Customer> fetchAllFromDatabase() {
        List<Customer> result = new ArrayList<>();
        try {
            result = customerDao.findAll();
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy dữ liệu từ database: " + e.getMessage());
        }
        return result;
    }

    // Hàm tìm khách hàng theo ID
    private Customer findByIdInDatabase(int id) {
        Customer customer = null;
        try {
            customer = customerDao.findById(id);
        } catch (Exception e) {
            System.err.println("Lỗi khi tìm khách hàng ID " + id + ": " + e.getMessage());
        }
        return customer;
    }

    // Hàm tạo mới khách hàng
    private int createInDatabase(Customer obj) {
        int result = 0;
        try {
            result = customerDao.create(obj);
        } catch (Exception e) {
            System.err.println("Lỗi khi tạo khách hàng: " + e.getMessage());
        }
        return result;
    }

    // Hàm cập nhật khách hàng
    private int updateInDatabase(Customer obj) {
        int result = 0;
        try {
            result = customerDao.update(obj);
        } catch (Exception e) {
            System.err.println("Lỗi khi cập nhật khách hàng: " + e.getMessage());
        }
        return result;
    }

    // Hàm xóa khách hàng
    private int deleteInDatabase(int id) {
        int result = 0;
        try {
            result = customerDao.delete(id);
        } catch (Exception e) {
            System.err.println("Lỗi khi xóa khách hàng ID " + id + ": " + e.getMessage());
        }
        return result;
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
        replaceAll(fetchAllFromDatabase());
    }

    @Override
    public void findById(int id) {
        Customer found = null;
        for (Customer cus : listCustomers) {
            if (cus.getId() == id) {
                found = cus;
                break;
            }
        }
        listCustomers.clear();
        if (found != null) {
            listCustomers.add(found);
        }
        syncObservable();
    }

    @Override
    public void searchNameByArray(String name) {
        List<Customer> filtered = new ArrayList<>();
        if (name == null || name.isBlank()) {
            filtered.addAll(listCustomers);
        } else {
            String keyword = name.toLowerCase();
            for (Customer cus : listCustomers) {
                if (cus.getFullName() != null && cus.getFullName().toLowerCase().contains(keyword)) {
                    filtered.add(cus);
                }
            }
        }
        customers.setAll(filtered);
    }

    @Override
    public void searchNameByDB(String name) {
        searchNameByArray(name);
    }

    @Override
    public int create(Customer obj) {
        int index = createInDatabase(obj);
        if (index > 0 && obj != null) {
            listCustomers.add(obj);
            syncObservable();
        }
        return index;
    }

    @Override
    public int update(Customer obj) {
        int index = updateInDatabase(obj);
        if (index > 0 && obj != null) {
            for (int i = 0; i < listCustomers.size(); i++) {
                if (listCustomers.get(i).getId() == obj.getId()) {
                    listCustomers.set(i, obj);
                    break;
                }
            }
            syncObservable();
        }
        return index;
    }

    @Override
    public int delete(int id) {
        int index = deleteInDatabase(id);
        if (index > 0) {
            java.util.Iterator<Customer> iterator = listCustomers.iterator();
            while (iterator.hasNext()) {
                Customer cus = iterator.next();
                if (cus.getId() == id) {
                    iterator.remove();
                    break;
                }
            }
            syncObservable();
        }
        return index;
    }
}
