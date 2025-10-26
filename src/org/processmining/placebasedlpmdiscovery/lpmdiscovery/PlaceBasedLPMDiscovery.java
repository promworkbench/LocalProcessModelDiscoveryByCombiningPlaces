package org.processmining.placebasedlpmdiscovery.lpmdiscovery;

import org.deckfour.xes.model.XLog;
import org.processmining.lpms.discovery.DiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.Main;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.inputs.FPGrowthForPlacesLPMBuildingInput;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algbuilder.LPMDiscoveryAlgBuilder;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algorithms.inputs.StandardLPMDiscoveryInput;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algorithms.parameters.PlaceBasedLPMDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.logs.EventLog;
import org.processmining.placebasedlpmdiscovery.model.logs.XLogWrapper;
import org.processmining.placebasedlpmdiscovery.prom.PlacesProviderFactory;

import java.util.Set;

public class PlaceBasedLPMDiscovery implements LPMDiscovery {

    private final int placeLimit;
    private final PlacesProviderFactory placesProviderFactory;
    private final int proximity;
    private final int concurrencyLimit;

    public PlaceBasedLPMDiscovery(PlacesProviderFactory placesProviderFactory) {
        this(placesProviderFactory, DiscoveryParameters.PlaceBased.placeLimit);
    }

    public PlaceBasedLPMDiscovery(PlacesProviderFactory placesProviderFactory, int placeLimit) {
        this(placesProviderFactory, placeLimit, DiscoveryParameters.Default.proximity);
    }

    public PlaceBasedLPMDiscovery(PlacesProviderFactory placesProviderFactory, int placeLimit, int proximity) {
        this(placesProviderFactory, placeLimit, proximity, 2);
    }

    public PlaceBasedLPMDiscovery(PlacesProviderFactory placesProviderFactory, int placeLimit, int proximity, int concurrencyLimit) {
        this.placesProviderFactory = placesProviderFactory;
        this.placeLimit = placeLimit;
        this.proximity = proximity;
        this.concurrencyLimit = concurrencyLimit;
    }

    @Override
    public LPMDiscoveryResult from(XLog log) {
        EventLog eventLog = new XLogWrapper(log);

        PlaceBasedLPMDiscoveryParameters parameters = new PlaceBasedLPMDiscoveryParameters(eventLog);
        parameters.getLpmCombinationParameters().setLpmProximity(proximity);
        parameters.getPlaceChooserParameters().setPlaceLimit(this.placeLimit);
        parameters.getLpmCombinationParameters().setConcurrencyCardinality(this.concurrencyLimit);

        LPMDiscoveryAlgBuilder builder = Main.createDefaultBuilder(log, parameters);
        Set<Place> places = this.placesProviderFactory.create(log).provide();

        FPGrowthForPlacesLPMBuildingInput lpmBuildingInput = new FPGrowthForPlacesLPMBuildingInput(eventLog, places);
        StandardLPMDiscoveryInput discoveryInput =
                new StandardLPMDiscoveryInput(eventLog, lpmBuildingInput);

        return builder.build().run(discoveryInput, parameters);
    }
}
