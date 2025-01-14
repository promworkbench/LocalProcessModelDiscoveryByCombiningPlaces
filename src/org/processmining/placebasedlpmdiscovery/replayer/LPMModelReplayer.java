package org.processmining.placebasedlpmdiscovery.replayer;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.ReplayableLocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.logs.activities.Activity;
import org.processmining.placebasedlpmdiscovery.model.lpmmodels.ExecutableLPMModel;
import org.processmining.placebasedlpmdiscovery.model.lpmmodels.LPMModel;

import java.util.List;

public interface LPMModelReplayer {

    static LPMModelReplayer getInstance(LPMModel<?> model) {
        return getInstance(model.toExecutable());
    }

    static LPMModelReplayer getInstance(ExecutableLPMModel executableModel) {
        if (executableModel instanceof ReplayableLocalProcessModel) {
            ReplayableLocalProcessModel cExecutableModel = (ReplayableLocalProcessModel) executableModel;
            return new ReplayableLocalProcessModelReplayer(cExecutableModel);
        }
        throw new IllegalArgumentException("Unsupported executable model: " + executableModel);
    }

    boolean canReplayActivitySequence(List<Activity> activityFiringSequence);
}
