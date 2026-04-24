package org.processmining.lpms.quality.alignments.temp;

import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.classification.XEventClasses;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.plugins.connectionfactories.logpetrinet.TransEvClassMapping;


public class PNLogConnector {

    /**
     * Compute a transition to event class mapping based on the name (name
     * classifier not considering lifecycle).
     *
     * @param eventClasses Event classes to which transitions should be mapped.
     * @param dummy        Dummy event class to which silent transitions are mapped.
     * @param pn           Petri net whose transition should be mapped
     * @return Mapping from Transition to Event Class
     */
    public static TransEvClassMapping instantiateTransEventMappingEqualName(
            XEventClasses eventClasses, XEventClass dummy, Petrinet pn) {
        TransEvClassMapping mapping;
        mapping = new TransEvClassMapping(eventClasses.getClassifier(), dummy);
        int sucessfulVisMapping = 0;
        int visTransitions = 0;
        for (Transition t : pn.getTransitions()) {
            if (t.isInvisible()) {
                mapping.put(t, dummy);
            } else {
                XEventClass eventClass = eventClasses.getByIdentity(t.getLabel());
                if (eventClass != null) {
                    mapping.put(t, eventClass);
                    sucessfulVisMapping++;
                }
                visTransitions++;
            }
        }

        return mapping;

    }

}

