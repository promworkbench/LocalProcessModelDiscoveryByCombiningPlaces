package org.processmining.lpms.transformers.expanders.withactivity;

import org.processmining.lpms.model.petrinets.PetriNet;
import org.processmining.placebasedlpmdiscovery.model.logs.activities.Activity;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

public class PBActivityExpander implements PNActivityExpander {

    private final HashSet<PNActivityExpander> expanders;

    public PBActivityExpander() {
        this.expanders = new HashSet<>();
        this.expanders.add(new StartPNActivityExpander());
    }

    @Override
    public Collection<PetriNet> expand(PetriNet lpm, Activity activity) {
        return expanders.stream()
                .map(e -> e.expand(lpm, activity))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }
}
