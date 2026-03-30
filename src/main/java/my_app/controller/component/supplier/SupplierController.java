package my_app.controller.component.supplier;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.time.LocalDate;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;


import javafx.animation.FadeTransition;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.*;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

// Apache POI (Excel)
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import my_app.model.Supplier;
import my_app.model.GoodsReceipt;
import my_app.model.GoodsReceiptDetail;
import my_app.bus.SupplierBus;
import my_app.bus.GoodsReceiptDetailBus;
import my_app.bus.GoodsReceiptBus;


public class SupplierController {

    //-table
    @FXML
    private TableView<Supplier> supplierTable;
    @FXML
    private TableColumn<Supplier, Integer> idColumn;
    @FXML
    private TableColumn<Supplier, String> nameColumn;
    @FXML
    private TableColumn<Supplier, String> phoneColumn;
    @FXML
    private TableColumn<Supplier, String> addressColumn;
    @FXML
    private TableColumn<Supplier, Double> spentColumn;
    @FXML
    private TableColumn<Supplier, String> activeColumn;
    @FXML
    private TableColumn<Supplier, String> transColumn;
    @FXML
    private TableColumn<Supplier, Void> actionColumn;
    //-2nd Table
    /*@FXML 
    private TableView<Supplier> supplierProduct;
    @FXML
    private TableColumn<Supplier, Integer> idProductColumn;
    @FXML
    private TableColumn<Supplier, String> ProductColumn;
    @FXML
    private TableColumn<Supplier, Integer> PriceColumn; */
    //-label 
    @FXML private Label total;  @FXML private Label totalD;
    @FXML private Label active;  @FXML private Label activeD; 
    @FXML private Label spent;  @FXML private Label spentD;
    @FXML private Label remain;
    @FXML private Label trans;  @FXML private Label transD;
    //-txtfield
    @FXML private TextField search;
    @FXML private TextField txtId;
    @FXML private TextField txtName;
    @FXML private TextField txtAddress;
    @FXML private TextField txtPhone;
    //-btn
    @FXML private Button btnAdd;
    @FXML private Button btnTheme;
    @FXML private Button btnReceiptList; //no use
    @FXML private Button btnClear; //no use
    @FXML private Button btnReload; //no use 
    @FXML private Button btnImport;
    @FXML private Button btnExport;
    //-comboBox.
    @FXML private ComboBox<String> sort;
    @FXML private BorderPane rootPane;

    
    private final  SupplierBus bus = new SupplierBus();
    private final  GoodsReceiptDetailBus RDBus= new GoodsReceiptDetailBus();
    private final  GoodsReceiptBus RBus= new GoodsReceiptBus();
    private  List<GoodsReceiptDetail> List= RDBus.fetchAllFromDb();
    private  List<GoodsReceipt> Parent= RBus.fetchAllFromDb();
    private ObservableList<Supplier> obser;
    public LocalDate time; 
    private static HashMap<String, Integer> supplierStats = new HashMap<>();
    static {
     supplierStats.put("total", 0);
     supplierStats.put("status", 0);
     supplierStats.put("trans", 0);
    } 
    

    @FXML
    private void initialize() {
        loadData();
        loadEvent();
    }

    private List<Supplier> CacheDB() {
     return bus.fetchAllFromDb();
    }
    
    private void loadTableData() {
     obser= FXCollections.observableArrayList(CacheDB());
     supplierTable.setItems(obser);
    }

    private void refreshData() {
      List = RDBus.fetchAllFromDb();
      Parent = RBus.fetchAllFromDb();
    }
    
    private int checkValidInput() {
      String test;
      test = txtName.getText();
      if (test==null||test.trim().isEmpty()) { return 1;}
      test = txtAddress.getText();
      if (test==null||test.trim().isEmpty()) { return 2;}
      test = txtPhone.getText();
      if (test==null||test.trim().isEmpty() || !test.matches("\\d+")) { return 3;}
      test = txtId.getText();
      if (test==null||test.trim().isEmpty()) { return 4;}
      return 0;
    }
    private void loadData() {
     configureColumns();
     loadTableData();
    }
    
