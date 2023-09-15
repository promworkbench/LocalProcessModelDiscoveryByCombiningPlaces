package org.processmining.placebasedlpmdiscovery.prom.plugins.imports;

import org.processmining.contexts.uitopia.annotations.UIImportPlugin;
import org.processmining.framework.abstractplugins.AbstractImportPlugin;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.placebasedlpmdiscovery.model.exporting.importers.ImporterFactory;
import org.processmining.placebasedlpmdiscovery.model.exporting.importers.JsonImporter;
import org.processmining.placebasedlpmdiscovery.model.serializable.LPMResult;

import java.io.InputStream;

@Plugin(name = "Import LPMResult from a file", parameterLabels = {"Filename"}, returnLabels = {"LPM Result"}, returnTypes = {LPMResult.class})
@UIImportPlugin(description = "Import local process models from a json file", extensions = {"json"})
public class LPMResultJsonImportPlugin extends AbstractImportPlugin {

    protected LPMResult importFromStream(PluginContext context, InputStream input, String filename, long fileSizeInBytes)
            throws Exception {
        try {
            context.getFutureResult(0).setLabel("Set of lpms imported from " + filename);
        } catch (final Throwable ignored) {

        }
        JsonImporter<LPMResult> importer = ImporterFactory.createLPMDiscoveryResultJsonImporter();
        return importer.read(LPMResult.class, input);
    }
}
