package my_app.util;

import java.util.ArrayList;
import java.util.HashMap;

public class DefaultValueObject {

    private static final String UnitProduct = "UnitProduct";
    private static final String StatusProduct = "StatusProduct";
    private static final String UnitPriceProductRaito = "UnitPriceProductRaito";
    private static HashMap<String, ArrayList<String>> defaultValues = new HashMap<>();

    static {
        ArrayList<String> UnitProductDefault = new ArrayList<>();
        UnitProductDefault.add("ly");
        UnitProductDefault.add("cái");
        UnitProductDefault.add("Phần");
        defaultValues.put(UnitProduct, UnitProductDefault);
        ArrayList<String> UnitPriceProductRaitoDefault = new ArrayList<>();
        UnitPriceProductRaitoDefault.add("0%");
        UnitPriceProductRaitoDefault.add("10%");
        UnitPriceProductRaitoDefault.add("20%");
        UnitPriceProductRaitoDefault.add("30%");
        UnitPriceProductRaitoDefault.add("50%");
        UnitPriceProductRaitoDefault.add("70%");
        UnitPriceProductRaitoDefault.add("100%");
        defaultValues.put(UnitPriceProductRaito, UnitPriceProductRaitoDefault);
        ArrayList<String> StatusProductDefault = new ArrayList<>();
        StatusProductDefault.add("available");
        StatusProductDefault.add("unavailable");
        defaultValues.put(StatusProduct, StatusProductDefault);

    }

    private static ArrayList<String> getDefaultValues(String key) {
        return defaultValues.get(key);
    }

    public static ArrayList<String> getDefaultUnitPriceProduct() {
        return getDefaultValues(UnitPriceProductRaito);
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
