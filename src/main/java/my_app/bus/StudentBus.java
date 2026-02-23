package my_app.bus;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import my_app.dao.StudentDao;
import my_app.model.StudentDTO;

public class StudentBus {

    private StudentDao studentDao = new StudentDao();
    private static ObservableList<StudentDTO> studentsObservableCache;
    public static FilteredList<StudentDTO> filteredStudents;

    public StudentBus() {
        studentsObservableCache = FXCollections.observableArrayList();
        filteredStudents = new FilteredList<>(studentsObservableCache, s -> true);
    }

    public void viewAll() {
        setDataList(studentDao.findAll());
    }

    private void setDataList(ArrayList<StudentDTO> students) {
        this.studentsObservableCache.setAll(students);
    }

    public int add(StudentDTO student) {
        validate(student);
        studentsObservableCache.add(student);
        return studentDao.insert(student);
    }

    public int update(StudentDTO student) {
        validate(student);
        int index = studentsObservableCache.indexOf(student);
        if (index >= 0) {
            studentsObservableCache.set(index, student);
        }
        return studentDao.update(student);
    }

    public int delete(int id) {

        studentsObservableCache.removeIf(s -> s.getId() == id);
        return studentDao.delete(id);
    }

    public int searchfromArray(String search) {
        filteredStudents.setPredicate(student -> {
            return student.getName().toLowerCase().contains(search.toLowerCase())
                    || student.getSurname().toLowerCase().contains(search.toLowerCase());
        });
        return this.filteredStudents.size();
    }

    public int searchfromQuery(String search) {
        setDataList(studentDao.findbyName(search));
        return this.studentsObservableCache.size();
    }

    private void validate(StudentDTO student) {
        if (student.getName() == null || student.getName().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (student.getSurname() == null || student.getSurname().isEmpty()) {
            throw new IllegalArgumentException("Surname is required");
        }
        if (student.getBirthdate() == null || student.getBirthdate().isEmpty()) {
            throw new IllegalArgumentException("Birthdate is required");
        }
        if (student.getHometown() == null || student.getHometown().isEmpty()) {
            throw new IllegalArgumentException("Hometown is required");
        }
        if (student.getCreatetime() == null || student.getCreatetime().isEmpty()) {
            throw new IllegalArgumentException("Createtime is required");
        }
    }

}
