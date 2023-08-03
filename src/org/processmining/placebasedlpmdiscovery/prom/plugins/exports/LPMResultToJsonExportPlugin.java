//package org.processmining.placebasedlpmdiscovery.plugins.exports;
//
//
//import com.csvreader.CsvWriter;
//import com.google.gson.Gson;
//import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
//import org.processmining.contexts.uitopia.annotations.UIExportPlugin;
//import org.processmining.framework.plugin.PluginContext;
//import org.processmining.framework.plugin.annotations.Plugin;
//import org.processmining.framework.plugin.annotations.PluginVariant;
//import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResultId;
//import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.aggregateoperations.EvaluationResultAggregateOperation;
//import org.processmining.placebasedlpmdiscovery.lpmevaluation.undecided.Utils;
//import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
//import org.processmining.placebasedlpmdiscovery.model.serializable.LPMResult;
//import org.processmining.placebasedlpmdiscovery.utils.LocalProcessModelUtils;
//
//import java.io.*;
//import java.nio.file.Files;
//import java.util.Iterator;
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipOutputStream;
//
//@Plugin(
//        name = "Export local process models into a json file",
//        returnLabels = {},
//        returnTypes = {},
//        parameterLabels = {"LPMResult", "Filename"})
//@UIExportPlugin(
//        description = "Exports local process models in json format.",
//        extension = "json")
//public class LPMResultToJsonExportPlugin {
//
//    @PluginVariant(variantLabel = "Export local process models into a file", requiredParameterLabels = {0, 1})
//    public void export(PluginContext context, LPMResult lpmResult, File file) throws IOException {
//        Gson gson = new Gson();
//
//        try (FileWriter fileWriter = new FileWriter(file)) {
//            fileWriter.write(gson.toJson(lpmResult));
//        }
//    }
//
//}
