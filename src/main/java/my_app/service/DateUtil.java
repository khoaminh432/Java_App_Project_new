package my_app.service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javafx.scene.control.DatePicker;

public class DateUtil {

    // =========================
    // 1. DatePicker -> Timestamp (lưu DB)
    // =========================
    public static Timestamp toTimestamp(DatePicker datePicker) {
        if (datePicker == null || datePicker.getValue() == null) {
            return null;
        }
        LocalDateTime dateTime = datePicker.getValue().atStartOfDay();
        return Timestamp.valueOf(dateTime);
    }

    // =========================
    // 2. DatePicker -> Timestamp với giờ custom
    // =========================
    public static Timestamp toTimestamp(DatePicker datePicker, int hour, int minute) {
        if (datePicker == null || datePicker.getValue() == null) {
            return null;
        }
        LocalDateTime dateTime = datePicker.getValue().atTime(hour, minute);
        return Timestamp.valueOf(dateTime);
    }

    // =========================
    // 3. Timestamp -> DatePicker (hiển thị lại)
    // =========================
    public static void setDatePicker(DatePicker datePicker, Timestamp timestamp) {
        if (datePicker == null || timestamp == null) {
            return;
        }
        LocalDate localDate = timestamp.toLocalDateTime().toLocalDate();
        datePicker.setValue(localDate);
    }

    // =========================
    // 4. LocalDate -> Timestamp
    // =========================
    public static Timestamp toTimestamp(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return Timestamp.valueOf(localDate.atStartOfDay());
    }

    // =========================
    // 5. Timestamp -> LocalDateTime
    // =========================
    public static LocalDateTime toLocalDateTime(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        return timestamp.toLocalDateTime();
    }

    // =========================
    // 6. Lấy thời gian hiện tại
    // =========================
    public static Timestamp now() {
        return Timestamp.valueOf(LocalDateTime.now());
    }
}
