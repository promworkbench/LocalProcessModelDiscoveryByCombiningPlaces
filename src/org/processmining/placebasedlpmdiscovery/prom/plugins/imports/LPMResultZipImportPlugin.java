package org.processmining.placebasedlpmdiscovery.prom.plugins.imports;

import org.processmining.contexts.uitopia.annotations.UIImportPlugin;
import org.processmining.framework.abstractplugins.AbstractImportPlugin;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.exporting.importers.ZipLPMImporter;

import java.io.InputStream;

@Plugin(name = "Import LPMResult from a zip file", parameterLabels = {"Filename"}, returnLabels = {"LPM Result"},
        returnTypes = {LPMDiscoveryResult.class})
@UIImportPlugin(description = "Import local process models from a zip file with Petri nets (and optionally a csv with" +
        " their scores)", extensions = {"zip"})
public class LPMResultZipImportPlugin extends AbstractImportPlugin {

    protected LPMDiscoveryResult importFromStream(PluginContext context, InputStream input, String filename,
                                                  long fileSizeInBytes)
            throws Exception {
        try {
            context.getFutureResult(0).setLabel("Set of lpms imported from " + filename);
        } catch (final Throwable ignored) {

        }
        ZipLPMImporter importer = new ZipLPMImporter();
        LPMDiscoveryResult result = importer.read(input, filename);
        for (String warning : importer.getWarnings())
            context.log(warning);
        return result;
    }
}
