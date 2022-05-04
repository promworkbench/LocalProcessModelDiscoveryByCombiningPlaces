package org.processmining.placebasedlpmdiscovery.lpmevaluation.logs;

public class IntegerActivityMapping extends AbstractActivityMapping<Integer> {

    private int labelMapInd;

    @Override
    protected Integer map(String label) {
        labelMap.put(label, this.labelMapInd);
        reverseLabelMap.put(this.labelMapInd, label);
        this.labelMapInd++;
        return this.labelMapInd - 1;
    }
}
