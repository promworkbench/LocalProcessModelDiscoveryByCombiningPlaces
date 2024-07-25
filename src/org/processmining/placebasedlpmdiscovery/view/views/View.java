package org.processmining.placebasedlpmdiscovery.view.views;

import org.processmining.placebasedlpmdiscovery.view.controllers.ViewController;
import org.processmining.placebasedlpmdiscovery.view.models.ViewModel;

import javax.swing.*;

public interface View<C extends ViewController, M extends ViewModel> {
    @Deprecated
    void setListener(C listener);

    void display(M model);

    JComponent getComponent();
}
