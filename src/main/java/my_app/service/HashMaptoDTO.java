package my_app.service;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HashMaptoDTO<T> {

    private final Class<T> targetClass;
    private final Map<String, Field> fieldCache;

    public HashMaptoDTO(Class<T> targetClass) {
        this.targetClass = targetClass;
        this.fieldCache = cacheFields(targetClass);
    }

    public T exchange(HashMap<String, Object> map) {
        if (map == null) {
            throw new IllegalArgumentException("Source map must not be null");
        }

        try {
            Constructor<T> ctor = targetClass.getDeclaredConstructor();
            ctor.setAccessible(true);
            T dto = ctor.newInstance();

            map.forEach((key, value) -> {
                Field field = fieldCache.get(normalize(key));
                if (field == null) {
                    return;
                }

                field.setAccessible(true);
                try {
                    Object coerced = coerceValue(field.getType(), value);
                    if (coerced != null || !field.getType().isPrimitive()) {
                        field.set(dto, coerced);
                    }
                } catch (IllegalAccessException | IllegalArgumentException ex) {
                    throw new RuntimeException(
                        "Failed to assign value for field '" + field.getName() + "'", ex);
                }
            });

            return dto;
        } catch (RuntimeException rte) {
            throw rte;
        } catch (Exception e) {
            throw new RuntimeException("Unable to convert map to DTO", e);
        }
    }

    private Map<String, Field> cacheFields(Class<T> clazz) {
        Map<String, Field> cache = new HashMap<>();
        for (Field field : clazz.getDeclaredFields()) {
            cache.put(normalize(field.getName()), field);
        }
        return cache;
    }

    private String normalize(String key) {
        return key == null ? "" : key.replaceAll("[^A-Za-z0-9]", "").toLowerCase(Locale.ROOT);
    }

    private Object coerceValue(Class<?> targetType, Object value) {
        if (value == null) {
            return null;
        }

        if (targetType.isAssignableFrom(value.getClass())) {
            return value;
        }

        if ((targetType == int.class || targetType == Integer.class) && value instanceof Number num) {
            return num.intValue();
        }

        if ((targetType == long.class || targetType == Long.class) && value instanceof Number num) {
            return num.longValue();
        }

        if (targetType == String.class) {
            return value.toString();
        }

        if ((targetType == double.class || targetType == Double.class) && value instanceof Number num) {
            return num.doubleValue();
        }

        return value;
    }
}
