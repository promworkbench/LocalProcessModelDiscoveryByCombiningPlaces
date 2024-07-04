package org.processmining.placebasedlpmdiscovery.view.components.lpmsetdisplay;

import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.view.listeners.NewElementSelectedListener;

import java.util.Collection;
import java.util.Map;

public interface LPMSetDisplayComponentFactory {

    LPMSetDisplayComponent createLPMSetDisplayComponent(LPMSetDisplayComponentType type,
                                                        Collection<LocalProcessModel> lpms,
                                                        Map<String, Object> parameters);

    LPMSetDisplayComponent createLPMSetDisplayComponent(LPMSetDisplayComponentType type,
                                                        Collection<LocalProcessModel> lpms,
                                                        NewElementSelectedListener<LocalProcessModel> listener,
                                                        Map<String, Object> parameters);
}
