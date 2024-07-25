package org.processmining.placebasedlpmdiscovery.datacommunication.emittabledata;

import org.processmining.placebasedlpmdiscovery.grouping.ClusteringAlgorithm;
import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistanceConfig;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

import java.util.Collection;
import java.util.Map;

public class RunLPMGroupingEmittableData implements EmittableData {

    private final String identifier;
    private final ClusteringAlgorithm clusteringAlgorithm;
    private final Map<String, String> clusteringParameters;
    private final ModelDistanceConfig modelDistanceConfig;
    private final Collection<LocalProcessModel> lpms;

    public RunLPMGroupingEmittableData(String identifier, ClusteringAlgorithm clusteringAlgorithm, Map<String,
            String> clusteringParameters, ModelDistanceConfig modelDistanceConfig, Collection<LocalProcessModel> lpms) {
        this.identifier = identifier;
        this.clusteringAlgorithm = clusteringAlgorithm;
        this.clusteringParameters = clusteringParameters;
        this.modelDistanceConfig = modelDistanceConfig;
        this.lpms = lpms;
    }

    @Override
    public EmittableDataType getType() {
        return EmittableDataType.RunLPMGrouping;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public ClusteringAlgorithm getClusteringAlgorithm() {
        return clusteringAlgorithm;
    }

    public Map<String, String> getClusteringParameters() {
        return clusteringParameters;
    }

    public ModelDistanceConfig getModelDistanceConfig() {
        return modelDistanceConfig;
    }

    public Collection<LocalProcessModel> getLPMs() {
        return lpms;
    }
}
