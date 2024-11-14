package org.processmining.placebasedlpmdiscovery.analysis.parametermanager;

import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.model.logs.XLogWrapper;
import org.processmining.placebasedlpmdiscovery.prom.plugins.mining.PlaceBasedLPMDiscoveryPluginParameters;

/**
 * Given some ParameterSetup, creates PlaceBasedLPMDiscoveryParameters for each possibility in the setup,
 * by first iterating through the proximity for each placeLimit.
 */
public class ParameterPrioritiser {

    private final ParameterSetup parameterSetup;
    private final XLog eventLog;
    private int currentPlaceLimitIndex;
    private int currentProximityIndex;
    private int currentCardinalityIndex;
    private int maxPlaces;
    private final int maxProximity;

    public ParameterPrioritiser(ParameterSetup parameterSetup, XLog eventLog) {
        this.parameterSetup = parameterSetup;
        this.eventLog = eventLog;
        this.currentPlaceLimitIndex = 0;
        this.currentProximityIndex = 0;
        this.currentCardinalityIndex = -1;
        this.maxPlaces = -1;
        this.maxProximity = -1;
    }

    public PlaceBasedLPMDiscoveryPluginParameters next() {
        return nextCardinality();
    }

    public PlaceBasedLPMDiscoveryPluginParameters nextPlaceLimit() {
        this.currentProximityIndex = 0;
        this.currentCardinalityIndex = 0;
        // if in the last iteration we limited the number of places to more than what is available we are done
//        if (maxPlaces != -1 && parameterSetup.getPlaceLimit().get(this.currentPlaceLimitIndex) >= maxPlaces)
//            return null;
        this.currentPlaceLimitIndex++;
        if (this.currentPlaceLimitIndex < this.parameterSetup.getPlaceLimit().size())
            return create();
        return null;
    }

    public PlaceBasedLPMDiscoveryPluginParameters nextProximity() {
        this.currentCardinalityIndex = 0;
        // if in the last iteration we limited the proximity to more than what is available we are done
//        if (maxProximity != -1 && parameterSetup.getProximity().get(this.currentProximityIndex) >= maxProximity)
//            return nextPlaceLimit();
        this.currentProximityIndex++;
        if (this.currentProximityIndex < this.parameterSetup.getProximity().size())
            return create();
        return nextPlaceLimit();
    }

    public PlaceBasedLPMDiscoveryPluginParameters nextCardinality() {
        this.currentCardinalityIndex++;
        if (this.currentCardinalityIndex < this.parameterSetup.getCardinality().size())
            return create();
        return nextProximity();
    }

    private PlaceBasedLPMDiscoveryPluginParameters create() {
        PlaceBasedLPMDiscoveryPluginParameters parameters = new PlaceBasedLPMDiscoveryPluginParameters(new XLogWrapper(eventLog));
        parameters.getLpmCombinationParameters().setLpmProximity(this.parameterSetup.getProximity().get(currentProximityIndex));
        parameters.getLpmCombinationParameters().setConcurrencyCardinality(this.parameterSetup.getCardinality().get(currentCardinalityIndex));
        parameters.getLpmCombinationParameters().setMinNumPlaces(this.parameterSetup.getMinPlaces());
        parameters.getLpmCombinationParameters().setMaxNumPlaces(this.parameterSetup.getMaxPlaces());
        parameters.getLpmCombinationParameters().setMinNumTransitions(this.parameterSetup.getMinTransitions());
        parameters.getLpmCombinationParameters().setMaxNumTransitions(this.parameterSetup.getMaxTransitions());
        parameters.setTimeLimit(this.parameterSetup.getTimeLimit());
        parameters.getPlaceChooserParameters().setPlaceLimit(this.parameterSetup.getPlaceLimit().get(currentPlaceLimitIndex));
        parameters.setLpmCount(Integer.MAX_VALUE);

        return parameters;
    }

    public void setMaxPlaces(int size) {
        this.maxPlaces = size;
    }
}
