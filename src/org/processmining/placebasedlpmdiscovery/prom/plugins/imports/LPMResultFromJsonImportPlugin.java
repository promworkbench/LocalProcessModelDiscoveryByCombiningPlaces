//package org.processmining.placebasedlpmdiscovery.plugins.imports;
//
//import com.google.gson.Gson;
//import com.google.gson.stream.JsonReader;
//import org.processmining.contexts.uitopia.annotations.UIImportPlugin;
//import org.processmining.framework.plugin.PluginContext;
//import org.processmining.framework.plugin.annotations.Plugin;
//import org.processmining.placebasedlpmdiscovery.model.serializable.LPMResult;
//
//import java.io.FileReader;
//import java.io.InputStream;
//
//@Plugin(name = "Import LPMResult from a file", parameterLabels = {"Filename"}, returnLabels = {"LPM Result"}, returnTypes = {LPMResult.class})
//@UIImportPlugin(description = "Import local process models from a json file", extensions = {"json"})
//public class LPMResultFromJsonImportPlugin {
//
//    protected LPMResult importFromStream(PluginContext context, InputStream input, String filename, long fileSizeInBytes)
//            throws Exception {
//        JsonReader jsonReader = new JsonReader(new FileReader(filename));
//        Gson gson = new Gson();
//        return gson.fromJson(jsonReader, LPMResult.class);
//    }
//}
