package org.processmining.lpms.transformers.expanders.withactivity;

import org.processmining.lpms.model.LPM;
import org.processmining.lpms.model.petrinets.PetriNet;
import org.processmining.lpms.model.petrinets.placebased.DefaultPBPetriNet;
import org.processmining.placebasedlpmdiscovery.model.logs.activities.Activity;

import java.util.Collection;
import java.util.Collections;

public class StartPNActivityExpander implements PNActivityExpander {

    @Override
    public Collection<LPM> expand(LPM lpm, Activity activity) {
        if (lpm instanceof PetriNet) {
            PetriNet newLPM = new DefaultPBPetriNet((PetriNet) lpm);
            newLPM.addInitialPlaceWithActivity(activity);
            return Collections.singleton(newLPM);
        }
        throw new IllegalArgumentException("An LPM of type " + lpm.getClass().getSimpleName() + " cannot be expanded by " + this.getClass().getSimpleName());
    }
}
