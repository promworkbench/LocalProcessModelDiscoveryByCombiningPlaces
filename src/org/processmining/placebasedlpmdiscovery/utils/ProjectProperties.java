package org.processmining.placebasedlpmdiscovery.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ProjectProperties {

    public static int ANALYSIS_WRITE_VERSION_VALUE;
    public static final String ANALYSIS_WRITE_VERSION_KEY = "analysis-write-version";

    private static Properties properties;

    public static void initialize() {
        // load properties
        properties = new Properties();
        try(FileInputStream fileInputStream = new FileInputStream("properties.xml")){
            properties.loadFromXML(fileInputStream);
        } catch(IOException e){
            e.printStackTrace();
        }

        // write properties
        ANALYSIS_WRITE_VERSION_VALUE = Integer.parseInt(properties.getProperty(ANALYSIS_WRITE_VERSION_KEY));
    }

    public static void updateIntegerProperty(String key, Integer value) {
        updateProperty(key, String.valueOf(value));
    }

    public static void updateProperty(String key, String value) {
        properties.setProperty(key, value);
        try(FileOutputStream fileOutputStream = new FileOutputStream("properties.xml")){
            properties.storeToXML(fileOutputStream,null);
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
