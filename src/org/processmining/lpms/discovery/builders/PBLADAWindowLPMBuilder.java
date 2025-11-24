package org.processmining.lpms.discovery.builders;

import org.apache.commons.lang3.NotImplementedException;
import org.processmining.lpms.model.LPM;
import org.processmining.lpms.transformers.expanders.LPMActivityExpander;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.storage.WindowLPMStorage;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.SlidingWindowInfo;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.logs.activities.Activity;
import org.processmining.placebasedlpmdiscovery.prom.PlacesProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PBLADAWindowLPMBuilder implements LADAWindowLPMBuilder {

    private final Set<Place> places;
    private final LPMActivityExpander activityExpander;

    public PBLADAWindowLPMBuilder(PlacesProvider placesProvider, LPMActivityExpander activityExpander) {
        this.places = placesProvider.provide();
        this.activityExpander = activityExpander;
    }

    @Override
    public WindowLPMStorage build(SlidingWindowInfo windowInfo, WindowLPMStorage prevWindowResult) {
        WindowLPMStorage newStorage = prevWindowResult;
        if (windowInfo.getRemovedActivity() != null) {
            newStorage.removeLPMsFor(windowInfo.getRemovedActivity(), windowInfo.getStartPos() - 1);
        }
        if (windowInfo.getAddedActivity() != null) {
            Activity activity = windowInfo.getAddedActivity();
            while (newStorage.hasNext()) {
                LPM lpm = newStorage.next();
                Iterable<LPM> extensions = this.activityExpander.expand(lpm, activity);
                newStorage.addLPMsFor(activity, windowInfo.getEndPos(), extensions);

                extendByAddingInitial(lpm, activity);
                extendByConcatenation(lpm, activity);
                extendByFiring(lpm, activity);
            }
        }
        return newStorage;
    }

    private void extendByFiring(LPM lpm, Activity activity) {
        throw new NotImplementedException();
    }

    private void extendByConcatenation(LPM lpm, Activity activity) {
        throw new NotImplementedException();
    }

    private void extendByAddingInitial(LPM lpm, Activity activity) {
        throw new NotImplementedException();
    }
}
