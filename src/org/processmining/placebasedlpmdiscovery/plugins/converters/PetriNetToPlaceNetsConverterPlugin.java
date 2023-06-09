package org.processmining.placebasedlpmdiscovery.plugins.converters;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.placebasedlpmdiscovery.model.serializable.LPMResult;
import org.processmining.placebasedlpmdiscovery.model.serializable.PlaceSet;
import org.processmining.placebasedlpmdiscovery.utils.PlaceUtils;

@Plugin(
        name = "Petri Net to Place Nets converter",
        parameterLabels = {"Petri Net"},
        returnLabels = {"Place nets"},
        returnTypes = {PlaceSet.class},
        help = "Extract place nets from a Petri net"
)
public class PetriNetToPlaceNetsConverterPlugin {

    @UITopiaVariant(
            affiliation = "RWTH - PADS",
            author = "Viki Peeva",
            email = "peeva@pads.rwth-aachen.de",
            uiLabel = "Petri Net to Place Nets converter"
    )
    @PluginVariant(
            variantLabel = "Petri Net to Place Nets converter",
            requiredParameterLabels = {0}
    )
    public static PlaceSet extractPlaceNets(UIPluginContext context, Petrinet net) {
        return new PlaceSet(PlaceUtils.getPlacesFromPetriNet(net));
    }
}
