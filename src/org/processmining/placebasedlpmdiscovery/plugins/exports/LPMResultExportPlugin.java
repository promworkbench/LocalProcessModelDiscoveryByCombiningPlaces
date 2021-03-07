package org.processmining.placebasedlpmdiscovery.plugins.exports;


import org.processmining.contexts.uitopia.annotations.UIExportPlugin;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.placebasedlpmdiscovery.model.serializable.LPMResult;

import java.io.*;

@Plugin(
        name = "Export local process models into a file",
        returnLabels = {},
        returnTypes = {},
        parameterLabels = {"LPMResult", "Filename"})
@UIExportPlugin(
        description = "ProM local process models (promlpm) files",
        extension = "promlpm")
public class LPMResultExportPlugin {

    @PluginVariant(variantLabel = "Export local process models into a file", requiredParameterLabels = {0, 1})
    public void export(PluginContext context, LPMResult lpmResult, File file) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(lpmResult);
        } catch (FileNotFoundException e) {
            System.err.println("File could not be found");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
