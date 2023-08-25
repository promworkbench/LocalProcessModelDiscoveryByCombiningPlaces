package org.processmining.placebasedlpmdiscovery.lpmevaluation;

import org.processmining.placebasedlpmdiscovery.RunningContext;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.LPMEvaluatorFactory;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.StandardLPMEvaluatorId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.WindowLPMEvaluator;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.additionalinfo.LPMAdditionalInfo;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.LPMTemporaryWindowInfo;

import java.util.*;

public class LPMEvaluationController implements EvaluatorHub {
    private RunningContext runningContext;
    private LPMEvaluatorFactory evaluatorFactory;
    private Map<String, WindowLPMEvaluator<?>> windowEvaluators;

    public LPMEvaluationController(RunningContext runningContext) {
        this.runningContext = runningContext;
        this.windowEvaluators = new HashMap<>();
    }

    public void setEvaluatorFactory(LPMEvaluatorFactory evaluatorFactory) {
        this.evaluatorFactory = evaluatorFactory;
    }

    public void evaluateForOneWindow(LocalProcessModel lpm,
                                     LPMTemporaryWindowInfo tempInfo,
                                     LPMAdditionalInfo additionalInfo) {
        for (WindowLPMEvaluator<?> evaluator : this.windowEvaluators.values()) {
            if (!additionalInfo.existsEvaluationResult(evaluator.getResultKey())) {
                additionalInfo.addEvaluationResult(evaluator.getResultKey(), evaluator.createEmptyResult(lpm));
            }
//            additionalInfo.updateInfo(
//                    evaluator.getKey(),
//                    evaluator.evaluate(lpm, tempInfo,
//                            additionalInfo.getInfo(evaluator.getKey(), evaluator.getResultClass())));
            additionalInfo.updateEvaluationResults(
                    evaluator.getResultKey(),
                    (LPMEvaluationResult) evaluator.evaluate(lpm, tempInfo,
                            additionalInfo.<LPMEvaluationResult>getEvaluationResult(evaluator.getResultKey(),
                                    LPMEvaluationResult.class)));
        }
    }

    @Override
    public void registerEvaluator(String key, WindowLPMEvaluator<?> evaluator) {
        this.windowEvaluators.put(key, evaluator);
    }

    public LPMEvaluationResult evaluate(String key, LocalProcessModel lpm) {
        if (EnumSet.of(StandardLPMEvaluatorId.PassageCoverageEvaluator).contains(StandardLPMEvaluatorId.valueOf(key)))
            throw new UnsupportedOperationException("This should have been evaluated before hand.");
        return this.evaluatorFactory.getEvaluator(StandardLPMEvaluatorId.valueOf(key)).evaluate(lpm);
    }
}
