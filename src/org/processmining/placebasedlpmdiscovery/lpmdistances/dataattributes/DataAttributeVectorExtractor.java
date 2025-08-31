package org.processmining.placebasedlpmdiscovery.lpmdistances.dataattributes;

import com.google.inject.assistedinject.Assisted;
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
import org.processmining.placebasedlpmdiscovery.utils.LogUtils;

import java.util.*;
import java.util.stream.Collectors;

public class DataAttributeVectorExtractor {

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
    private List<String> eventAttributeKeysOrder;

    /**
     * For all literal attributes we need the full list of values to build vectors of the same dimension. Since not all
     * LPMs would cover all values we store them here for easy access.
     */
    private final Map<String, List<String>> literalValuesOrder;

    private int vectorSize;
    private int[] vectorStructure; // the vector structures stores for each attribute how many features are extracted and is used in normalization
    private List<String> positionMapping;

    public DataAttributeVectorExtractor(XLog log, @Assisted Collection<String> attributes) {
        this.log = log;
        if (attributes == null || attributes.isEmpty()) {
            attributes = LogUtils.getEventAttributesKeys(this.log);
        }
        this.attributeSummaryController = new AttributeSummaryController();

        this.defaultEventAttributeSummaries = new EventAttributeCollectorResult();
        this.attributeSummaryController.initializeAttributeSummaryStorage(this.defaultEventAttributeSummaries, log);
        this.eventAttributeKeysOrder = this.defaultEventAttributeSummaries.getAttributeKeys().stream().filter(attributes::contains).collect(Collectors.toList());
        Collections.sort(this.eventAttributeKeysOrder);

        this.literalValuesOrder = new HashMap<>();
        initVectorStructureInfo();
    }

    public void chooseSubsetOfAttributes(Collection<String> attributes) {
        this.eventAttributeKeysOrder = this.eventAttributeKeysOrder.stream().filter(attributes::contains).collect(Collectors.toList());
        initVectorStructureInfo();
    }

    public List<double[]> convertToVectors(List<LocalProcessModel> lpms) {
        List<double[]> orLpmVectors = new ArrayList<>();

        // compute original vectors and min and max vectors
        for (LocalProcessModel lpm : lpms) {
            orLpmVectors.add(this.convertLPMToFeatureVector(lpm));
        }

        return orLpmVectors;
    }

    public List<double[]> convertToVectorsNormalized(List<LocalProcessModel> lpms) {
        List<double[]> orLpmVectors = convertToVectors(lpms);

        double[] maxVector = new double[this.vectorSize];
        Arrays.fill(maxVector, Double.MIN_VALUE);
        double[] minVector = new double[this.vectorSize];
        Arrays.fill(minVector, Double.MAX_VALUE);

        // compute original vectors and min and max vectors
        for (double[] featureVector : orLpmVectors) {
            for (int i = 0; i < vectorSize; ++i) {
                maxVector[i] = Math.max(maxVector[i], featureVector[i]);
                minVector[i] = Math.min(minVector[i], featureVector[i]);
            }
        }

        // normalize using min max and vector size
        List<double[]> minMaxNormLPMVectors = new ArrayList<>();
        int ind = 0;
        for (double[] vec : orLpmVectors) {
            for (int i = 0; i < vec.length; ++i) {
                if (maxVector[i] - minVector[i] == 0) {
                    vec[i] = 0;
                } else {
                    vec[i] = (vec[i] - minVector[i]) / (maxVector[i] - minVector[i]); // min-max
                }
                int attrSize = i < this.vectorStructure[ind] ?
                        ind == 0 ? this.vectorStructure[ind] : this.vectorStructure[ind] - this.vectorStructure[ind - 1] :
                        this.vectorStructure[++ind] - this.vectorStructure[ind - 1];
                vec[i] = vec[i] / attrSize; //vector size
            }
            minMaxNormLPMVectors.add(vec);
        }

        // normalize using size of representation features

        return minMaxNormLPMVectors;
    }

    private void initVectorStructureInfo() {
        this.vectorSize = 0;
        this.vectorStructure = new int[eventAttributeKeysOrder.size()];
        this.positionMapping = new ArrayList<>();
        int index = 0;
        for (String attributeKey : eventAttributeKeysOrder) {
            AttributeSummary<?, ?> defaultAttributeSummary =
                    this.defaultEventAttributeSummaries.getAttributeSummaryForAttributeKey(attributeKey)
                            .orElseThrow(() -> new IllegalStateException("There is an attribute key for which" +
                                    " there is not summary although the possible keys were extracted from the " +
                                    "initially computed summaries."));

            // get representation features for that attribute
            Map<String, Number> representationFeatures = defaultAttributeSummary
                    .getRepresentationFeatures();
            List<String> featureKeys = new ArrayList<>(representationFeatures.keySet());

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
            // for each position set the attribute and feature key
            this.positionMapping.addAll(featureKeys.stream().map(key -> attributeKey + "-" + key).collect(Collectors.toList()));
            this.vectorStructure[index] = featureKeys.size() + (index == 0 ? 0 : this.vectorStructure[index - 1]);
            index++;
            this.vectorSize += featureKeys.size();
        }
    }

    public double[] convertLPMToFeatureVector(LocalProcessModel lpm) {
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

                // if the attribute is of type literal, we need all values of that attribute to have vectors of the same dimension
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

                double sum = featureKeys.stream()
                        .map(key -> Math.pow(representationFeatures.getOrDefault(key, 0).doubleValue(), 2))
                        .mapToDouble(Number::doubleValue).sum();
                double norm = sum == 0 ? 1 : Math.sqrt(sum);

                for (String feature : featureKeys) {
                    attributeVector.add(representationFeatures.getOrDefault(feature, 0).doubleValue() / norm);
                }

            }
        }

        return ArrayUtils.toPrimitive(attributeVector.toArray(new Double[0]));
    }

    public List<String> getPositionMapping() {
        return positionMapping;
    }
}
