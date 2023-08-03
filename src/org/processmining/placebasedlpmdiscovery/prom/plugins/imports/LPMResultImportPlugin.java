package org.processmining.placebasedlpmdiscovery.prom.plugins.imports;

import org.processmining.contexts.uitopia.annotations.UIImportPlugin;
import org.processmining.framework.abstractplugins.AbstractImportPlugin;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.placebasedlpmdiscovery.model.serializable.LPMResult;

import java.io.InputStream;
import java.io.ObjectInputStream;

@Plugin(name = "Import LPMResult from a file", parameterLabels = {"Filename"}, returnLabels = {"LPM Result"}, returnTypes = {LPMResult.class})
@UIImportPlugin(description = "Import local process models from a file", extensions = {"promlpm"})
public class LPMResultImportPlugin extends AbstractImportPlugin {

    protected LPMResult importFromStream(PluginContext context, InputStream input, String filename, long fileSizeInBytes)
            throws Exception {

        try {
            context.getFutureResult(0).setLabel("LPM result imported from " + filename);
        } catch (final Throwable ignored) {

        }

        ObjectInputStream ois = new ObjectInputStream(input);
        Object object = ois.readObject();
        ois.close();
        if (object instanceof LPMResult) {
            return (LPMResult) object;
        } else {
            System.err.println("File could not be parsed as valid LPMResult object");
        }
        return null;
    }
}
