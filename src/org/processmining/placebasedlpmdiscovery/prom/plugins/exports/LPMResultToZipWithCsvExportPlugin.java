package org.processmining.placebasedlpmdiscovery.prom.plugins.exports;

import org.processmining.contexts.uitopia.annotations.UIExportPlugin;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.placebasedlpmdiscovery.model.exporting.exporters.ZipLPMExporter;
import org.processmining.placebasedlpmdiscovery.model.serializable.LPMResult;

import java.io.File;
import java.io.IOException;

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
        ZipLPMExporter.export(lpmResult, file, context);
    }
}