    private void loadEvent() {
     searchBar();
     calculateStatistic();
     setMouseClickTableEvent();
     configComboBox();
    }

    private void configComboBox() {
     sort.getItems().addAll("least Purchase","most Purchase","Active","unActive","show Deleted","show All");
     sort.setValue("least Purchase");//default
     sort.setOnAction(e -> {
      String chose= sort.getValue();
      if(chose.equals("least Purchase")) {sortLeast();}
      else if(chose.equals("most Purchase")) {sortMost();}
      else if(chose.equals("Active")) {sortActive();}
      else if(chose.equals("unActive")) {sortInActive();}
      else if(chose.equals("show Deleted")) {sortDeleted();}
      else if(chose.equals("show All")) {sortAll();}
     });
    }

    private void sortLeast() {
     HashMap<Integer, Double> map= calSpent();
     ObservableList<Supplier> obserMap= FXCollections.observableArrayList();
     List<Map.Entry<Integer,Double>> list = new ArrayList<>(map.entrySet());
     Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {
      @Override
      public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {
       return o1.getValue().compareTo(o2.getValue());
      }
     }); 
      for(Map.Entry<Integer, Double> entry :list) {
       bus.findById(entry.getKey());
       if (!bus.listSuppliers.isEmpty()) {
        Supplier s= bus.listSuppliers.get(0); 
        obserMap.add(s);
       }
      }
      obser.setAll(obserMap);
    } 

    private void sortMost() {
     HashMap<Integer, Double> map= calSpent();
     ObservableList<Supplier> obserMap= FXCollections.observableArrayList();
     List<Map.Entry<Integer,Double>> list = new ArrayList<>(map.entrySet());
     Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {
      @Override
      public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {
       return o2.getValue().compareTo(o1.getValue());
      }
     }); 
      for(Map.Entry<Integer, Double> entry :list) {
       bus.findById(entry.getKey());
       if (!bus.listSuppliers.isEmpty()) {
        Supplier s= bus.listSuppliers.get(0); 
        obserMap.add(s);
       }
      }
      obser.setAll(obserMap);
    } 

   private void sortActive() {
    HashMap<Integer, Boolean> map= calRemain();
    ObservableList<Supplier> obserMap= FXCollections.observableArrayList();
    for(Map.Entry<Integer, Boolean> entry: map.entrySet()) {
     if (entry.getValue()) {
     bus.findById(entry.getKey());
     if (!bus.listSuppliers.isEmpty()) {
      Supplier s= bus.listSuppliers.get(0);
      obserMap.add(s);
      }
     }
    }
    obser.setAll(obserMap);
    }

   private void sortInActive() {
    HashMap<Integer, Boolean> map= calRemain();
    ObservableList<Supplier> obserMap= FXCollections.observableArrayList();
    for(Map.Entry<Integer, Boolean> entry: map.entrySet()) {
     if (!entry.getValue()) {
     bus.findById(entry.getKey());
     if (!bus.listSuppliers.isEmpty()) {
      Supplier s= bus.listSuppliers.get(0);
      obserMap.add(s);
      }
     }
    }
    obser.setAll(obserMap);
    }

   private void sortDeleted() {
    ObservableList<Supplier> obserMap= FXCollections.observableArrayList();
    List<Supplier> list= bus.getTheDB();
    for( Supplier s :list) {
      if( s.getStatus() == 0) {
       obserMap.add(s);
      }
     }
     obser.setAll(obserMap);
    }

   private void sortAll() {
     ObservableList<Supplier> obserMap= FXCollections.observableArrayList(bus.getTheDB());
     obser.setAll(obserMap);
    }
    
    @FXML
    private void handleUpdate() {
     Alert e =new Alert(Alert.AlertType.INFORMATION);
     int check= checkValidInput();
     if(check==1) {
      showAlert("warning", "name ko duoc de trong");
      return;
     }
     if(check==2) {
      showAlert("warning", "address ko duoc de trong");
      return;
     }
     if(check==3) {
      showAlert("warning", "loi, sdt co chua ky tu hoac sdt rong");
      return;
     }
     if(check==4) {
      showAlert("warning", "chua chon mot supplier");
      return;
     }
     int test= update( Integer.parseInt(txtId.getText()) ,txtName.getText(), txtAddress.getText(), txtPhone.getText());
     if(test == 0) {
     showAlert("warning", "ko the change supplier");
     } else {
     loadTableData();
     handleLabel();
     showAlert("Thong bao", "supplier changed succesfully");
     }
    }

    private void handleDelete(int id) {
     int test = delete(id);
     if(test==0) { 
       showAlert("Error", "error, cant delete supplier");
       }
     else {
       handleMap();
       loadTableData();
       handleLabel();
       showAlert("OK", "deleted a supplier");
       }
    }

    private void handleHardDelete(int id) {
     int test = hardDelete(id);
     if(test==0) { 
       showAlert("Error", "error, cant delete supplier"); 
       }
     else {
       refreshData();
       handleMap();
       loadTableData();
       handleLabel();
       showAlert("OK", "deleted a supplier");
       }
    }
    
    private void setMouseClickTableEvent() {
     supplierTable.getSelectionModel().selectedItemProperty().addListener(
      (observable, oldValue, newValue) -> { if(newValue!=null) { 
       txtId.setText(String.valueOf(newValue.getId()));
       txtName.setText(newValue.getSupplierName());
       txtPhone.setText(newValue.getPhoneNumber());
       txtAddress.setText(newValue.getAddress());
      } }
     ); 
    }
    
    private void configureColumns() {
        handleMap();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        actionColumn.setCellFactory(param -> new TableCell<Supplier, Void>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            {
                editBtn.setStyle("-fx-background-color: #48bb78; -fx-text-fill: white; -fx-cursor: hand;");
                editBtn.setOnAction(event -> {
                    Supplier s = getTableView().getItems().get(getIndex());
                    editDialog(s);
                });
                deleteBtn.setStyle("-fx-background-color: #f56565; -fx-text-fill: white; -fx-cursor: hand;");
                deleteBtn.setOnAction(event -> {
                    Supplier s = getTableView().getItems().get(getIndex());
                    handleDelete(s.getId());
                });
            }
             @Override
             protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(5, editBtn, deleteBtn);
                    setGraphic(buttons);
                }
            }
     } );
    }
    private void searchBar() {
     search.textProperty().addListener((observable, oldValue, newValue) -> {
      if(newValue!=null) {
       String s= newValue.trim();
      try {
          int id = Integer.parseInt(s);
          searchIDSupplier(id);
      } catch (NumberFormatException e) {
          searchNameSupplier(s);
       }
      }
     });
    }
    
    @FXML 
    private void theme() {
     if (rootPane.getStyleClass().contains("dark")) {
        rootPane.getStyleClass().remove("dark");
        btnTheme.setText("🌙 DARK THEME");
     } else {
        rootPane.getStyleClass().add("dark");
        btnTheme.setText("☀️ LIGHT THEME");
     }
    }
    
    @FXML
    private void clearForm() {
        txtName.clear();
        txtAddress.clear();
        txtPhone.clear();
        txtId.clear();
        loadTableData();
    }
    
    @FXML
    private void reloadTable() {
     loadTableData();
    } 

    private void showAlert(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
    }

    private boolean confirmAlert(String title, String message) {
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle(title);
      alert.setHeaderText(null);
      alert.setContentText(message);

      Optional<ButtonType> result = alert.showAndWait();
      return result.isPresent() && result.get() == ButtonType.OK;
    }

    @FXML
  private void addDialog() {
    Dialog<Supplier> log = new Dialog<>();
    log.setTitle("Add supplier");
    ButtonType add = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
    log.getDialogPane().getButtonTypes().addAll(add, ButtonType.CANCEL);
    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    TextField txt_Name = new TextField();
    TextField txt_Address = new TextField();
    TextField txt_Phone = new TextField();
    grid.add(new Label("Name"), 0, 0);
    grid.add(txt_Name, 1, 0);
    grid.add(new Label("Address"), 0, 1);
    grid.add(txt_Address, 1, 1);
    grid.add(new Label("Phone"), 0, 2);
    grid.add(txt_Phone, 1, 2);
    log.getDialogPane().setContent(grid);

    Button btn = (Button) log.getDialogPane().lookupButton(add);
    btn.addEventFilter(ActionEvent.ACTION, event -> {
        String name = txt_Name.getText().trim();
        String address = txt_Address.getText().trim();
        String phone = txt_Phone.getText().trim();

        if (name.isEmpty() || address.isEmpty() || phone.isEmpty()) {
            showAlert("Loi", "Khong đuoc đe trong!");
            event.consume();
            return;
        }
        if (!phone.matches("\\d+")) {
            showAlert("Loi", "So đien thoai phai la so!");
            event.consume();
            return;
        }
    });

    log.setResultConverter(btnTpe -> {
        if (btnTpe == add) {
            return new Supplier(null,
                    txt_Name.getText(),
                    txt_Address.getText(),
                    txt_Phone.getText());
        }
        return null;
    });
    Optional<Supplier> result = log.showAndWait();
    result.ifPresent(s -> {
        int success = add(s.getSupplierName(), s.getAddress(), s.getPhoneNumber());
        if (success == 1) {
            handleMap();
            loadTableData();
            handleLabel();
            showAlert("Thanh cong", "Them supplier thanh cong!");
        } else {
            showAlert("Loi", "Insert that bai!");
        }
      });
    }

 private void editDialog(Supplier s) {
    Dialog<String> dialog = new Dialog<>();
    dialog.setTitle("Edit Supplier");
    ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
    ButtonType deleteBtn = new ButtonType("Hard Delete", ButtonBar.ButtonData.OTHER);
    dialog.getDialogPane().getButtonTypes().addAll(saveBtn, deleteBtn, ButtonType.CANCEL);
    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    TextField txt_Name = new TextField(s.getSupplierName());
    TextField txt_Address = new TextField(s.getAddress());
    TextField txt_Phone = new TextField(s.getPhoneNumber());
    grid.add(new Label("Name"), 0, 0);
    grid.add(txt_Name, 1, 0);
    grid.add(new Label("Address"), 0, 1);
    grid.add(txt_Address, 1, 1);
    grid.add(new Label("Phone"), 0, 2);
    grid.add(txt_Phone, 1, 2);
    dialog.getDialogPane().setContent(grid);
    Button btnSave = (Button) dialog.getDialogPane().lookupButton(saveBtn);
    btnSave.addEventFilter(ActionEvent.ACTION, event -> {
        String name = txt_Name.getText().trim();
        String address = txt_Address.getText().trim();
        String phone = txt_Phone.getText().trim();
        if (name.isEmpty() || address.isEmpty() || phone.isEmpty()) {
            showAlert("Loi", "Khong đuoc đe trong!");
            event.consume();
            return;
        }
        if (!phone.matches("\\d+")) {
            showAlert("Loi", "SĐT phai la so!");
            event.consume();
         }
      });
      Button btnDelete = (Button) dialog.getDialogPane().lookupButton(deleteBtn);
      btnDelete.addEventFilter(ActionEvent.ACTION, event -> {
        boolean confirm = confirmAlert("Xac nhan", "Ban co chac muon xoa?");
        if (!confirm) {
            event.consume();
            return;
        }
        handleHardDelete(s.getId());
        dialog.close();
      });
      dialog.setResultConverter(btn -> {
        if (btn == saveBtn) {
            s.setName(txt_Name.getText());
            s.setAddress(txt_Address.getText());
            s.setPhone(txt_Phone.getText());
            return "save";
        }
        return null;
      });
      Optional<String> result = dialog.showAndWait();
      result.ifPresent(res -> {
        if (res.equals("save")) {
            int success = update(
                    s.getId(),
                    s.getSupplierName(),
                    s.getAddress(),
                    s.getPhoneNumber()
            );
            if (success == 1) {
                loadTableData();
                showAlert("OK", "Update thanh cong!");
            } else {
                showAlert("Loi", "Update that bai!");
            }
          }
        });
      }
    
    private void searchIDSupplier(int id) {
     bus.findById(id);
     obser= FXCollections.observableArrayList(bus.getSuppliers());
     supplierTable.setItems(obser);
    }
    private void searchNameSupplier(String name) {
     bus.searchNameByDB(name);
     obser= FXCollections.observableArrayList(bus.getSuppliers());
     supplierTable.setItems(obser);
    }
    private void searchNameByArray(String name) {
     bus.searchNameByArray(name);
     obser= FXCollections.observableArrayList(bus.getSuppliers());
     supplierTable.setItems(obser);
    }
    
    private int add(String Name, String Address, String Phone) {
     Supplier s= new Supplier(null ,Name,Address,Phone);
     return bus.create(s);
    }
    
    private int update(int id, String Name, String Address, String Phone) {
     Supplier s= new Supplier(id ,Name,Address,Phone);
     return bus.update(s);
    }
    
    private int delete(int id) {
     return bus.delete(id);
    }
    private int hardDelete(int id) {
     return bus.hardDelete(id);
    }
    
    private void calculateStatistic() {
     handleLabel();
    }
    
    // convert dữ liệu
    private double toDouble(BigDecimal price) {
        return price == null ? 0d : price.doubleValue();
    }
    private Double toDouble(Integer weight) {
        return weight == null ? 0d : weight.doubleValue();
    }

    /*
    private void loadSampleData() {
        supplierData.add(new Supplier(1, "Công ty TNHH ABC", "0123 456 789", "123 Đường Láng, HN", 12500.00, "Active"));
        supplierData.add(new Supplier(2, "DNTN XYZ", "0987 654 321", "456 Nguyễn Trãi, HCM", 8700.50, "Active"));
        supplierData.add(new Supplier(3, "Tập đoàn EFG", "0912 345 678", "789 Cầu Giấy, HN", 0.00, "Inactive"));
        supplierData.add(new Supplier(4, "Cửa hàng Thực phẩm", "0933 222 111", "321 Hai Bà Trưng, ĐN", 4300.75, "Active"));
    }*/

    private void handleLabel() {
     HashMap<Integer,Double> costMap= calSpent();
     HashMap<Integer,Boolean> remainMap= calRemain();
     HashMap<Integer,Integer> transMap= calTrans();
     double sum=0;
     for(double val: costMap.values()) {sum +=val;}
     spent.setText(sum +"$"); sum=0;
     for(boolean val: remainMap.values()) {
     if(val){ sum +=1; }
     }
     int all= CacheDB().size();
     double last= all-sum;
     active.setText(sum + " Supplier");
     remain.setText(last +" Supplier not used"); sum=0;
     for(int val: transMap.values()) { sum+=val; }
     trans.setText(sum +" Transmitions"); sum=0;
     total.setText(String.valueOf(calTotalSupplier()));
    }
    
    private int calTotalSupplier() {
     return CacheDB().size();
    }
    private HashMap<Integer,Integer> calTrans() {
     HashMap<Integer,Integer> map= new HashMap<>();
     List<GoodsReceipt> parent= Parent;
     int current;
     for (GoodsReceipt g: parent) {
      current =map.getOrDefault(g.getSupplierId(),0);
               map.put(g.getSupplierId(),current+1);
     }
     return map;
    }

    private HashMap<Integer,Boolean> calRemain() {
     HashMap<Integer,Boolean> map= new HashMap<>();
     List<Supplier> sup= CacheDB();
     List<GoodsReceipt> parent= Parent;
     for (GoodsReceipt g: parent) {
      map.put(g.getSupplierId(),true);
     }
     for (Supplier s: sup) {
      map.putIfAbsent(s.getId(), false);
     }
     return map;
    }

    private HashMap<Integer,Double> calSpent() {
     HashMap<Integer,Double> map= new HashMap<>();
     List<GoodsReceiptDetail> list= List;
     List<GoodsReceipt> parent= Parent;
     Map<Integer,List<GoodsReceiptDetail>> detailMap= new HashMap<>();
     for(GoodsReceiptDetail r: list) {
      int receiptId= r.getReceiptId();
      detailMap.computeIfAbsent(receiptId, k->new ArrayList<>()).add(r);
     }
     for (GoodsReceipt g : parent) {
     int supplierId = g.getSupplierId();
     int receiptId = g.getId();
     List<GoodsReceiptDetail> details = detailMap.get(receiptId);
     if (details != null) {
     for (GoodsReceiptDetail r : details) {
     double cost = r.getQuantity() * toDouble(r.getUnitPrice());
     double current =map.getOrDefault(supplierId, 0.0);
               map.put(supplierId, current+cost);
          }
        }
      }
     return map; 
    }

    private void handleMap() {
     HashMap<Integer,Double> map= calSpent();
     HashMap<Integer,Boolean> remain= calRemain();
     HashMap<Integer,Integer> trans= calTrans();
     spentColumn.setCellValueFactory(cell -> {
      Supplier s=cell.getValue();
      double cost= map.getOrDefault(s.getId(),0.0);
      return new ReadOnlyObjectWrapper<>(cost);
     });
     spentColumn.setCellFactory(cell -> new TableCell<Supplier, Double>() {
     @Override
     protected void updateItem(Double item, boolean empty) {
     super.updateItem(item, empty);
     if (empty || item == null) {
         setText(null);
     } else {
         setText(String.format("%.2f$", item));
         }
       }
     });
     activeColumn.setCellValueFactory(cell -> {
      Supplier s= cell.getValue();
      boolean active= remain.getOrDefault(s.getId(),false);
      String text= active ? "isUsed" : "notUsed";
      return new ReadOnlyObjectWrapper<>(text);
     });
     activeColumn.setCellFactory(cell-> 
     new TableCell<Supplier,String>() {
      @Override
      protected void updateItem(String item, boolean empty) {
       super.updateItem(item,empty);
       if(empty||item==null) {setText(null); setStyle("");}
       else{ setText(item); 
       if(item.equals("isUsed")) { setStyle("-fx-text-fill: green;");}
       else{ setStyle("-fx-text-fill: red;");}
       }
      }
     });
     transColumn.setCellValueFactory(cell -> {
      Supplier s= cell.getValue();
      int travel= trans.getOrDefault(s.getId(),0);
      return new SimpleStringProperty(String.valueOf(travel)+" times");
     });
    }
  
