package uci.vision.logger.server.domain;

import java.util.Map;

public class LogConfig {
	Map<String, String> properties;

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	@Override
	public String toString() {
		return "LogConfig [properties=" + properties + "]";
	} 	
}
