package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.eventattributesummaries;

import org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary.AttributeSummary;

import javax.swing.*;

public abstract class EventAttributeSummaryComponent<T extends AttributeSummary<?, ?>> extends JPanel {

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
