package org.processmining.placebasedlpmdiscovery.model.lpmmodels.processtree;

import org.processmining.placebasedlpmdiscovery.model.logs.activities.Activity;
import org.processmining.placebasedlpmdiscovery.model.lpmmodels.LPMModel;

public class ProcessTree implements LPMModel<ExecutableProcessTree> {

    public ProcessTree(Activity activity) {

    }

    @Override
    public ExecutableProcessTree toExecutable() {
        return null;
    }
}
