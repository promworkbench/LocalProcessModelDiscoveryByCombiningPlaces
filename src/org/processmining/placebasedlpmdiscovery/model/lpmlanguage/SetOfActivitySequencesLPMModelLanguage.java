package org.processmining.placebasedlpmdiscovery.model.lpmlanguage;

import org.processmining.placebasedlpmdiscovery.model.logs.activities.Activity;

import java.util.List;
import java.util.Set;

public class SetOfActivitySequencesLPMModelLanguage implements ActivitySequencesLPMModelLanguage {

    private final Set<List<Activity>> activitySequences;

    public SetOfActivitySequencesLPMModelLanguage(Set<List<Activity>> activitySequences) {
        this.activitySequences = activitySequences;
    }

    public Set<List<Activity>> getActivitySequences() {
        return activitySequences;
    }
}
