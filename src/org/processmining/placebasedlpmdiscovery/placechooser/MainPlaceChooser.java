package org.processmining.placebasedlpmdiscovery.placechooser;

import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.analysis.analyzers.loganalyzer.LEFRMatrix;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.placechooser.placepredicates.EmptyIOTransitionSetPlacePredicate;
import org.processmining.placebasedlpmdiscovery.placechooser.placepredicates.SelfLoopPlacePredicate;
import org.processmining.placebasedlpmdiscovery.placechooser.placerankconverters.RankedPlace;
import org.processmining.placebasedlpmdiscovery.placechooser.placerankconverters.RankedPlaceComparator;
import org.processmining.placebasedlpmdiscovery.placechooser.placerankconverters.TotalPassageCoveragePlaceRankConverter;
import org.processmining.placebasedlpmdiscovery.placechooser.placerankconverters.TransitionCountPlaceRankConverter;
import org.processmining.placebasedlpmdiscovery.placechooser.placetransformers.IncludedActivitiesPlaceTransformer;
import org.processmining.placebasedlpmdiscovery.placechooser.placetransformers.PassageUsagePlaceTransformer;
import org.processmining.placebasedlpmdiscovery.utils.LogUtils;

import java.util.Set;
import java.util.stream.Collectors;

public class MainPlaceChooser implements PlaceChooser {

    private final PlaceChooserParameters placeChooserParameters;
    private final XLog log;

    /**
     * Limited eventually follow relation
     */
    private LEFRMatrix lefr;

    public MainPlaceChooser(XLog log, PlaceChooserParameters placeChooserParameters, LEFRMatrix lefr) {
        this.placeChooserParameters = placeChooserParameters;
        this.log = log;

        this.lefr = lefr; // TODO: what happens with information that are needed by the filters, transformers, rankers and so on.
    }

    @Override
    public Set<Place> choose(Set<Place> places, int count) {
        return places.stream()
                .map(new IncludedActivitiesPlaceTransformer(this.placeChooserParameters.getChosenActivities()))
                .map(new PassageUsagePlaceTransformer(LogUtils.getFollowRelations(log, placeChooserParameters.getFollowRelationsLimit()))) // TODO: this might be duplicate work, since the lefr should already contain it
                .filter(new SelfLoopPlacePredicate())
                .filter(new EmptyIOTransitionSetPlacePredicate())
                .map(p -> new RankedPlace(p, new TransitionCountPlaceRankConverter().convert(p), new TotalPassageCoveragePlaceRankConverter(lefr).convert(p)))
                .sorted(new RankedPlaceComparator())
                .map(RankedPlace::getPlace)
                .collect(Collectors.toSet());
    }
}
