package my_app.controller.bt6;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

public class caua {

    private int number;

    @FXML
    private GridPane gridpViewNumber;

    private ArrayList<Integer> listNumber = new ArrayList<>();

    private final int columns = 10; // Số cột mặc định

    @FXML
    private void initialize() {
        // Khởi tạo số ban đầu
        number = 0;
        updateGrid();
    }

    public void setData(ArrayList<Integer> listNumber) {
        this.listNumber = listNumber;
        updateGrid();
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
        updateGrid();
    }

    private void addLabeltoGrid(int row, int col, String Value) {
        Label label = new Label(Value);
        label.setAlignment(Pos.CENTER);
        gridpViewNumber.add(label, col, row);
        GridPane.setHalignment(label, HPos.CENTER);
        GridPane.setValignment(label, VPos.CENTER);
    }

    private void updateGrid() {
        // Implementation for updating the grid based on the current number

        int row = (int) Math.ceil((double) listNumber.size() / columns);
        System.out.println("Number: " + number + ", Column: " + columns + ", Row: " + row);

        for (int j = 0; j < row; j++) {
            for (int i = 0; i < columns; i++) {
                if (j == 0) {
                    ColumnConstraints col = new ColumnConstraints();
                    col.setPercentWidth(100.0 / columns);
                    gridpViewNumber.getColumnConstraints().add(col);
                }
                if (j * columns + i < listNumber.size()) {
                    addLabeltoGrid(j, i, String.valueOf(listNumber.get(j * columns + i)));
                }
            }
        }
    }
}
