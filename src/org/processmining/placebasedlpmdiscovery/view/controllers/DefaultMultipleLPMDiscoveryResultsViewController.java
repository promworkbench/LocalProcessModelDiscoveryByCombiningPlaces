package org.processmining.placebasedlpmdiscovery.view.controllers;

import org.processmining.placebasedlpmdiscovery.view.models.MultipleLPMDiscoveryResultsViewModel;
import org.processmining.placebasedlpmdiscovery.view.views.MultipleLPMDiscoveryResultsView;

public class DefaultMultipleLPMDiscoveryResultsViewController implements MultipleLPMDiscoveryResultsViewController {

    private final MultipleLPMDiscoveryResultsView view;
    private final MultipleLPMDiscoveryResultsViewModel model;

    public DefaultMultipleLPMDiscoveryResultsViewController(MultipleLPMDiscoveryResultsView view,
                                                            MultipleLPMDiscoveryResultsViewModel model) {
        this.view = view;
        this.view.setListener(this);
        this.model = model;
    }

    @Override
    public void selectFirstSet() {
        model.setLPMs(model.getLPMDiscoveryResult("Set 1").getAllLPMs());
        view.display(model);
    }

    @Override
    public void selectSecondSet() {
        model.setLPMs(model.getLPMDiscoveryResult("Set 2").getAllLPMs());
        view.display(model);
    }

    @Override
    public void selectIntersection() {
        model.setLPMs(model.intersection("Set 1", "Set 2"));
        view.display(model);
    }

    @Override
    public void selectUnion() {
        model.setLPMs(model.union("Set 1", "Set 2"));
        view.display(model);
    }

    @Override
    public void selectOnlyInFirstSet() {
        model.setLPMs(model.diff("Set 1", "Set 2"));
        view.display(model);
    }

    @Override
    public void selectOnlyInSecondSet() {
        model.setLPMs(model.diff("Set 2", "Set 1"));
        view.display(model);
    }
}
