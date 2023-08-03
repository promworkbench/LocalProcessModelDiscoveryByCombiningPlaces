package org.processmining.placebasedlpmdiscovery.prom.plugins.exports;


import org.processmining.contexts.uitopia.annotations.UIExportPlugin;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.placebasedlpmdiscovery.model.serializable.PlaceSet;

import java.io.*;

@Plugin(
        name = "Export set of places into a file",
        returnLabels = {},
        returnTypes = {},
        parameterLabels = {"Set of places", "Filename"})
@UIExportPlugin(
        description = "ProM set of places (promspl) files",
        extension = "promspl")
public class PlaceSetExportPlugin {

    @PluginVariant(variantLabel = "Export set of places into a file", requiredParameterLabels = {0, 1})
    public static void export(PluginContext context, PlaceSet placeSet, File file) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(placeSet);
        } catch (FileNotFoundException e) {
            System.err.println("File could not be found");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
