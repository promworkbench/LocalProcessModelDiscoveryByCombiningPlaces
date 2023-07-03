package org.processmining.placebasedlpmdiscovery.plugins.addons;

import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.serializable.LPMResult;

@Plugin(
        name = "LPM sets merger",
        parameterLabels = {"LPMs"},
        returnLabels = {"LPMs"},
        returnTypes = {LPMResult.class},
        help = "Merge multiple local process model sets into one"
)
public class MergeLPMSets {
    @UITopiaVariant(
            affiliation = "RWTH - PADS",
            author = "Viki Peeva",
            email = "peeva@pads.rwth-aachen.de",
            uiLabel = "LPM sets merger"
    )
    @PluginVariant(
            variantLabel = "LPM sets merger",
            requiredParameterLabels = {0}
    )
    public static LPMResult extractPlaceNets(UIPluginContext context, LPMResult... lpmSets) {
        if (lpmSets.length < 2) {
            throw new IllegalArgumentException("You need at least two sets to merge.");
        }
        LPMResult lpmResult = new LPMResult();
        for (LPMResult lpmSet : lpmSets) {
            for (LocalProcessModel lpm : lpmSet.getElements()) {
                lpmResult.add(lpm);
            }
        }
        return lpmResult;
    }
}
