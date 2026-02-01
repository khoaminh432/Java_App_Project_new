package my_app.bus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import my_app.dao.EmployeeDao;
import my_app.model.Employee;

public class EmployeeBus implements GeneralConfig<Employee> {

    private final EmployeeDao employeeDao = new EmployeeDao();
    public static ArrayList<Employee> listEmployees = new ArrayList<>();
    private final ObservableList<Employee> employees;

    public EmployeeBus() {
        this.employees = FXCollections.observableArrayList();
    }

    public ObservableList<Employee> getEmployees() {
        return employees;
    }

    public List<Employee> fetchAllFromDb() {
        return employeeDao.findAll();
    }

    public void replaceAll(List<Employee> newEmployees) {
        listEmployees.clear();
        if (newEmployees != null) {
            listEmployees.addAll(newEmployees);
        }
        syncObservable();
    }

    private void syncObservable() {
        employees.setAll(listEmployees);
    }

    private boolean containsIgnoreCase(String value, String keyword) {
        return value != null && value.toLowerCase().contains(keyword);
    }

    @Override
    public void findAll() {
        replaceAll(fetchAllFromDb());
    }

    @Override
    public void findById(int id) {
        Employee employee = employeeDao.findById(id);
        listEmployees.clear();
        if (employee != null) {
            listEmployees.add(employee);
        }
        syncObservable();
    }

    private List<Employee> filterByKeyword(List<Employee> source, String keyword) {
        if (source == null || source.isEmpty()) {
            return Collections.emptyList();
        }
        if (keyword == null || keyword.isBlank()) {
            return new ArrayList<>(source);
        }
        String needle = keyword.toLowerCase();
        return source.stream()
                .filter(employee -> containsIgnoreCase(employee.getFullName(), needle))
                .collect(Collectors.toList());
    }

    @Override
    public void searchNameByArray(String name) {
        employees.setAll(filterByKeyword(listEmployees, name));
    }

    @Override
    public void searchNameByDB(String name) {
        List<Employee> refreshed = filterByKeyword(employeeDao.findAll(), name);
        listEmployees = new ArrayList<>(refreshed);
        syncObservable();
    }

    @Override
    public int create(Employee obj) {
        int index = employeeDao.create(obj);
        findAll();
        return index;
    }

    @Override
    public int update(Employee obj) {
        int index = employeeDao.update(obj);
        findAll();
        return index;
    }

    @Override
    public int delete(int id) {
        int index = employeeDao.delete(id);
        findAll();
        return index;
    }
}
