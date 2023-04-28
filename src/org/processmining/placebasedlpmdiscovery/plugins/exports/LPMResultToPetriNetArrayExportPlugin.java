package org.processmining.placebasedlpmdiscovery.plugins.exports;


import org.processmining.acceptingpetrinet.models.impl.AcceptingPetriNetArrayImpl;
import org.processmining.contexts.uitopia.annotations.UIExportPlugin;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.serializable.LPMResult;
import org.processmining.placebasedlpmdiscovery.utils.LocalProcessModelUtils;

import java.io.File;
import java.io.IOException;

@Plugin(
        name = "Export local process models into an array of Petri nets",
        returnLabels = {},
        returnTypes = {},
        parameterLabels = {"LPMResult", "Filename"})
@UIExportPlugin(
        description = "Exports local process models as Accepting Petri Nets together with a csv file that includes all filenames",
        extension = "csv")
public class LPMResultToPetriNetArrayExportPlugin {

    @PluginVariant(variantLabel = "Export local process models into a file", requiredParameterLabels = {0, 1})
    public void export(PluginContext context, LPMResult lpmResult, File file) {
        AcceptingPetriNetArrayImpl lpmPnArray = new AcceptingPetriNetArrayImpl();
        for (LocalProcessModel lpm : lpmResult.getElements()) {
            lpmPnArray.addElement(LocalProcessModelUtils.getAcceptingPetriNetRepresentation(lpm));
        }
        try {
            lpmPnArray.exportToFile(context, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
