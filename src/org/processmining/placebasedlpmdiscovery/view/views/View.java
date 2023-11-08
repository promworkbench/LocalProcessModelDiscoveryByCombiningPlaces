package org.processmining.placebasedlpmdiscovery.view.views;

import org.processmining.placebasedlpmdiscovery.view.controllers.ViewController;
import org.processmining.placebasedlpmdiscovery.view.models.ViewModel;

public interface View<C extends ViewController, M extends ViewModel> {
    void setListener(C listener);

    void display(M model);
}
