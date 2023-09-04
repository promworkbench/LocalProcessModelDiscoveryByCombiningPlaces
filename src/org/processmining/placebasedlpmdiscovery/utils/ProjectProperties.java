//package org.processmining.placebasedlpmdiscovery.utils;
//
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.Properties;
//
//public class ProjectProperties {
//
//    public static final String ANALYSIS_WRITE_VERSION_KEY = "analysis-write-version";
//    public static final String PLACE_WRITE_DESTINATION_KEY = "place-write-destination";
//    public static final String STATISTICS_WRITE_DESTINATION_KEY = "statistics-write-destination";
//
//    private static Properties properties;
//
//    private static ProjectProperties instance;
//
//    public static ProjectProperties getInstance() {
//        return getInstance("src/resources/properties.xml");
//    }
//
//    public static ProjectProperties getInstance(String filename) {
//        if (instance != null) {
//            return instance;
//        }
//        instance = new ProjectProperties(filename);
//        return instance;
//    }
//
//    private ProjectProperties(String filename) {
//        // load properties
//        properties = new Properties();
//        try(FileInputStream fileInputStream = new FileInputStream(filename)){
//            properties.loadFromXML(fileInputStream);
//        } catch(IOException e){
//            e.printStackTrace();
//        }
//    }
//
//    public void updateIntegerProperty(String key, Integer value) {
//        updateProperty(key, String.valueOf(value));
//    }
//
//    public void updateProperty(String key, String value) {
//        properties.setProperty(key, value);
//        try(FileOutputStream fileOutputStream = new FileOutputStream("properties.xml")){
//            properties.storeToXML(fileOutputStream,null);
//        } catch(IOException e){
//            e.printStackTrace();
//        }
//    }
//
//    public String getProperty(String key) {
//        return properties.getProperty(key);
//    }
//
//    public Integer getIntProperty(String key) {
//        String value = getProperty(key);
//        return Integer.parseInt(value);
//    }
//}
