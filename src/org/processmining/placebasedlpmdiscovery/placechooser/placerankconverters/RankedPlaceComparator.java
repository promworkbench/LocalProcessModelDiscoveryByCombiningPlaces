package org.processmining.placebasedlpmdiscovery.placechooser.placerankconverters;

import java.util.Comparator;

public class RankedPlaceComparator implements Comparator<RankedPlace> {
    @Override
    public int compare(RankedPlace o1, RankedPlace o2) {
        for (int i = 0; i < o1.getRanks().size(); ++i) {
            double o1ComparisonValue = o1.getRanks().get(i);
            double o2ComparisonValue = o2.getRanks().get(i);

            if (o1ComparisonValue != o2ComparisonValue) {
                return Double.compare(o1ComparisonValue, o2ComparisonValue);
            }
        }
        return 0;
    }
}
