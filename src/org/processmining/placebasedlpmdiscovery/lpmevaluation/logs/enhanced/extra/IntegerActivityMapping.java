package org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.enhanced.extra;

import org.processmining.placebasedlpmdiscovery.model.logs.activities.ActivityCache;

public class IntegerActivityMapping extends AbstractActivityMapping<Integer> {

    private int labelMapInd;

    @Override
    protected Integer map(String label) {
        int mapValue =
                ActivityCache.getInstance().getIntForActivityId(ActivityCache.getInstance().getActivity(label).getId());
        labelMap.put(label, mapValue);
        reverseLabelMap.put(mapValue, label);
        return mapValue;
//        labelMap.put(label, this.labelMapInd);
//        reverseLabelMap.put(this.labelMapInd, label);
//        this.labelMapInd++;
//        return this.labelMapInd - 1;
    }
}
