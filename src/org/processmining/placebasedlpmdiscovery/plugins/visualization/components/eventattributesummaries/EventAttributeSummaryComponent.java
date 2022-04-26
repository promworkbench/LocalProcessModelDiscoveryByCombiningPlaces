package org.processmining.placebasedlpmdiscovery.plugins.visualization.components.eventattributesummaries;

import org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary.EventAttributeSummary;

import javax.swing.*;

public abstract class EventAttributeSummaryComponent<T extends EventAttributeSummary<?, ?>> extends JPanel {

    protected T model;

    public EventAttributeSummaryComponent(T model) {
        this.model = model;
    }

    public T getModel() {
        return this.model;
    }

    public void setModel(T model) {
        this.model = model;
    }
}
