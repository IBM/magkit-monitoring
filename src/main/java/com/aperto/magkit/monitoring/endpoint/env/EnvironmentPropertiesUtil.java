package com.aperto.magkit.monitoring.endpoint.env;


import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Envrionment Properties Utils class
 *
 */
public class EnvironmentPropertiesUtil {

	private static final String DELIMITER = "=";
	private static final String EMPTY_STRING = "";

	private EnvironmentPropertiesUtil() {

	}

	public static List<String> getJvmArguments() {
		final RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();

		return runtimeMXBean.getInputArguments();
	}

	public static Map<String, String> getSystemProperties() {
		final HashMap<String, String> map = new HashMap<>();
		final Properties systemProperties = System.getProperties();
		final Enumeration enuProp = systemProperties.propertyNames();

		while (enuProp.hasMoreElements()) {
			String propertyName = (String) enuProp.nextElement();
			String propertyValue = systemProperties.getProperty(propertyName);
			map.put(propertyName, propertyValue);
		}

		return map;
	}

	public static Map<String, String> parsingJvmArgs(final List<String> jvmArgs) {
		final Map<String, String> map = new HashMap<>();
		for (final String arg : jvmArgs) {
			String[] tokens = arg.split(DELIMITER);
			if (tokens.length == 2) {
				map.put(tokens[0], tokens[1]);
			} else {
				map.put(tokens[0], EMPTY_STRING);
			}
		}
		return map;
	}

	/*
	 * public static Map<String, String> getMagnoliaProperties() { String
	 * propFileName = "magnolia.properties"; Properties prop = new Properties();
	 * InputStream inputStream =
	 * getClass().getClassLoader().getResourceAsStream(propFileName); if(inputStream
	 * != null) { prop.load(inputStream); } System.out.println(prop); return null; }
	 */
}
