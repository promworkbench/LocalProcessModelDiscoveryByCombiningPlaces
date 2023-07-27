package org.processmining.placebasedlpmdiscovery.plugins.exports;


import com.csvreader.CsvWriter;
import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.contexts.uitopia.annotations.UIExportPlugin;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.GroupedEvaluationResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.aggregateoperations.EvaluationResultAggregateOperation;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.undecided.Utils;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.serializable.LPMResult;
import org.processmining.placebasedlpmdiscovery.utils.LocalProcessModelUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Plugin(
        name = "Export local process models into an array of Petri nets",
        returnLabels = {},
        returnTypes = {},
        parameterLabels = {"LPMResult", "Filename"})
@UIExportPlugin(
        description = "Exports local process models as Accepting Petri Nets together with a csv file that includes all filenames and aggregated score.",
        extension = "zip")
public class LPMResultToZipWithCsvExportPlugin {

    @PluginVariant(variantLabel = "Export local process models into a file", requiredParameterLabels = {0, 1})
    public void export(PluginContext context, LPMResult lpmResult, File file) throws IOException {
        String fileName = file.getName();
        String prefix = fileName.substring(0, fileName.indexOf("."));

        ZipOutputStream out = new ZipOutputStream(Files.newOutputStream(file.toPath()));

        String csvFileName = prefix + ".csv";
        ByteArrayOutputStream csvOS = new ByteArrayOutputStream();
        CsvWriter csvWriter = new CsvWriter(new OutputStreamWriter(csvOS), ',');
        csvWriter.writeRecord(new String[]{"Name", "Fitting Windows Score", "Trace Support Score", "Aggregated Score"});

        EvaluationResultAggregateOperation aggregateOperation = new EvaluationResultAggregateOperation();
        LPMEvaluationResultId[] ids = new LPMEvaluationResultId[]{
                LPMEvaluationResultId.FittingWindowsEvaluationResult,
                LPMEvaluationResultId.TraceSupportEvaluationResult
        };

        for (LocalProcessModel lpm : lpmResult.getElements()) {
            // convert lpm to accepting petri net
            AcceptingPetriNet apn = LocalProcessModelUtils.getAcceptingPetriNetRepresentation(lpm);

            String zfName = prefix + "." + lpm.getId() + ".pnml";

            // add the net file to the zip folder
            ByteArrayOutputStream oos = Utils.exportAcceptingPetriNetToOutputStream(context, apn);
            addContentToZip(out, oos.toByteArray(), prefix + "." + lpm.getId() + ".pnml");

            // write an entry in the csv
            csvWriter.write(zfName);
            for (LPMEvaluationResultId id : ids) {
                csvWriter.write(String.valueOf(lpm.getAdditionalInfo()
                        .getEvaluationResult(id.name(), LPMEvaluationResult.class).getResult()));
            }
            csvWriter.write(String.valueOf(LocalProcessModelUtils.getGroupedEvaluationResult(lpm).getResult(aggregateOperation)));
            csvWriter.endRecord();
        }
        // add csv file to zip
        csvWriter.close();
        addContentToZip(out, csvOS.toByteArray(), csvFileName);
        out.close();
    }

    private void addContentToZip(ZipOutputStream out, byte[] content, String fileName) throws IOException {
        ZipEntry e = new ZipEntry(fileName.substring(0, fileName.lastIndexOf(".")) + fileName.substring(fileName.lastIndexOf(".")));
        out.putNextEntry(e);
        out.write(content);
        out.closeEntry();
    }
}
