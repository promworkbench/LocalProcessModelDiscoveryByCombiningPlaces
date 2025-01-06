package org.processmining.placebasedlpmdiscovery.model.logs;

import org.deckfour.xes.model.XAttributeLiteral;
import org.deckfour.xes.model.XEvent;
import org.processmining.placebasedlpmdiscovery.model.logs.activities.Activity;
import org.processmining.placebasedlpmdiscovery.model.logs.activities.ActivityCache;

public class XEventWrapper implements Event {

    private final XEvent originalEvent;
    private final Activity activity;

    public XEventWrapper(XEvent originalEvent, String activityKey) {
        this.originalEvent = originalEvent;
        this.activity =
                ActivityCache.getInstance().getActivity(
                        ((XAttributeLiteral) this.originalEvent.getAttributes().get(activityKey)).getValue());
    }

    @Override
    public Activity getActivity() {
        return this.activity;
    }
}
