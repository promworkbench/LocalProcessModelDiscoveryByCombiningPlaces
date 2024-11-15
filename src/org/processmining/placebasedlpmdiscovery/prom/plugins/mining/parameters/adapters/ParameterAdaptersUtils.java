package org.processmining.placebasedlpmdiscovery.prom.plugins.mining.parameters.adapters;

import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algorithms.parameters.PlaceBasedLPMDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.model.logs.EventLog;
import org.processmining.placebasedlpmdiscovery.prom.plugins.mining.PlaceBasedLPMDiscoveryPluginParameters;

public class ParameterAdaptersUtils {

    public static PlaceBasedLPMDiscoveryParameters transform(PlaceBasedLPMDiscoveryPluginParameters inParam,
                                                             EventLog log) {
        PlaceBasedLPMDiscoveryParameters outParam = new PlaceBasedLPMDiscoveryParameters(log);
        outParam.setLpmCount(inParam.getLpmCount());
        outParam.setTimeLimit(inParam.getTimeLimit());
        outParam.setPlaceDiscoveryParameters(inParam.getPlaceDiscoveryParameters());
        outParam.setLpmBuildingAlgType(inParam.getLpmBuildingAlgType());
        outParam.setPlaceDiscoveryAlgorithmId(inParam.getPlaceDiscoveryAlgorithmId());
        outParam.setUseDefaultPlaceDiscoveryParameters(inParam.isUseDefaultPlaceDiscoveryParameters());
        outParam.setLpmBuildingParameters(inParam.getLpmBuildingParameters());
        outParam.setLpmFilterParameters(inParam.getLpmFilterParameters());
        outParam.setLpmCombinationParameters(inParam.getLpmCombinationParameters());
        return outParam;
    }
}
