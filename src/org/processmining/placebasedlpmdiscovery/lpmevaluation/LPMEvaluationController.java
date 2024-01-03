package org.processmining.placebasedlpmdiscovery.lpmevaluation;

import org.processmining.placebasedlpmdiscovery.RunningContext;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.LPMCollectorFactory;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.StandardLPMEvaluatorId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.WindowLPMCollector;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.additionalinfo.LPMAdditionalInfo;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.LPMTemporaryWindowInfo;

import java.util.*;

public class LPMEvaluationController implements EvaluatorHub {
    private RunningContext runningContext;
    private LPMCollectorFactory evaluatorFactory;
    private Map<String, WindowLPMCollector<?>> windowEvaluators;

    public LPMEvaluationController(RunningContext runningContext) {
        this.runningContext = runningContext;
        this.windowEvaluators = new HashMap<>();
    }

    public void setEvaluatorFactory(LPMCollectorFactory evaluatorFactory) {
        this.evaluatorFactory = evaluatorFactory;
    }

    public void evaluateForOneWindow(LocalProcessModel lpm,
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
                            LPMEvaluationResult.class)));
        }
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
}
