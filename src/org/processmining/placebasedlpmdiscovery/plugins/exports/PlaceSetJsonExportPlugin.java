package org.processmining.placebasedlpmdiscovery.plugins.exports;

import org.processmining.contexts.uitopia.annotations.UIExportPlugin;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.exporting.JsonExporter;
import org.processmining.placebasedlpmdiscovery.model.serializable.PlaceSet;

import java.io.*;

@Plugin(
        name = "Export set of places into a file",
        returnLabels = {},
        returnTypes = {},
        parameterLabels = {"Set of places", "Filename"})
@UIExportPlugin(
        description = "ProM set of places (json) files",
        extension = "json")
public class PlaceSetJsonExportPlugin {

    @PluginVariant(variantLabel = "Export set of places into a file", requiredParameterLabels = {0, 1})
    public static void export(PluginContext context, PlaceSet placeSet, File file) {
        JsonExporter<PlaceSet> exporter = new JsonExporter<>(file);
        exporter.export(placeSet);
    }
}
