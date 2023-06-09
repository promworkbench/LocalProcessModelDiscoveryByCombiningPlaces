package org.processmining.placebasedlpmdiscovery.plugins.exports;


import com.csvreader.CsvWriter;
import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.contexts.uitopia.annotations.UIExportPlugin;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
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

    }


}
