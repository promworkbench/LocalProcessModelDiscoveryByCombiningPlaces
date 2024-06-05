package org.processmining.placebasedlpmdiscovery.view.controllers;

import org.processmining.placebasedlpmdiscovery.view.listeners.LPMDiscoveryResultViewListener;
import org.processmining.placebasedlpmdiscovery.view.models.LPMDiscoveryResultViewModel;
import org.processmining.placebasedlpmdiscovery.view.views.LPMDiscoveryResultView;

public interface LPMDiscoveryResultViewController extends ViewController, LPMDiscoveryResultViewListener {

    LPMDiscoveryResultView getView();

    LPMDiscoveryResultViewModel getModel();
}
