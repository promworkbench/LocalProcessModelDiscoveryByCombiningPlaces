package org.processmining.placebasedlpmdiscovery.lpmevaluation;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
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

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

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

        public void export(File outputFile) throws IOException {
            Set<String> lpms = coverageMap.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
            ImmutableTable.Builder<String, String, Integer> tableBuilder = new ImmutableTable.Builder<>();

            for (String lpm : lpms) {
                for (Pair<Integer, Integer> event : coverageMap.keySet()) {
                    String eventId = event.getKey() + "_" +event.getValue();
                    tableBuilder.put(lpm, eventId,
                            coverageMap.get(event).contains(lpm) ? traceVariantsCount.get(event.getKey()) : 0);
                }
            }

            Table<String, String, Integer> coverageTable = tableBuilder.build();
            System.out.println(coverageTable.rowMap().size());
            try (CSVPrinter csvPrinter = CSVFormat.DEFAULT
                    .builder()
                    .setHeader(ArrayUtils.concatAll(new String[]{"LPM"},
                            coverageTable.columnKeySet().stream().sorted().toArray(String[]::new)))
                    .build()
                    .print(outputFile, StandardCharsets.UTF_8)) {
                csvPrinter.printRecords(coverageTable.rowMap().entrySet()
                        .stream().map(entry -> ImmutableList.builder()
                                .add(entry.getKey())
                                .addAll(entry.getValue().entrySet().stream().sorted(Map.Entry.comparingByKey())
                                        .map(Map.Entry::getValue).collect(Collectors.toList()))
                                .build())
                        .collect(Collectors.toList()));
            }
        }
    }
}
