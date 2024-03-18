package org.processmining.placebasedlpmdiscovery.lpmdistances;

import com.google.inject.Inject;
import org.processmining.placebasedlpmdiscovery.lpmdistances.dataattributes.DataAttributeModelDistanceConfig;
import org.processmining.placebasedlpmdiscovery.lpmdistances.dataattributes.DataAttributeModelDistanceFactory;
import org.processmining.placebasedlpmdiscovery.lpmdistances.mixed.MixedModelDistance;
import org.processmining.placebasedlpmdiscovery.lpmdistances.mixed.MixedModelDistanceConfig;
import org.processmining.placebasedlpmdiscovery.lpmdistances.mixed.WeightedModelDistanceConfig;
import org.processmining.placebasedlpmdiscovery.lpmdistances.precomputed.PrecomputedFromFileModelDistance;
import org.processmining.placebasedlpmdiscovery.lpmdistances.precomputed.PrecomputedFromFileModelDistanceConfig;
import org.processmining.placebasedlpmdiscovery.lpmdistances.processmodelsimilarity.*;

import java.util.Map;
import java.util.stream.Collectors;

public class ModelDistanceFactory {

    @Inject
    private DataAttributeModelDistanceFactory dataAttributeModelDistanceFactory;

    @Inject
    public ModelDistance getModelDistance(ModelDistanceConfig distanceConfig) {
        switch (distanceConfig.getDistanceMethod()) {
            case DataAttributeModelDistanceConfig.METHOD:
                return dataAttributeModelDistanceFactory.create();
            case ProcessModelSimilarityDistanceConfig.METHOD:
                if (!(distanceConfig instanceof ProcessModelSimilarityDistanceConfig))
                    throw new IllegalStateException("The distance method does not pass on the distance config.");
                return this.getProcessModelSimilarityDistance((ProcessModelSimilarityDistanceConfig) distanceConfig);
            case MixedModelDistanceConfig.METHOD:
                if (!(distanceConfig instanceof MixedModelDistanceConfig))
                    throw new IllegalStateException("The distance method does not pass on the distance config.");
                return this.getMixedModelDistance((MixedModelDistanceConfig) distanceConfig);
            case "Precomputed":
                return new PrecomputedFromFileModelDistance((PrecomputedFromFileModelDistanceConfig) distanceConfig);
        }
        throw new IllegalArgumentException("The Distance Method " + distanceConfig.getDistanceMethod() + " is illegal.");
    }

    private ModelDistance getProcessModelSimilarityDistance(ProcessModelSimilarityDistanceConfig distanceConfig) {
        switch (distanceConfig.getProcessModelSimilarityMeasure()) {
            case NodeMatching:
                return new NodeMatchingModelDistance();
            case TransitionLabel:
                return new TransitionLabelModelDistance();
            case TraceMatching:
                return new TraceMatchingModelDistance();
            case EFOverlap:
                return new EFOverlapModelDistance();
        }
        throw new IllegalArgumentException("The Process Model Similarity Measure " + distanceConfig.getDistanceMethod()
                + " is illegal.");
    }

    private ModelDistance getMixedModelDistance(MixedModelDistanceConfig distanceConfig) {
        Map<String, Double> weights = distanceConfig.getModelDistanceWeightPairs().stream()
                .collect(Collectors.toMap(WeightedModelDistanceConfig::getKey, WeightedModelDistanceConfig::getWeight));
        Map<String, ModelDistance> distances = distanceConfig.getModelDistanceWeightPairs().stream()
                .collect(Collectors.toMap(
                        WeightedModelDistanceConfig::getKey,
                        wdist -> this.getModelDistance(wdist.getDistanceConfig())));

        return new MixedModelDistance(weights, distances);
    }

}
