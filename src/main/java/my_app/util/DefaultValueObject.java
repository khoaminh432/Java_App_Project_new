package my_app.util;

import java.util.ArrayList;
import java.util.HashMap;

public class DefaultValueObject {

    private static final String UnitProduct = "UnitProduct";
    private static final String StatusProduct = "StatusProduct";
    private static HashMap<String, ArrayList<String>> defaultValues = new HashMap<>();

    static {
        ArrayList<String> UnitProductDefault = new ArrayList<>();
        UnitProductDefault.add("ly");
        UnitProductDefault.add("cái");
        UnitProductDefault.add("Phần");
        defaultValues.put(UnitProduct, UnitProductDefault);
        ArrayList<String> StatusProductDefault = new ArrayList<>();
        StatusProductDefault.add("available");
        StatusProductDefault.add("unavailable");
        defaultValues.put(StatusProduct, StatusProductDefault);

    }

    private static ArrayList<String> getDefaultValues(String key) {
        return defaultValues.get(key);
    }

    public static ArrayList<String> getUnitProduct() {
        return getDefaultValues(UnitProduct);
    }

    public static ArrayList<String> getStatusProduct() {
        return getDefaultValues(StatusProduct);
    }

    public static void setDefaultValues(String key, ArrayList<String> values) {
        defaultValues.put(key, values);
    }

    public static HashMap<String, ArrayList<String>> getAllDefaultValues() {
        return defaultValues;
    }
}
