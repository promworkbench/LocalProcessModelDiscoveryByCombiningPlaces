package org.processmining.lpms.transformers.expanders.withactivity;

import org.processmining.lpms.model.petrinets.ExecutablePetriNet;
import org.processmining.placebasedlpmdiscovery.model.logs.activities.Activity;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

public class PBActivityExpander implements PNActivityExpander<ExecutablePetriNet> {

    private final HashSet<PNActivityExpander<ExecutablePetriNet>> expanders;

    public PBActivityExpander() {
        this.expanders = new HashSet<>();
        this.expanders.add(new StartPNActivityExpander());
    }

    @Override
    public Collection<ExecutablePetriNet> expand(ExecutablePetriNet lpm, Activity activity) {
        return expanders.stream()
                .map(e -> e.expand(lpm, activity))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }
}
