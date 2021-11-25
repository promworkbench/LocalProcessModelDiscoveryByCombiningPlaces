package org.processmining.placebasedlpmdiscovery.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ProjectProperties {

    public static final String ANALYSIS_WRITE_VERSION_KEY = "analysis-write-version";
    public static final String PLACE_WRITE_DESTINATION_KEY = "analysis-write-version";

    private static Properties properties;

    private static void initialize() {
        // load properties
        properties = new Properties();
        try(FileInputStream fileInputStream = new FileInputStream("properties.xml")){
            properties.loadFromXML(fileInputStream);
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void updateIntegerProperty(String key, Integer value) {
        updateProperty(key, String.valueOf(value));
    }

    public static void updateProperty(String key, String value) {
        if (properties == null) {
            initialize();
        }

        properties.setProperty(key, value);
        try(FileOutputStream fileOutputStream = new FileOutputStream("properties.xml")){
            properties.storeToXML(fileOutputStream,null);
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        if (properties == null) {
            initialize();
        }

        return properties.getProperty(key);
    }

    public static Integer getIntProperty(String key) {
        String value = getProperty(key);
        return Integer.parseInt(value);
    }
}
