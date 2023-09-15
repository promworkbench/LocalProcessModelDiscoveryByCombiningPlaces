package org.processmining.placebasedlpmdiscovery.prom.plugins.exports;

import org.processmining.contexts.uitopia.annotations.UIExportPlugin;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.placebasedlpmdiscovery.model.exporting.exporters.ExporterFactory;
import org.processmining.placebasedlpmdiscovery.model.serializable.LPMResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Plugin(
        name = "Export local process models into a json file",
        returnLabels = {},
        returnTypes = {},
        parameterLabels = {"LPMResult", "Filename"})
@UIExportPlugin(
        description = "Exports local process models in json format.",
        extension = "json")
public class LPMResultJsonExportPlugin {

    @PluginVariant(variantLabel = "Export local process models into a file", requiredParameterLabels = {0, 1})
    public void export(PluginContext context, LPMResult lpmResult, File file) throws IOException {
        lpmResult.export(ExporterFactory.createLPMDiscoveryResultExporter(), Files.newOutputStream(file.toPath()));
    }

}
