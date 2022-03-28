package org.processmining.placebasedlpmdiscovery.placechooser.placerankconverters;

import java.util.Comparator;

public class RankedPlaceComparator implements Comparator<RankedPlace> {
    @Override
    public int compare(RankedPlace o1, RankedPlace o2) {
        while (o1.getRanks().hasNext() && o2.getRanks().hasNext()) {
            double o1ComparisonValue = o1.getRanks().next();
            double o2ComparisonValue = o2.getRanks().next();

            if (o1ComparisonValue != o2ComparisonValue) {
                return Double.compare(o1ComparisonValue, o2ComparisonValue);
            }
        }
        return 0;
    }
}
