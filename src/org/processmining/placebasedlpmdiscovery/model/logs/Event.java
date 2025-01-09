package org.processmining.placebasedlpmdiscovery.model.logs;

import org.processmining.placebasedlpmdiscovery.model.exporting.gson.GsonSerializable;
import org.processmining.placebasedlpmdiscovery.model.logs.activities.Activity;

public interface Event extends GsonSerializable {
    Activity getActivity();
}
