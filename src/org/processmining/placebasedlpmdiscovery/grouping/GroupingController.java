package org.processmining.placebasedlpmdiscovery.grouping;

import org.apache.commons.lang3.ArrayUtils;
import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMCollectorResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.StandardLPMCollectorResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.AttributeCollectorResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.EventAttributeCollectorResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary.AttributeSummaryController;
import smile.clustering.HierarchicalClustering;
import smile.clustering.linkage.CompleteLinkage;
import smile.math.distance.EuclideanDistance;
import smile.math.distance.HammingDistance;

import java.util.*;
import java.util.stream.Collectors;

public class GroupingController {

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

    public GroupingController(XLog log) {
        defaultEventAttributeSummaries = new EventAttributeCollectorResult();
        AttributeSummaryController attributeSummaryController = new AttributeSummaryController();
        attributeSummaryController.initializeAttributeSummaryStorage(defaultEventAttributeSummaries, log);
        this.eventAttributeKeysOrder = new ArrayList<>(this.defaultEventAttributeSummaries.getAttributeKeys());
        Collections.sort(this.eventAttributeKeysOrder);
    }

    private List<double[]> convertToVectors(List<LocalProcessModel> lpms) {
        List<double[]> lpmVectors = new ArrayList<>();
        for (LocalProcessModel lpm : lpms) {
            double[] featureVector = convertLPMToFeatureVector(lpm);
            lpmVectors.add(featureVector);
        }
        return lpmVectors;
    }

    public void groupLPMs(Collection<LocalProcessModel> lpms) {
        List<LocalProcessModel> lpmList = new ArrayList<>(lpms);

//        Map<String, String> clusteringConfig = new HashMap<>();
//        clusteringConfig.put("linkage", "complete");
//        clusteringConfig.put("partitions", "5");

        HierarchicalClustering hc = HierarchicalClustering.fit(new CompleteLinkage(getProximityMatrix(lpmList)));
        int[] membership = hc.partition(10);
        for (int i = 0; i < lpmList.size(); ++i) {
            lpmList.get(i).getAdditionalInfo().getGroupsInfo()
                    .addGroupingProperty("default", membership[i]);
        }
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
                Map<String, Number> representationFeatures = attrCollectorResult
                        .getAttributeSummaryForAttributeKey(attributeKey)
                        .orElse(this.defaultEventAttributeSummaries.getAttributeSummaryForAttributeKey(attributeKey)
                                .orElseThrow(() -> new IllegalStateException("There is an attribute key for which" +
                                        " there is not summary although the possible keys were extracted from the " +
                                        "initially computed summaries.")))
                        .getRepresentationFeatures();

                // sort representation features keys such that for each vector the same order is used
                List<String> featureKeys = representationFeatures.keySet()
                        .stream().sorted().collect(Collectors.toList());
                for (String feature : featureKeys) {
                    attributeVector.add(representationFeatures.get(feature).doubleValue());
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
