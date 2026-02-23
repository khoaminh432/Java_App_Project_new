package my_app.controller.btsinhvien;

import java.time.LocalDateTime;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import my_app.bus.StudentBus;
import my_app.model.StudentDTO;

public class btsinhvienController {

    private StudentBus studentbus = new StudentBus();

    @FXML
    private TableColumn<StudentDTO, String> colBirthDate;

    @FXML
    private TableColumn<StudentDTO, String> colHomeTown;

    @FXML
    private TableColumn<StudentDTO, Integer> colID;

    @FXML
    private TableColumn<StudentDTO, String> colName;

    @FXML
    private TableColumn<StudentDTO, String> colSurname;

    @FXML
    private TableView<StudentDTO> tblSinhVien;

    @FXML
    private TextField tfsearchQuery;

    @FXML
    private TextField tfsearcharray;

    @FXML
    private DatePicker dpBirthStudent;

    @FXML
    private TextField tfCodeStudent;

    @FXML
    private TextField tfHometownStudent;

    @FXML
    private TextField tfNameStudent;

    @FXML
    private TextField tfSurnameStudent;

    public void initialize() {

        setDataList();
        tblSinhVien.setItems(studentbus.filteredStudents);
        studentbus.viewAll();
        searchbar();
        setAllList();
        ActionTableStudent();
    }

    private void ClearFormStudent() {
        tfCodeStudent.clear();
        tfNameStudent.clear();
        tfSurnameStudent.clear();
        dpBirthStudent.setValue(null);
        tfHometownStudent.clear();
    }

    private void setFormStudent(StudentDTO student) {
        tfCodeStudent.setText(String.valueOf(student.getId()));
        tfNameStudent.setText(student.getName());
        tfSurnameStudent.setText(student.getSurname());
        dpBirthStudent.setValue(LocalDateTime.parse(student.getBirthdate()).toLocalDate());
        tfHometownStudent.setText(student.getHometown());
    }

    private StudentDTO getFormStudent() {
        StudentDTO student = new StudentDTO();
        student.setName(tfNameStudent.getText());
        student.setSurname(tfSurnameStudent.getText());
        student.setBirthdate(dpBirthStudent.getValue().toString());
        student.setHometown(tfHometownStudent.getText());
        return student;
    }

    private void ActionTableStudent() {
        tblSinhVien.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldval, newval) -> {
                    if (newval != null) {
                        setFormStudent(newval);
                    }
                }
        );
    }

    private void setAllList() {
        studentbus.viewAll();
        tblSinhVien.refresh();
    }

    private void searchbar() {
        tfsearcharray.textProperty().addListener((obs, oldvalue, newvalue) -> {
            this.studentbus.searchfromArray(newvalue);
        });
        tfsearchQuery.textProperty().addListener((obs, oldvalue, newvalue) -> {
            this.studentbus.searchfromQuery(newvalue);

        });
    }

    private void setDataList() {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        colBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthdate"));
        colHomeTown.setCellValueFactory(new PropertyValueFactory<>("hometown"));

    }

    @FXML
    private void handleAddStudent(ActionEvent event) {
        System.out.println("Add Student button clicked");
        studentbus.add(getFormStudent());
        System.out.println(studentbus.filteredStudents.size());
    }

    @FXML
    void handleCancelStudent(ActionEvent event) {
        ClearFormStudent();
    }

    @FXML
    void handleDeleteStudent(ActionEvent event) {
        StudentDTO selectedStudent = tblSinhVien.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            studentbus.delete(selectedStudent.getId());
            ClearFormStudent();
        }
    }

    @FXML
    void handleEditStudent(ActionEvent event) {
        StudentDTO selectedStudent = tblSinhVien.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            StudentDTO updatedStudent = getFormStudent();
            updatedStudent.setId(selectedStudent.getId());
            studentbus.update(updatedStudent);
            setAllList();
        }
    }
}
