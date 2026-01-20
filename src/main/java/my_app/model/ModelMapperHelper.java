package my_app.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
// Helper class for mapping data from Map to model attributes
public final class ModelMapperHelper {
	private ModelMapperHelper() {
	}

	public static Integer getInteger(Map<String, Object> data, String... keys) {
		Object value = extract(data, keys);
		if (value instanceof Number) {
	            Number number = (Number) value;
			return number.intValue();
		}
		if (value instanceof String) {
			String str = ((String) value).trim();
			if (!str.isEmpty()) {
			try {
				return Integer.parseInt(str);
			} catch (NumberFormatException ignored) {
				return null;
			}
			}
		}
		return null;
	}

	public static BigDecimal getBigDecimal(Map<String, Object> data, String... keys) {
		Object value = extract(data, keys);
		if (value instanceof BigDecimal) {
			return (BigDecimal) value;
		}
		if (value instanceof Number) {
			Number number = (Number) value;
			return BigDecimal.valueOf(number.doubleValue());
		}
		if (value instanceof String) {
			String str = ((String) value).trim();
			if (!str.isEmpty()) {
				try {
					return new BigDecimal(str);
				} catch (NumberFormatException ignored) {
					return null;
				}
			}
		}
		return null;
	}

	public static String getString(Map<String, Object> data, String... keys) {
		Object value = extract(data, keys);
		if (value == null) {
			return null;
		}
		String str = value.toString().trim();
		return str.isEmpty() ? null : str;
	}

	public static LocalDateTime getLocalDateTime(Map<String, Object> data, String... keys) {
		Object value = extract(data, keys);
		if (value instanceof LocalDateTime) {
			return (LocalDateTime) value;
		}
		if (value instanceof Timestamp) {
			Timestamp timestamp = (Timestamp) value;
			return timestamp.toLocalDateTime();
		}
		if (value instanceof Date) {
			Date date = (Date) value;
			return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
		}
		if (value instanceof String) {
			String str = ((String) value).trim();
			if (!str.isEmpty()) {
				try {
					return LocalDateTime.parse(str);
				} catch (Exception ignored) {
					return null;
				}
			}
		}
		return null;
	}

	public static LocalDate getLocalDate(Map<String, Object> data, String... keys) {
		Object value = extract(data, keys);
		if (value instanceof LocalDate) {
			return (LocalDate) value;
		}
		if (value instanceof LocalDateTime) {
			LocalDateTime localDateTime = (LocalDateTime) value;
			return localDateTime.toLocalDate();
		}
		if (value instanceof java.sql.Date) {
			java.sql.Date sqlDate = (java.sql.Date) value;
			return sqlDate.toLocalDate();
		}
		if (value instanceof Date) {
			Date date = (Date) value;
			return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		}
		if (value instanceof String) {
			String str = ((String) value).trim();
			if (!str.isEmpty()) {
				try {
					return LocalDate.parse(str);
				} catch (Exception ignored) {
					return null;
				}
			}
		}
		return null;
	}

	private static Object extract(Map<String, Object> data, String... keys) {
		if (data == null || data.isEmpty() || keys == null) {
			return null;
		}
		for (String key : keys) {
			if (key != null && data.containsKey(key)) {
				return data.get(key);
			}
		}
		return null;
	}
}
