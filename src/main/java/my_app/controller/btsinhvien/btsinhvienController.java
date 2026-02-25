package my_app.controller.btsinhvien;

import java.time.LocalDate;
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
import my_app.service.AlertInformation;
import my_app.service.ValidateObject;

public class btsinhvienController {

    private StudentBus studentbus = new StudentBus();
    private StudentDTO TempStudent = new StudentDTO();

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
        TempStudent = new StudentDTO();
    }

    private void setFormStudent(StudentDTO student) {
        tfCodeStudent.setText(String.valueOf(student.getId()));
        tfNameStudent.setText(student.getName());
        tfSurnameStudent.setText(student.getSurname());
        dpBirthStudent.setValue(LocalDate.parse(student.getBirthdate()));
        tfHometownStudent.setText(student.getHometown());
        TempStudent = student;
    }

    private void getFormStudent() {

        TempStudent.setName(tfNameStudent.getText());
        TempStudent.setSurname(tfSurnameStudent.getText());
        if (dpBirthStudent.getValue() != null) {
            TempStudent.setBirthdate(dpBirthStudent.getValue().toString());
        }
        TempStudent.setHometown(tfHometownStudent.getText());
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
        try {
            getFormStudent();
            TempStudent.setCreatetime(LocalDateTime.now().toString());
            ValidateObject.isStudentDTOValid(TempStudent);
            studentbus.add(TempStudent);
            ClearFormStudent();
        } catch (IllegalArgumentException e) {
            AlertInformation.showErrorAlert("Lỗi dữ liệu", "Dữ liệu không hợp lệ", e.getMessage());
        }

    }

    @FXML
    private void handleCancelStudent(ActionEvent event) {
        ClearFormStudent();
    }

    @FXML
    private void handleDeleteStudent(ActionEvent event) {
        StudentDTO selectedStudent = tblSinhVien.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            studentbus.delete(selectedStudent.getId());
            ClearFormStudent();
        }
    }

    @FXML
    private void handleEditStudent(ActionEvent event) {
        StudentDTO selectedStudent = tblSinhVien.getSelectionModel().getSelectedItem();
        try {
            if (selectedStudent == null) {
                throw new IllegalArgumentException("Vui lòng chọn một học sinh để sửa");
            }
            getFormStudent();
            ValidateObject.isStudentDTOValid(TempStudent);
            TempStudent.setId(selectedStudent.getId());
            System.out.println("Updated Student: " + TempStudent);
            studentbus.update(TempStudent);
        } catch (IllegalArgumentException e) {
            AlertInformation.showErrorAlert("Lỗi dữ liệu", "Dữ liệu không hợp lệ", e.getMessage());
        }
    }
}
