package org.processmining.placebasedlpmdiscovery.lpmdistances;

import com.google.inject.Inject;
import org.processmining.placebasedlpmdiscovery.lpmdistances.dataattributes.DataAttributeModelDistanceConfig;
import org.processmining.placebasedlpmdiscovery.lpmdistances.dataattributes.DataAttributeModelDistanceFactory;
import org.processmining.placebasedlpmdiscovery.lpmdistances.mixed.MixedModelDistance;
import org.processmining.placebasedlpmdiscovery.lpmdistances.mixed.MixedModelDistanceConfig;
import org.processmining.placebasedlpmdiscovery.lpmdistances.processmodelsimilarity.NodeMatchingModelDistance;
import org.processmining.placebasedlpmdiscovery.lpmdistances.processmodelsimilarity.ProcessModelSimilarityDistanceConfig;
import org.processmining.placebasedlpmdiscovery.lpmdistances.processmodelsimilarity.TransitionLabelModelDistance;

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
        }
        throw new IllegalArgumentException("The Distance Method " + distanceConfig.getDistanceMethod() + " is illegal.");
    }

    private ModelDistance getProcessModelSimilarityDistance(ProcessModelSimilarityDistanceConfig distanceConfig) {
        switch (distanceConfig.getProcessModelSimilarityMeasure()) {
            case NodeMatching:
                return new NodeMatchingModelDistance();
            case TransitionLabel:
                return new TransitionLabelModelDistance();
        }
        throw new IllegalArgumentException("The Process Model Similarity Measure " + distanceConfig.getDistanceMethod()
                + " is illegal.");
    }

    private ModelDistance getMixedModelDistance(MixedModelDistanceConfig distanceConfig) {
        return new MixedModelDistance(distanceConfig.getModelDistanceWeightPairs());
    }

}
