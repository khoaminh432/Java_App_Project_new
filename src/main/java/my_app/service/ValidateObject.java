package my_app.service;

import my_app.model.StudentDTO;

public class ValidateObject {

    private static ValidateObject instance;

    private ValidateObject() {
    }

    public static boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void isStudentDTOValid(StudentDTO obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Học sinh không được null");
        }

        if (obj.getName() == null || obj.getName().isEmpty()) {
            throw new IllegalArgumentException("Tên học sinh không được null hoặc rỗng");
        }
        if (obj.getSurname() == null || obj.getSurname().isEmpty()) {
            throw new IllegalArgumentException("Họ học sinh không được null hoặc rỗng");
        }
        if (obj.getBirthdate() == null || obj.getBirthdate().isEmpty()) {
            throw new IllegalArgumentException("Ngày sinh học sinh không được null hoặc rỗng");
        }
        if (obj.getHometown() == null || obj.getHometown().isEmpty()) {
            throw new IllegalArgumentException("Quê học sinh không được null hoặc rỗng");
        }
    }
}
