package org.processmining.placebasedlpmdiscovery.lpmevaluation;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.tuple.Pair;
import org.processmining.framework.util.ArrayUtils;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.LPMCollectorFactory;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.StandardLPMEvaluatorId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.WindowLPMCollector;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMCollectorResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.additionalinfo.LPMAdditionalInfo;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.LPMTemporaryWindowInfo;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;

public class LPMEvaluationController implements EvaluatorHub {
    private LPMCollectorFactory evaluatorFactory;
    private Map<String, WindowLPMCollector<?>> windowEvaluators;

    private final EventCoverageSetLevel eventCoverageSetLevel;

    public LPMEvaluationController() {
        this.evaluatorFactory = new LPMCollectorFactory();
        this.windowEvaluators = new HashMap<>();

        this.eventCoverageSetLevel = new EventCoverageSetLevel();
    }

    public void setEvaluatorFactory(LPMCollectorFactory evaluatorFactory) {
        this.evaluatorFactory = evaluatorFactory;
    }

    public LPMAdditionalInfo updateAdditionalInfoForOneWindow(LocalProcessModel lpm,
                                                              LPMTemporaryWindowInfo tempInfo,
                                                              LPMAdditionalInfo additionalInfo) {
        for (WindowLPMCollector<?> evaluator : this.windowEvaluators.values()) {
            if (!additionalInfo.existsCollectorResult(evaluator.getResultKey())) {
                additionalInfo.addCollectorResult(evaluator.getResultKey(), evaluator.createEmptyResult(lpm));
            }
//            additionalInfo.updateInfo(
//                    evaluator.getKey(),
//                    evaluator.evaluate(lpm, tempInfo,
//                            additionalInfo.getInfo(evaluator.getKey(), evaluator.getResultClass())));
            additionalInfo.updateCollectorResults(
                    evaluator.getResultKey(),
                    evaluator.evaluate(lpm, tempInfo, additionalInfo.getEvaluationResult(
                            evaluator.getResultKey(),
                            LPMCollectorResult.class)));
        }
        this.eventCoverageSetLevel.update(lpm, tempInfo);
        return additionalInfo;
    }

    @Override
    public void registerEvaluator(String key, WindowLPMCollector<?> evaluator) {
        this.windowEvaluators.put(key, evaluator);
    }

    public LPMEvaluationResult evaluate(String key, LocalProcessModel lpm) {
        if (EnumSet.of(StandardLPMEvaluatorId.PassageCoverageEvaluator).contains(StandardLPMEvaluatorId.valueOf(key)))
            throw new UnsupportedOperationException("This should have been evaluated before hand.");
        return this.evaluatorFactory.getEvaluator(StandardLPMEvaluatorId.valueOf(key)).evaluate(lpm);
    }

    public EventCoverageSetLevel getEventCoverageSetLevel() {
        return eventCoverageSetLevel;
    }

    public class EventCoverageSetLevel {

        private final Map<Pair<Integer, Integer>, Set<String>> coverageMap;
        private final Map<Integer, Integer> traceVariantsCount;

        public EventCoverageSetLevel() {
            this.coverageMap = new HashMap<>();
            this.traceVariantsCount = new HashMap<>();
        }

        public void update(LocalProcessModel lpm, LPMTemporaryWindowInfo tempInfo) {
            tempInfo.getReplayedEventsIndices().forEach(idx -> {
                Set<String> coveringLPMs = coverageMap.getOrDefault(Pair.of(tempInfo.getTraceVariantId(), idx),
                        new HashSet<>());
                coveringLPMs.add(lpm.getShortString());
                coverageMap.put(Pair.of(tempInfo.getTraceVariantId(), idx), coveringLPMs);
            });
            traceVariantsCount.putIfAbsent(tempInfo.getTraceVariantId(), tempInfo.getWindowCount());
        }

        /**
         * Writes a csv with one row per given LPM and one column per event (named {@code <variantId>_<eventIndex>})
         * covered by any evaluated LPM. A cell holds the count of the event's trace variant if the LPM covers the
         * event and 0 otherwise. The coverage is recorded per short string, so LPMs are matched by their short string.
         *
         * @param rowName gives the name in the first column of the LPM's row
         */
        public void export(Appendable out, Collection<LocalProcessModel> lpms,
                           Function<LocalProcessModel, String> rowName) throws IOException {
            Map<String, Pair<Integer, Integer>> events = new TreeMap<>();
            coverageMap.keySet().forEach(event -> events.put(event.getKey() + "_" + event.getValue(), event));

            CSVPrinter csvPrinter = CSVFormat.DEFAULT
                    .builder()
                    .setHeader(ArrayUtils.concatAll(new String[]{"LPM"}, events.keySet().toArray(new String[0])))
                    .build()
                    .print(out);
            for (LocalProcessModel lpm : lpms) {
                String shortString = lpm.getShortString();
                List<Object> record = new ArrayList<>();
                record.add(rowName.apply(lpm));
                for (Pair<Integer, Integer> event : events.values())
                    record.add(coverageMap.get(event).contains(shortString) ? traceVariantsCount.get(event.getKey())
                            : 0);
                csvPrinter.printRecord(record);
            }
            csvPrinter.flush();
        }
    }
}
