package org.processmining.placebasedlpmdiscovery.grouping;

import org.apache.commons.lang3.ArrayUtils;
import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMCollectorResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.StandardLPMCollectorResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.AttributeCollectorResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.EventAttributeCollectorResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary.AttributeSummary;
import org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary.AttributeSummaryController;
import org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary.LiteralAttributeSummary;
import org.processmining.placebasedlpmdiscovery.utils.Constants;
import smile.math.distance.EuclideanDistance;
import smile.math.distance.HammingDistance;

import java.util.*;

public class GroupingController {

    private final XLog log;
    private final AttributeSummaryController attributeSummaryController;

    /**
     * Empty attribute summaries for all possible event attributes in the event log. Default values for local process
     * models that do not have any values for some of the attributes since the feature representation vector should
     * always have the same dimension.
     */
    private final AttributeCollectorResult defaultEventAttributeSummaries;

    /**
     * Ordered attribute keys for all possible event attributes in the event log. The order is important so that for
     * all LPMs the feature representation vector should have the features in the correct order.
     */
    private final List<String> eventAttributeKeysOrder;

    /**
     * For all literal attributes we need the full list of values to build vectors of the same dimension. Since not all
     * LPMs would cover all values we store them here for easy access.
     */
    private final Map<String, List<String>> literalValuesOrder;

    public GroupingController(XLog log) {
        this.log = log;
        this.attributeSummaryController = new AttributeSummaryController();

        this.defaultEventAttributeSummaries = new EventAttributeCollectorResult();
        this.attributeSummaryController.initializeAttributeSummaryStorage(this.defaultEventAttributeSummaries, log);
        this.eventAttributeKeysOrder = new ArrayList<>(this.defaultEventAttributeSummaries.getAttributeKeys());
        Collections.sort(this.eventAttributeKeysOrder);

        this.literalValuesOrder = new HashMap<>();
    }

    private List<double[]> convertToVectors(List<LocalProcessModel> lpms) {
        List<double[]> lpmVectors = new ArrayList<>();
        for (LocalProcessModel lpm : lpms) {
            double[] featureVector = convertLPMToFeatureVector(lpm);
            lpmVectors.add(featureVector);
        }
        return lpmVectors;
    }

    public void groupLPMs(Collection<LocalProcessModel> lpms, Map<String, Object> config) {
        List<LocalProcessModel> lpmList = new ArrayList<>(lpms);

        int[] membership = ClusteringLPMs.cluster(
                lpmList,
                computeProximity(lpmList, config),
                getClusteringAlgorithm(config),
                config);

        for (int i = 0; i < lpmList.size(); ++i) {
            lpmList.get(i).getAdditionalInfo().getGroupsInfo()
                    .addGroupingProperty((String) config.get(Constants.Grouping.Config.TITLE), membership[i]);
        }
    }

    private double[][] computeProximity(List<LocalProcessModel> lpmList, Map<String, Object> config) {
        return getProximityMatrix(lpmList);
    }

    private ClusteringAlgorithm getClusteringAlgorithm(Map<String, Object> config) {
        return ClusteringAlgorithm.valueOf((String) config.get(Constants.Grouping.Config.CLUSTERING_ALG));
    }

    private double[][] getProximityMatrix(List<LocalProcessModel> lpms) {
        return getEuclideanDistance(convertToVectors(lpms));
    }

    private double[] convertLPMToFeatureVector(LocalProcessModel lpm) {
        // initialize the vector for this lpm
        List<Double> attributeVector = new ArrayList<>();

        // for now take the event attribute collector
        LPMCollectorResult collectorResult =
                lpm.getAdditionalInfo().getCollectorResults()
                        .get(StandardLPMCollectorResultId.EventAttributeCollectorResult.name());
        if (collectorResult instanceof AttributeCollectorResult) {
            AttributeCollectorResult attrCollectorResult = (AttributeCollectorResult) collectorResult;

            for (String attributeKey : eventAttributeKeysOrder) {
                AttributeSummary<?, ?> defaultAttributeSummary =
                        this.defaultEventAttributeSummaries.getAttributeSummaryForAttributeKey(attributeKey)
                                .orElseThrow(() -> new IllegalStateException("There is an attribute key for which" +
                                        " there is not summary although the possible keys were extracted from the " +
                                        "initially computed summaries."));

                // get representation features for that attribute
                Map<String, Number> representationFeatures = attrCollectorResult
                        .getAttributeSummaryForAttributeKey(attributeKey)
                        .orElse(defaultAttributeSummary)
                        .getRepresentationFeatures();
                List<String> featureKeys = new ArrayList<>(representationFeatures.keySet());

                // if the attribute is of type literal we need all values of that attribute to have vectors of same dimension
                if (defaultAttributeSummary instanceof LiteralAttributeSummary) {
                    featureKeys = this.literalValuesOrder.get(attributeKey);

                    if (featureKeys == null) {
                        featureKeys = new ArrayList<>(
                                this.attributeSummaryController
                                        .computeEventAttributeSummary(this.log, attributeKey)
                                        .getRepresentationFeatures()
                                        .keySet());
                        this.literalValuesOrder.put(attributeKey, featureKeys);
                    }
                }
                // sort representation features keys such that for each vector the same order is used
                Collections.sort(featureKeys);

                for (String feature : featureKeys) {
                    attributeVector.add(representationFeatures.getOrDefault(feature, 0).doubleValue());
                }

            }
        }

        return ArrayUtils.toPrimitive(attributeVector.toArray(new Double[0]));
    }

    private double[][] getHammingDistance(List<int[]> vectors) {
        double[][] distance = new double[vectors.size()][vectors.size()];
        for (int i = 0; i < vectors.size() - 1; ++i) {
            for (int j = i; j < vectors.size(); ++j) {
                distance[i][j] = HammingDistance.d(vectors.get(i), vectors.get(j));
                distance[j][i] = distance[i][j];
            }
        }
        return distance;
    }

    private double[][] getEuclideanDistance(List<double[]> vectors) {
        EuclideanDistance euclidean = new EuclideanDistance();

        double[][] distance = new double[vectors.size()][vectors.size()];
        for (int i = 0; i < vectors.size() - 1; ++i) {
            for (int j = i; j < vectors.size(); ++j) {
                distance[i][j] = euclidean.d(vectors.get(i), vectors.get(j));
                distance[j][i] = distance[i][j];
            }
        }
        return distance;
    }
}
