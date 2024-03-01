package org.processmining.placebasedlpmdiscovery.prom.plugins.converters;


import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.placebasedlpmdiscovery.main.StandardLPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.serializable.LPMResult;

@Plugin(
        name = "LPMDiscoveryResult to LPMResult converter",
        parameterLabels = {"LPMDiscoveryResult"},
        returnLabels = {"LPMResult"},
        returnTypes = {LPMResult.class},
        help = "Convert any lpm discovery result to a lpm result"
)
public class LPMDiscoveryResultToLPMResultConverterPlugin {

    @UITopiaVariant(
            affiliation = "RWTH - PADS",
            author = "Viki Peeva",
            email = "peeva@pads.rwth-aachen.de",
            uiLabel = "LPMDiscoveryResult to LPMResult converter"
    )
    @PluginVariant(
            variantLabel = "LPMDiscoveryResult to LPMResult converter",
            requiredParameterLabels = {0}
    )
    public static LPMResult convertStandardLPMDiscoveryResult(UIPluginContext context,
                                                              StandardLPMDiscoveryResult result) {
        return new LPMResult(result);
    }
}
