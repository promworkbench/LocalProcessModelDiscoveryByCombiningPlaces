package org.processmining.placebasedlpmdiscovery.prom.plugins.mining.wizards.steps;

import org.deckfour.xes.model.XLog;
import org.processmining.framework.util.ui.widgets.ProMComboCheckBox;
import org.processmining.framework.util.ui.widgets.ProMPropertiesPanel;
import org.processmining.framework.util.ui.wizard.ProMWizardStep;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.EventAttributeCollectorResult;
import org.processmining.placebasedlpmdiscovery.prom.plugins.mining.PlaceBasedLPMDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.eventattributesummaries.EventAttributeSummaryComponent;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.eventattributesummaries.EventAttributeSummaryComponentFactory;
import org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary.AttributeSummary;
import org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary.AttributeSummaryController;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.*;

public class LPMContextWizardStep extends ProMPropertiesPanel implements ProMWizardStep<PlaceBasedLPMDiscoveryParameters> {

    private static final String TITLE = "LPM Context Configuration";

    private final Collection<String> lastSelected;
    private final Map<String, EventAttributeSummaryComponent<?>> components;

    public LPMContextWizardStep(XLog log) {
        super(TITLE);

        this.lastSelected = new HashSet<>();
        AttributeSummaryController attributeSummaryController = new AttributeSummaryController();
        EventAttributeCollectorResult attributeSummaryResult = attributeSummaryController.computeEventAttributeSummary(log);

        this.components = new HashMap<>();
        for (String key : attributeSummaryResult.getAttributeKeys()) {
            AttributeSummary<?,?> attributeSummary = attributeSummaryResult
                    .getEventAttributeSummaryForAttributeKey(key);
            this.components.put(
                    key,
                    EventAttributeSummaryComponentFactory.getComponentForEventAttributeSummary(attributeSummary)
            );
        }

        ProMComboCheckBox attributes = new ProMComboCheckBox(attributeSummaryResult.getAttributeKeys().toArray(new String[0]), false);
        addProperty("Attribute Selection", attributes);
        attributes.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                Collection<String> selected = attributes.getSelectedItems();
                selected = selected == null ? new HashSet<>() : selected;
                if (shouldRecreate(selected))
                    recreate(selected);
            }
        });
    }

    private boolean shouldRecreate(Collection<String> selected) {
        if (selected.size() != this.lastSelected.size()) {
            return true;
        }
        for (String item : selected) {
            if (!this.lastSelected.contains(item)) {
                return true;
            }
        }
        return false;
    }

    private void recreate(Collection<String> selected) {
        Collection<String> difference;
        if (selected.size() > lastSelected.size()) {
            difference = new HashSet<>(selected);
            difference.removeAll(lastSelected);
            for (String key : difference) {
                this.lastSelected.add(key);
                JComponent component = components.get(key);
                this.addProperty(key, component);
            }
        } else {
            difference = new HashSet<>(lastSelected);
            difference.removeAll(selected);
            for (String key : difference) {
                JComponent component = components.get(key);
                if (component.getParent() != null && component.getParent().getParent() != null) {
                    component.getParent().getParent().remove(component.getParent());
                    lastSelected.remove(key);
                }
            }
        }
        this.validate();
    }

    @Override
    public PlaceBasedLPMDiscoveryParameters apply(PlaceBasedLPMDiscoveryParameters placeBasedLPMDiscoveryParameters, JComponent jComponent) {
        if (!canApply(placeBasedLPMDiscoveryParameters, jComponent)) {
            return placeBasedLPMDiscoveryParameters;
        }
        for (EventAttributeSummaryComponent<?> eventAttributeSummary : this.components.values()) {
            placeBasedLPMDiscoveryParameters.getEventAttributeSummary()
                    .put(eventAttributeSummary.getModel().getKey(), eventAttributeSummary.getModel());
        }
        return placeBasedLPMDiscoveryParameters;
    }

    @Override
    public boolean canApply(PlaceBasedLPMDiscoveryParameters placeBasedLPMDiscoveryParameters, JComponent jComponent) {
        return jComponent instanceof LPMContextWizardStep;
    }

    @Override
    public JComponent getComponent(PlaceBasedLPMDiscoveryParameters placeBasedLPMDiscoveryParameters) {
        return this;
    }

    @Override
    public String getTitle() {
        return TITLE;
    }
}
