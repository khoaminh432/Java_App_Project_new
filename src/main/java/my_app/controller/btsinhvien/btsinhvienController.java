package my_app.controller.btsinhvien;
import java.time.LocalDateTime;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
    private TableColumn<StudentDTO  , Integer> colID;

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

    private ObservableList<StudentDTO> studentList = FXCollections.observableArrayList();

    public void initialize() {

        setDataList();
        
        tblSinhVien.setItems(studentList);
        studentbus.viewAll();
        searchbar();
        setList();
    }
    private void setList(){
        studentList.setAll(studentbus.studentsCache);
        tblSinhVien.refresh();
    }

    private void searchbar(){
        tfsearcharray.textProperty().addListener((obs,oldvalue,newvalue)->{
            this.studentbus.searchfromArray(newvalue);
            setList();
        });
        tfsearchQuery.textProperty().addListener((obs,oldvalue,newvalue)->{
            this.studentbus.searchfromQuery(newvalue);
            setList();
        });
    }
    private void setDataList(){
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        colBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthdate"));
        colHomeTown.setCellValueFactory(new PropertyValueFactory<>("hometown"));
        
    }
    @FXML
    private void handleAddStudent(ActionEvent event) {
        System.out.println("Add Student button clicked");
        studentbus.add(new StudentDTO(0, "Nguyen Van A", "Nguyen", LocalDateTime.now().toString(), "2000-01-01", ""));
        setList();
        System.out.println(studentbus.studentsCache.size());
    }
}
