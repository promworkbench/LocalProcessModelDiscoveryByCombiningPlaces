package org.processmining.lpms.transformers.expanders;

import org.processmining.lpms.model.LPM;
import org.processmining.placebasedlpmdiscovery.model.logs.activities.Activity;

import java.util.Collection;

public interface LPMActivityExpander<T extends LPM> {

    Collection<T> expand(T lpm, Activity activity);
}
