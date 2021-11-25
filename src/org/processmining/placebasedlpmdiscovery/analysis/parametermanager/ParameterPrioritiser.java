package org.processmining.placebasedlpmdiscovery.analysis.parametermanager;

import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.plugins.mining.PlaceBasedLPMDiscoveryParameters;

/**
 * Given some ParameterSetup, creates PlaceBasedLPMDiscoveryParameters for each possibility in the setup,
 * by first iterating through the proximity for each placeLimit.
 */
public class ParameterPrioritiser {

    private final ParameterSetup parameterSetup;
    private final XLog eventLog;
    private int currentPlaceLimitIndex;
    private int currentProximityIndex;

    public ParameterPrioritiser(ParameterSetup parameterSetup, XLog eventLog) {
        this.parameterSetup = parameterSetup;
        this.eventLog = eventLog;
        this.currentPlaceLimitIndex = 0;
        this.currentProximityIndex = 0;
    }

    public PlaceBasedLPMDiscoveryParameters next() {
        return nextProximity();
    }

    public PlaceBasedLPMDiscoveryParameters nextPlaceLimit() {
        this.currentProximityIndex = 0;
        this.currentPlaceLimitIndex++;
        if (this.currentPlaceLimitIndex < this.parameterSetup.getPlaceLimit().size())
            return create();
        return null;
    }

    public PlaceBasedLPMDiscoveryParameters nextProximity() {
        if (this.currentProximityIndex < this.parameterSetup.getProximity().size())
            return create();
        return nextPlaceLimit();
    }

    private PlaceBasedLPMDiscoveryParameters create() {
        PlaceBasedLPMDiscoveryParameters parameters = new PlaceBasedLPMDiscoveryParameters(eventLog);
        parameters.getLpmCombinationParameters().setLpmProximity(this.parameterSetup.getProximity().get(currentProximityIndex));
        parameters.getLpmCombinationParameters().setMinNumPlaces(this.parameterSetup.getMinPlaces());
        parameters.getLpmCombinationParameters().setMaxNumPlaces(this.parameterSetup.getMaxPlaces());
        parameters.getLpmCombinationParameters().setMinNumTransitions(this.parameterSetup.getMinTransitions());
        parameters.getLpmCombinationParameters().setMaxNumTransitions(this.parameterSetup.getMaxTransitions());
        parameters.setTimeLimit(this.parameterSetup.getTimeLimit());
        parameters.getPlaceChooserParameters().setPlaceLimit(this.parameterSetup.getPlaceLimit().get(currentPlaceLimitIndex));
        parameters.setLpmCount(Integer.MAX_VALUE);

        this.currentProximityIndex++;

        return parameters;
    }
}
