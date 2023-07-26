package org.processmining.placebasedlpmdiscovery.lpmevaluation;

import org.processmining.placebasedlpmdiscovery.RunningContext;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.LPMEvaluator;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.LPMEvaluatorFactory;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.LPMEvaluatorId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.WindowLPMEvaluator;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.AbstractEvaluationResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.additionalinfo.LPMAdditionalInfo;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.LPMTemporaryWindowInfo;

import java.util.ArrayList;
import java.util.List;

public class LPMEvaluationController implements EvaluatorHub {
    private RunningContext runningContext;

    private LPMEvaluatorFactory evaluatorFactory;
    private List<WindowLPMEvaluator<?>> windowEvaluators;

    public LPMEvaluationController(RunningContext runningContext) {
        this.runningContext = runningContext;
        this.windowEvaluators = new ArrayList<>();
    }

    public void setEvaluatorFactory(LPMEvaluatorFactory evaluatorFactory) {
        this.evaluatorFactory = evaluatorFactory;
    }

    public void evaluateForOneWindow(LocalProcessModel lpm, LPMTemporaryWindowInfo tempInfo, LPMAdditionalInfo additionalInfo) {
        for (WindowLPMEvaluator<?> evaluator : this.windowEvaluators) {
            if (!additionalInfo.existsEvaluationResult(evaluator.getKey())) {
                additionalInfo.addEvaluationResult(evaluator.getKey(), evaluator.createEmptyResult(lpm));
            }
//            additionalInfo.updateInfo(
//                    evaluator.getKey(),
//                    evaluator.evaluate(lpm, tempInfo,
//                            additionalInfo.getInfo(evaluator.getKey(), evaluator.getResultClass())));
            additionalInfo.updateEvaluationResults(
                    evaluator.getKey(),
                    (LPMEvaluationResult) evaluator.evaluate(lpm, tempInfo,
                            additionalInfo.<LPMEvaluationResult>getEvaluationResult(evaluator.getKey(), LPMEvaluationResult.class)));
        }
    }

    @Override
    public void registerEvaluator(WindowLPMEvaluator<?> evaluator) {
        this.windowEvaluators.add(evaluator);
    }

    public LPMEvaluationResult evaluate(LPMEvaluatorId evaluatorId, LocalProcessModel lpm) {
        return this.evaluatorFactory.getEvaluator(evaluatorId).evaluate(lpm);
    }
}
