package org.processmining.placebasedlpmdiscovery.view.components.lpmdisplay;

import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.acceptingpetrinetclassicalreductor.algorithms.ReduceUsingMurataRulesAlgorithm;
import org.processmining.acceptingpetrinetclassicalreductor.parameters.ReduceUsingMurataRulesParameters;
import org.processmining.models.graphbased.AttributeMap;
import org.processmining.models.graphbased.ViewSpecificAttributeMap;
import org.processmining.models.jgraph.ProMJGraphVisualizer;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.StandardLPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.EventCoverageEvaluationResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.utils.LocalProcessModelUtils;
import org.processmining.placebasedlpmdiscovery.view.components.LPMDisplayComponent;

import javax.swing.*;

public class LPMPetriNetComponent extends JComponent implements LPMDisplayComponent {

    public LPMPetriNetComponent(LocalProcessModel lpm) {
        AcceptingPetriNet net = LocalProcessModelUtils.getAcceptingPetriNetRepresentation(lpm);
        ReduceUsingMurataRulesAlgorithm reduceMurataAlg = new ReduceUsingMurataRulesAlgorithm();
        AcceptingPetriNet reducedNet = reduceMurataAlg.apply(null, net, new ReduceUsingMurataRulesParameters());

        ViewSpecificAttributeMap map = new ViewSpecificAttributeMap();
        for (org.processmining.models.graphbased.directed.petrinet.elements.Transition t :
                reducedNet.getNet().getTransitions()) {
            if (!t.isInvisible()) {
                String label = getLabel(t.getLabel(), lpm);
                map.putViewSpecific(t, AttributeMap.LABEL, label);
            }
        }

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(ProMJGraphVisualizer.instance().visualizeGraphWithoutRememberingLayout(net.getNet(), map));
    }

    private String getLabel(String trLabel, LocalProcessModel lpm) {
        EventCoverageEvaluationResult res = lpm.getAdditionalInfo()
                .getEvaluationResult(StandardLPMEvaluationResultId.EventCoverageEvaluationResult.name(),
                        EventCoverageEvaluationResult.class);

        return res == null ? trLabel : trLabel + ": " + System.lineSeparator() +
                res.getCoveredEventsCount(trLabel) + "/" + res.getEventCountPerActivity().get(trLabel);
    }

    @Override
    public JComponent getComponent() {
        return this;
    }
}
