package org.processmining.placebasedlpmdiscovery.plugins.visualization.components;

import org.processmining.placebasedlpmdiscovery.evaluation.results.AbstractEvaluationResult;
import org.processmining.placebasedlpmdiscovery.evaluation.results.GroupedEvaluationResult;
import org.processmining.placebasedlpmdiscovery.evaluation.results.SimpleEvaluationResult;
import org.processmining.placebasedlpmdiscovery.evaluation.results.aggregateoperations.EvaluationResultAggregateOperation;
import org.processmining.placebasedlpmdiscovery.evaluation.results.concrete.PassageCoverageEvaluationResult;

import javax.swing.*;
import java.text.DecimalFormat;

public class ComponentFactory {

//    private static JPanel getWindowsEvaluationResultComponent(FittingWindowsEvaluationResult result) {
//        JPanel panel = new JPanel();
//        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//
//        JLabel windowCountLbl = getjLabel("Windows count:    ", result.getCount());
//        windowCountLbl.setToolTipText("The window count, shows the number of windows that could be replayed on the local process model");
//        panel.add(windowCountLbl);
//
//        JLabel fittingWindowsScoreLbl = getjLabel("Fitting windows score:    ", result.getResult());
//        fittingWindowsScoreLbl.setToolTipText("The count of fitting score is divided with the number of all windows in the log.");
//        panel.add(fittingWindowsScoreLbl);
//
//        JLabel trCoverageScoreLbl = getjLabel("Transition coverage score:    ", result.getTransitionCoverageScore());
//        trCoverageScoreLbl.setToolTipText("This score is calculated as an average of the fractions of fitting window in which ");
//        panel.add(trCoverageScoreLbl);

//        JLabel passageCoverageScoreLbl = getjLabel("Passage coverage score:    ", result.getPassageCoverageScore());
//        passageCoverageScoreLbl.setToolTipText("We define a passage to be a pair of input and output transition in a place.\n" +
//                "When a passage appears in a fitting window, we count it as covered.\nThis score represent the fraction of " +
//                "the covered passages.\nThe larger the score the less the dead (unused) parts in the model.");
//        panel.add(passageCoverageScoreLbl);
//
//        return panel;
//    }

    private static JLabel getjLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(label.getFont().deriveFont(20.0f));
        return label;
    }

    private static String getEvaluationTextResult(double result) {
        DecimalFormat df = new DecimalFormat("#.#####");
        return df.format(result);
    }

    public static JPanel getComplexEvaluationResultComponent(GroupedEvaluationResult evaluationResult) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        for (AbstractEvaluationResult result : evaluationResult.getResults()) {
            panel.add(ComponentFactory.getLPMEvaluationResultComponent(result));
        }
        panel.add(getjLabel("Aggregate Result:    " +
                getEvaluationTextResult(evaluationResult.getResult(new EvaluationResultAggregateOperation()))));

        return panel;
    }

    private static JPanel getLPMEvaluationResultComponent(AbstractEvaluationResult evaluationResult) {
        if (evaluationResult instanceof GroupedEvaluationResult)
            return getComplexEvaluationResultComponent((GroupedEvaluationResult) evaluationResult);
//        if (evaluationResult instanceof PassageCoverageEvaluationResult)
//            return getCoveredPassages((PassageCoverageEvaluationResult) evaluationResult);
        if (evaluationResult instanceof SimpleEvaluationResult)
            return getSimpleEvaluationResultComponent((SimpleEvaluationResult) evaluationResult);
//        if (evaluationResult instanceof FittingWindowsEvaluationResult)
//            return getWindowsEvaluationResultComponent((FittingWindowsEvaluationResult) evaluationResult);
//        if (evaluationResult instanceof TransitionsOverlappingEvaluationResult)
//            return getTransitionOverlappingEvaluationResultComponent((TransitionsOverlappingEvaluationResult) evaluationResult);

        return null;
    }

    private static JPanel getCoveredPassages(PassageCoverageEvaluationResult evaluationResult) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = getjLabel(evaluationResult.getId() + ":    " + evaluationResult.getPassages());
        panel.add(label);

        return panel;
    }

    private static JPanel getSimpleEvaluationResultComponent(SimpleEvaluationResult evaluationResult) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = getjLabel(evaluationResult.getId() + ":    " +
                getEvaluationTextResult(evaluationResult.getResult()));
        panel.add(label);

        return panel;
    }

//    private static JPanel getTransitionOverlappingEvaluationResultComponent(TransitionsOverlappingEvaluationResult evaluationResult) {
//        JPanel panel = new JPanel();
//        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//
//        JLabel label = getjLabel("Transition overlapping score:    ", evaluationResult.getResult());
//        label.setToolTipText("This score tries to capture the structure of the model.\nHigh score it means that " +
//                "all places bring information to the model.\nWhen the score is low it means that we have at least one pair " +
//                "of places which are very similar to each other.");
//        panel.add(label);
//
//        return panel;
//    }
}
