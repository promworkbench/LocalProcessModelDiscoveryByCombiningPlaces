package org.processmining.lpms.transformers.expanders.withactivity;

import org.processmining.lpms.model.petrinets.ExecutablePetriNet;
import org.processmining.placebasedlpmdiscovery.model.logs.activities.Activity;

import java.util.Collection;
import java.util.Collections;

public class StartPNActivityExpander implements PNActivityExpander<ExecutablePetriNet> {

    @Override
    public Collection<ExecutablePetriNet> expand(ExecutablePetriNet lpm, Activity activity) {
        ExecutablePetriNet newLPM = lpm.deepCopy();
        newLPM.addPlace(Collections.emptyList(), Collections.singletonList(newLPM.addTransition(activity.getName())));
        return Collections.singleton(newLPM);
    }
}
