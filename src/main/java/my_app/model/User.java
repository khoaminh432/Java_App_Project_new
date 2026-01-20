package my_app.model;

import java.util.HashMap;
import java.util.Map;

public class User {
	private final Map<String, Object> attributes = new HashMap<>();

	public void applyFromMap(Map<String, Object> data) {
		if (data == null || data.isEmpty()) {
			return;
		}
		attributes.putAll(data);
	}

	public Map<String, Object> getAttributes() {
		return new HashMap<>(attributes);
	}
}
