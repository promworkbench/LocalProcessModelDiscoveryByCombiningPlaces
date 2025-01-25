package org.processmining.placebasedlpmdiscovery.lpmdiscovery;

import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.Main;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.inputs.FPGrowthForPlacesLPMBuildingInput;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algbuilder.LPMDiscoveryAlgBuilder;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algorithms.inputs.StandardLPMDiscoveryInput;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algorithms.parameters.PlaceBasedLPMDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.discovery.StandardLPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.logs.EventLog;
import org.processmining.placebasedlpmdiscovery.model.logs.XLogWrapper;
import org.processmining.placebasedlpmdiscovery.model.serializable.LPMResult;
import org.processmining.placebasedlpmdiscovery.prom.PlacesProvider;

import java.util.Set;

public class PlaceBasedLPMDiscovery implements LPMDiscovery {

    private final int placeLimit;
    private final PlacesProvider placesProvider;

    public PlaceBasedLPMDiscovery(PlacesProvider placesProvider, int placeLimit) {
        this.placesProvider = placesProvider;
        this.placeLimit = placeLimit;
    }

    @Override
    public LPMDiscoveryResult from(XLog log) {
        EventLog eventLog = new XLogWrapper(log);

        PlaceBasedLPMDiscoveryParameters parameters = new PlaceBasedLPMDiscoveryParameters(eventLog);
        parameters.getPlaceChooserParameters().setPlaceLimit(this.placeLimit);

        LPMDiscoveryAlgBuilder builder = Main.createDefaultBuilder(log, parameters);
        Set<Place> places = this.placesProvider.from(log);
        FPGrowthForPlacesLPMBuildingInput lpmBuildingInput = new FPGrowthForPlacesLPMBuildingInput(eventLog, places);
        StandardLPMDiscoveryInput discoveryInput =
                new StandardLPMDiscoveryInput(eventLog, lpmBuildingInput);
        return new LPMResult((StandardLPMDiscoveryResult) builder.build().run(discoveryInput, parameters));
    }
}
