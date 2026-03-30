package my_app.service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class FormatBigDecimal {

    public static String getVNFormat(BigDecimal value) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formatted = nf.format(value);
        return formatted;
    }
}
