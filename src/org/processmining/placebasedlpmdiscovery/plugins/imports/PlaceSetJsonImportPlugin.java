package org.processmining.placebasedlpmdiscovery.plugins.imports;

import org.processmining.contexts.uitopia.annotations.UIImportPlugin;
import org.processmining.framework.abstractplugins.AbstractImportPlugin;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.placebasedlpmdiscovery.model.exporting.JsonImporter;
import org.processmining.placebasedlpmdiscovery.model.serializable.PlaceSet;

import java.io.InputStream;

@Plugin(name = "Import Set of places from a json file", parameterLabels = {"Filename"}, returnLabels = {"Set of places"}, returnTypes = {PlaceSet.class})
@UIImportPlugin(description = "Import set of places from a json file", extensions = {"json"})
public class PlaceSetJsonImportPlugin extends AbstractImportPlugin {

    protected PlaceSet importFromStream(PluginContext context, InputStream input, String filename, long fileSizeInBytes)
            throws Exception {

        try {
            context.getFutureResult(0).setLabel("Set of places imported from " + filename);
        } catch (final Throwable ignored) {

        }
        JsonImporter<PlaceSet> importer = new JsonImporter<>(input, PlaceSet.class);
        return importer.read();
//        return PlaceUtils.getPlaceSetFromInputStream(input);
    }
}