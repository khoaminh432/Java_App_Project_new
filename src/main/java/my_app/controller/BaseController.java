package my_app.controller;

import javafx.fxml.FXML;

public abstract class BaseController {

    // Hàm khởi tạo chung cho tất cả controller
    @FXML
    private void initialize() {
        initData();
        initEvent();
        initUI();
    }

    // Các hàm con cho phép override
    protected abstract void initData();

    protected abstract void initEvent();

    protected abstract void initUI();
}
