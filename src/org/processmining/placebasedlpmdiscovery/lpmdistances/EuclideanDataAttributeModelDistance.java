package org.processmining.placebasedlpmdiscovery.lpmdistances;

import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import smile.math.distance.EuclideanDistance;

import java.util.List;

public class EuclideanDataAttributeModelDistance extends DataAttributeModelDistance {
    public EuclideanDataAttributeModelDistance(XLog log) {
        super(log);
    }

    @Override
    public double calculateDistance(LocalProcessModel lpm1, LocalProcessModel lpm2) {
        EuclideanDistance euclidean = new EuclideanDistance();
        return euclidean.d(this.convertLPMToFeatureVector(lpm1), this.convertLPMToFeatureVector(lpm2));
    }

    @Override
    public double[][] calculatePairwiseDistance(List<LocalProcessModel> lpms) {
        EuclideanDistance euclidean = new EuclideanDistance();
        double[][] distances = new double[lpms.size()][lpms.size()];

        List<double[]> vectors = this.convertToVectors(lpms);
        for (int i = 0; i < vectors.size() - 1; ++i) {
            for (int j = i; j < vectors.size(); ++j) {
                distances[i][j] = euclidean.d(vectors.get(i), vectors.get(j));
                distances[j][i] = distances[i][j];
            }
        }
        return distances;
    }
}
