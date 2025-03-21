package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.eventattributesummaries;

import org.processmining.framework.util.ui.widgets.ProMComboCheckBox;
import org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary.DistinctValuesAttributeSummary;

import javax.swing.*;
import java.awt.*;

public class DistinctValuesEventAttributeSummaryComponent extends EventAttributeSummaryComponent<DistinctValuesAttributeSummary<?,?>> {
    public DistinctValuesEventAttributeSummaryComponent(DistinctValuesAttributeSummary<?,?> model) {
        super(model);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setBackground(new Color(60, 60, 60, 160));

        ProMComboCheckBox valueChooser = new ProMComboCheckBox(model.getRepresentationFeatures().keySet().stream().distinct().toArray(), false);
        this.add(valueChooser);
    }
}
