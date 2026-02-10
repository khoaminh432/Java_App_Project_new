package my_app.bus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import my_app.dao.TimesheetDao;
import my_app.model.Timesheet;

public class TimesheetBus implements GeneralConfig<Timesheet> {

    private static final TimesheetDao timesheetDao = new TimesheetDao();
    public static ArrayList<Timesheet> listTimesheets = new ArrayList<>();
    private final ObservableList<Timesheet> timesheets;

    public TimesheetBus() {
        this.timesheets = FXCollections.observableArrayList();
    }

    public ObservableList<Timesheet> getTimesheets() {
        return timesheets;
    }

    public List<Timesheet> fetchAllFromDb() {
        return timesheetDao.findAll();
    }

    public void replaceAll(List<Timesheet> newTimesheets) {
        listTimesheets.clear();
        if (newTimesheets != null) {
            listTimesheets.addAll(newTimesheets);
        }
        syncObservable();
    }

    private void syncObservable() {
        timesheets.setAll(listTimesheets);
    }

    private boolean containsIgnoreCase(Number value, String keyword) {
        return value != null && value.toString().toLowerCase().contains(keyword);
    }

    private boolean containsIgnoreCase(String value, String keyword) {
        return value != null && value.toLowerCase().contains(keyword);
    }

    private List<Timesheet> filterByKeyword(List<Timesheet> source, String keyword) {
        if (source == null || source.isEmpty()) {
            return Collections.emptyList();
        }
        if (keyword == null || keyword.isBlank()) {
            return new ArrayList<>(source);
        }
        String needle = keyword.toLowerCase();
        return source.stream()
                .filter(timesheet -> containsIgnoreCase(timesheet.getId(), needle))
                .collect(Collectors.toList());
    }

    @Override
    public void findAll() {
        replaceAll(fetchAllFromDb());
    }

    @Override
    public void findById(int id) {
        Timesheet timesheet = timesheetDao.findById(id);
        listTimesheets.clear();
        if (timesheet != null) {
            listTimesheets.add(timesheet);
        }
        syncObservable();
    }

    @Override
    public void searchNameByArray(String name) {
        timesheets.setAll(filterByKeyword(listTimesheets, name));
    }

    @Override
    public void searchNameByDB(String name) {
        List<Timesheet> refreshed = filterByKeyword(fetchAllFromDb(), name);
        listTimesheets = new ArrayList<>(refreshed);
        syncObservable();
    }

    @Override
    public int create(Timesheet obj) {
        int index = timesheetDao.create(obj);
        findAll();
        return index;
    }

    @Override
    public int update(Timesheet obj) {
        int index = timesheetDao.update(obj);
        findAll();
        return index;
    }

    @Override
    public int delete(int id) {
        int index = timesheetDao.delete(id);
        findAll();
        return index;
    }
}
