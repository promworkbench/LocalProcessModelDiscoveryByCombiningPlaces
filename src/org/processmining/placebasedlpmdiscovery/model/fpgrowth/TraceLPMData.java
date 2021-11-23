package org.processmining.placebasedlpmdiscovery.model.fpgrowth;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.ReplayableLocalProcessModel;

import java.util.Optional;

public class TraceLPMData {

    private final ReplayableLocalProcessModel rlpm;
    private ReplayableLocalProcessModel emptyRlpm;

    public TraceLPMData() {
        this.rlpm = new ReplayableLocalProcessModel();
    }

    public TraceLPMData(ReplayableLocalProcessModel rlpm, int event) {
        this.rlpm = rlpm;
        this.emptyRlpm = new ReplayableLocalProcessModel(rlpm);
        this.emptyRlpm.fire(event);
        if (!this.emptyRlpm.isEmptyMarking())
            this.emptyRlpm = null;
        else
            System.out.println("Viki");
    }

    public ReplayableLocalProcessModel getRlpm() {
        return rlpm;
    }

    public Optional<ReplayableLocalProcessModel> getEmptyRlpm() {
        return Optional.ofNullable(this.emptyRlpm);
    }
}
