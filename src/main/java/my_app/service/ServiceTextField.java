package my_app.service;

import javafx.scene.control.TextField;

public class ServiceTextField {

    public boolean isEmpty(TextField tf) {
        return sanitize(tf).isEmpty();
    }

    public int CheckInteger(TextField tf) {
        String value = requireNonEmpty(tf);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("TextField must contain an integer value", ex);
        }
    }

    public double CheckDouble(TextField tf) {
        String value = requireNonEmpty(tf);
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("TextField must contain a decimal number", ex);
        }
    }

    public boolean isWithinRange(TextField tf, double minInclusive, double maxInclusive) {
        if (minInclusive > maxInclusive) {
            throw new IllegalArgumentException("Minimum value cannot be greater than maximum value");
        }
        double value = CheckDouble(tf);
        return value >= minInclusive && value <= maxInclusive;
    }

    public boolean hasLengthBetween(TextField tf, int minLength, int maxLength) {
        if (minLength < 0 || maxLength < 0) {
            throw new IllegalArgumentException("Length constraints must be non-negative");
        }
        if (minLength > maxLength) {
            throw new IllegalArgumentException("Minimum length cannot be greater than maximum length");
        }
        int length = sanitize(tf).length();
        return length >= minLength && length <= maxLength;
    }

    private String requireNonEmpty(TextField tf) {
        String value = sanitize(tf);
        if (value.isEmpty()) {
            throw new IllegalArgumentException("TextField cannot be empty");
        }
        return value;
    }

    private String sanitize(TextField tf) {
        if (tf == null) {
            throw new IllegalArgumentException("TextField reference cannot be null");
        }
        String value = tf.getText();
        return value == null ? "" : value.trim();
    }
}
