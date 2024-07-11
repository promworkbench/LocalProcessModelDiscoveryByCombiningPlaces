package org.processmining.placebasedlpmdiscovery.lpmdistances;

import com.google.inject.Inject;
import org.processmining.placebasedlpmdiscovery.lpmdistances.dataattributes.*;
import org.processmining.placebasedlpmdiscovery.lpmdistances.mixed.MixedModelDistance;
import org.processmining.placebasedlpmdiscovery.lpmdistances.mixed.MixedModelDistanceConfig;
import org.processmining.placebasedlpmdiscovery.lpmdistances.mixed.WeightedModelDistanceConfig;
import org.processmining.placebasedlpmdiscovery.lpmdistances.precomputed.PrecomputedFromFileModelDistance;
import org.processmining.placebasedlpmdiscovery.lpmdistances.precomputed.PrecomputedFromFileModelDistanceConfig;
import org.processmining.placebasedlpmdiscovery.lpmdistances.processmodelsimilarity.*;

import java.util.Map;
import java.util.stream.Collectors;

public class ModelDistanceFactory {

    private final DataAttributeVectorExtractorFactory dataAttributeVectorExtractorFactory;

    @Inject
    public ModelDistanceFactory(DataAttributeVectorExtractorFactory dataAttributeVectorExtractorFactory) {
        this.dataAttributeVectorExtractorFactory = dataAttributeVectorExtractorFactory;
    }

    public ModelDistance getModelDistance(ModelDistanceConfig distanceConfig) {
        switch (distanceConfig.getDistanceMethod()) {
            case DataAttributeModelDistanceConfig.METHOD:
                return this.getDataAttributeDistance((DataAttributeModelDistanceConfig) distanceConfig);
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

    private ModelDistance getDataAttributeDistance(DataAttributeModelDistanceConfig distanceConfig) {
        DataAttributeModelDistance res = new EuclideanDataAttributeModelDistance(
                dataAttributeVectorExtractorFactory.create(distanceConfig.getAttributes())
        );
        return res;
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
            case GED:
                return new GEDModelDistances();
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
