package org.processmining.placebasedlpmdiscovery.placechooser.placerankconverters;

import org.processmining.placebasedlpmdiscovery.analysis.analyzers.loganalyzer.LEFRMatrix;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.Transition;

public class TotalPassageCoveragePlaceRankConverter implements PlaceRankConverter {

    private final LEFRMatrix lefr; // TODO: should this class know about the lefr?

    public TotalPassageCoveragePlaceRankConverter(LEFRMatrix lefr) {
        this.lefr = lefr;
    }

    @Override
    public Double convert(Place place) {
        int passageCountSum = 0;
        for (Transition inTr : place.getInputTransitions()) {
            if (inTr.isInvisible())
                continue;
            for (Transition outTr : place.getOutputTransitions()) {
                if (outTr.isInvisible())
                    continue;
                passageCountSum += lefr.get(inTr.getLabel(), outTr.getLabel());
            }
        }
        return (double) passageCountSum;
    }
}
