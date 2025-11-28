package org.processmining.lpms.transformers.expanders.withactivity;

import org.processmining.lpms.model.petrinets.ExecutablePetriNet;
import org.processmining.lpms.model.petrinets.nodes.Transition;
import org.processmining.placebasedlpmdiscovery.model.logs.activities.Activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OnlyFirePNActivityExpander implements PNActivityExpander<ExecutablePetriNet> {

    @Override
    public Collection<ExecutablePetriNet> expand(ExecutablePetriNet lpm, Activity activity) {
        List<ExecutablePetriNet> extensions = new ArrayList<>();
        Collection<Transition> enabled = lpm.getEnabledForLabel(activity.getName());
        for (Transition transition : enabled) {
            ExecutablePetriNet newLPM = lpm.deepCopy();
            boolean fired = newLPM.fireOnPosition(lpm.getNodePosition(transition));
            if (fired) {
                extensions.add(newLPM);
            }
        }
        return extensions;
    }
}
