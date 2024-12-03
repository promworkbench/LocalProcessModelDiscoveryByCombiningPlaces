package org.processmining.placebasedlpmdiscovery.model.logs;

import org.processmining.placebasedlpmdiscovery.model.logs.activities.Activity;

public interface Event {
    Activity getActivity();
}
