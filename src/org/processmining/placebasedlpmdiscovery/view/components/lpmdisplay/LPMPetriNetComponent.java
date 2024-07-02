package org.processmining.placebasedlpmdiscovery.view.components.lpmdisplay;

import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.acceptingpetrinetclassicalreductor.plugins.ReduceUsingMurataRulesPlugin;
import org.processmining.framework.plugin.PluginContext;
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

    public LPMPetriNetComponent(LocalProcessModel lpm, PluginContext context) {
        AcceptingPetriNet net = LocalProcessModelUtils.getAcceptingPetriNetRepresentation(lpm);
        ViewSpecificAttributeMap map = new ViewSpecificAttributeMap();

        ReduceUsingMurataRulesPlugin reductorPlugin = new ReduceUsingMurataRulesPlugin();
        net = reductorPlugin.runDefault(context, net);

        for (org.processmining.models.graphbased.directed.petrinet.elements.Transition t : net.getNet().getTransitions()) {
            if (!t.isInvisible()) {
                String label = getLabel(t.getLabel(), lpm);
                map.putViewSpecific(t, AttributeMap.LABEL, label);

//                map.putViewSpecific(t, AttributeMap.EXTRALABELS, new String[]{""+lpm.getCount(t)});
//                map.putViewSpecific(t, AttributeMap.EXTRALABELPOSITIONS, new Point2D[]{new Point2D.Double(10, 10)});
            }
        }

        JComponent component = new JPanel();
        component.setLayout(new BoxLayout(component, BoxLayout.X_AXIS));
//        component.add((new CustomAcceptingPetriNetVisualizer()).visualize(context, net));
        component.add(ProMJGraphVisualizer.instance().visualizeGraph(context, net.getNet(), map));
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
