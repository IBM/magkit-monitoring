package com.aperto.magkit.monitoring.endpoint.env;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Envrionment Properties Utils class.
 * 
 * @author VladNacu
 *
 */
public final class EnvironmentPropertiesUtil {

    private static final String DELIMITER = "=";
    private static final String EMPTY_STRING = "";

    private EnvironmentPropertiesUtil() {

    }

    public static List<String> getJvmArguments() {
        final RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();

        return runtimeMxBean.getInputArguments();
    }

    public static Map<String, String> getSystemProperties() {
        final Map<String, String> map = new HashMap<>();
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

}
