package org.processmining.placebasedlpmdiscovery.view.components;

import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.view.components.lpmsetdisplay.LPMSetDisplayComponentType;
import org.processmining.placebasedlpmdiscovery.view.listeners.NewElementSelectedListener;

import java.util.Collection;

public interface ComponentFactory {

    LPMSetDisplayComponent createLPMSetDisplayComponent(LPMSetDisplayComponentType type,
                                                        Collection<LocalProcessModel> lpms);

    LPMSetDisplayComponent createLPMSetDisplayComponent(LPMSetDisplayComponentType type,
                                                        Collection<LocalProcessModel> lpms,
                                                        NewElementSelectedListener<LocalProcessModel> listener);
}
