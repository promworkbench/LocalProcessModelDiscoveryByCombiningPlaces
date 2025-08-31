package org.processmining.placebasedlpmdiscovery.lpmdistances.dataattributes;

import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import smile.math.distance.EuclideanDistance;

import java.util.List;

public class EuclideanDataAttributeModelDistance extends DataAttributeModelDistance {

    public EuclideanDataAttributeModelDistance(DataAttributeVectorExtractor dataAttrVecExtractor) {
        super(dataAttrVecExtractor);
    }

    @Override
    public double calculateDistance(LocalProcessModel lpm1, LocalProcessModel lpm2) {
        EuclideanDistance euclidean = new EuclideanDistance();
        return euclidean.d(
                this.dataAttrVecExtractor.convertLPMToFeatureVector(lpm1),
                this.dataAttrVecExtractor.convertLPMToFeatureVector(lpm2));
    }

    @Override
    public double[][] calculatePairwiseDistance(List<LocalProcessModel> lpms) {
        EuclideanDistance euclidean = new EuclideanDistance();
        double[][] distances = new double[lpms.size()][lpms.size()];
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        List<double[]> vectors = this.dataAttrVecExtractor.convertToVectors(lpms);
        for (int i = 0; i < vectors.size() - 1; ++i) {
            for (int j = i; j < vectors.size(); ++j) {
                distances[i][j] = euclidean.d(vectors.get(i), vectors.get(j));
                distances[j][i] = distances[i][j];
                min = Math.min(min, distances[i][j]);
                max = Math.max(max, distances[i][j]);
            }
        }

        // normalize the distances via min-max normalization
        for (int i = 0; i < distances.length; ++i) {
            for (int j = 0; j < distances.length; ++j) {
                distances[i][j] = (distances[i][j] - min) / (max - min);
            }
        }

        return distances;
    }
}