@FXML
private void handleImport() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Chọn file Excel để import");
    fileChooser.getExtensionFilters().add(
        new FileChooser.ExtensionFilter("Excel files", "*.xlsx", "*.xls")
    );

    File selectedFile = fileChooser.showOpenDialog(btnImport.getScene().getWindow());

    if (selectedFile != null) {
        try (FileInputStream fis = new FileInputStream(selectedFile)) {

            Workbook workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheetAt(0);

            int success = 0;

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // bỏ header

                String name = row.getCell(0).getStringCellValue();
                String address = row.getCell(1).getStringCellValue();
                String phone = row.getCell(2).getStringCellValue();

                Supplier s = new Supplier(null, name, address, phone);

                int result = bus.create(s);
                if (result != 0) success++;
            }

            workbook.close();
            handleMap();
            loadTableData(); // reload lại table

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setContentText("Import thành công " + success + " supplier");
            alert.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setContentText("Import thất bại!");
            alert.show();
        }
    }
}

@FXML
private void handleExport() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Lưu file Excel");
    fileChooser.getExtensionFilters().add(
        new FileChooser.ExtensionFilter("Excel files", "*.xlsx")
    );

    File file = fileChooser.showSaveDialog(btnExport.getScene().getWindow());

    if (file != null) {
        try (FileOutputStream fos = new FileOutputStream(file)) {

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Suppliers");

            // Header
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Name");
            header.createCell(1).setCellValue("Address");
            header.createCell(2).setCellValue("Phone");

            // Data
            int rowNum = 1;
            for (Supplier s : supplierTable.getItems()) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(s.getSupplierName());
                row.createCell(1).setCellValue(s.getAddress());
                row.createCell(2).setCellValue(s.getPhoneNumber());
            }

            workbook.write(fos);
            workbook.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setContentText("Export thành công!");
            alert.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setContentText("Export thất bại!");
            alert.show();
        }
    }
 }

}
