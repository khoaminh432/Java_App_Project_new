package my_app.bus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import my_app.dao.PaymentMethodDao;
import my_app.model.PaymentMethod;

public class PaymentMethodBus implements GeneralConfig<PaymentMethod> {

    private static final PaymentMethodDao paymentMethodDao = new PaymentMethodDao();
    public static ArrayList<PaymentMethod> listPaymentMethods = new ArrayList<>();
    private final ObservableList<PaymentMethod> paymentMethods;

    public PaymentMethodBus() {
        this.paymentMethods = FXCollections.observableArrayList();
    }

    public ObservableList<PaymentMethod> getPaymentMethods() {
        return paymentMethods;
    }

    public List<PaymentMethod> fetchAllFromDb() {
        return paymentMethodDao.findAll();
    }

    public void replaceAll(List<PaymentMethod> newMethods) {
        listPaymentMethods.clear();
        if (newMethods != null) {
            listPaymentMethods.addAll(newMethods);
        }
        syncObservable();
    }

    private void syncObservable() {
        paymentMethods.setAll(listPaymentMethods);
    }

    private boolean containsIgnoreCase(String value, String keyword) {
        return value != null && value.toLowerCase().contains(keyword);
    }

    private boolean containsIgnoreCase(Number value, String keyword) {
        return value != null && value.toString().toLowerCase().contains(keyword);
    }

    private List<PaymentMethod> filterByKeyword(List<PaymentMethod> source, String keyword) {
        if (source == null || source.isEmpty()) {
            return Collections.emptyList();
        }
        if (keyword == null || keyword.isBlank()) {
            return new ArrayList<>(source);
        }
        String needle = keyword.toLowerCase();
        return source.stream()
                .filter(method -> containsIgnoreCase(method.getMethodName(), needle))
                .collect(Collectors.toList());
    }

    @Override
    public void findAll() {
        replaceAll(fetchAllFromDb());
    }

    @Override
    public void findById(int id) {
        PaymentMethod method = paymentMethodDao.findById(id);
        listPaymentMethods.clear();
        if (method != null) {
            listPaymentMethods.add(method);
        }
        syncObservable();
    }

    @Override
    public void searchNameByArray(String name) {
        paymentMethods.setAll(filterByKeyword(listPaymentMethods, name));
    }

    @Override
    public void searchNameByDB(String name) {
        List<PaymentMethod> refreshed = filterByKeyword(fetchAllFromDb(), name);
        listPaymentMethods = new ArrayList<>(refreshed);
        syncObservable();
    }

    @Override
    public int create(PaymentMethod obj) {
        int index = paymentMethodDao.create(obj);
        findAll();
        return index;
    }

    @Override
    public int update(PaymentMethod obj) {
        int index = paymentMethodDao.update(obj);
        findAll();
        return index;
    }

    @Override
    public int delete(int id) {
        int index = paymentMethodDao.delete(id);
        findAll();
        return index;
    }
}
