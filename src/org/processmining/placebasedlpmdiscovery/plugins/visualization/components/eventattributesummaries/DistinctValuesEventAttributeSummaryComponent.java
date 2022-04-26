package org.processmining.placebasedlpmdiscovery.plugins.visualization.components.eventattributesummaries;

import org.processmining.framework.util.ui.widgets.ProMComboCheckBox;
import org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary.DistinctValuesEventAttributeSummary;

import javax.swing.*;
import java.awt.*;

public class DistinctValuesEventAttributeSummaryComponent extends EventAttributeSummaryComponent<DistinctValuesEventAttributeSummary<?,?>> {
    public DistinctValuesEventAttributeSummaryComponent(DistinctValuesEventAttributeSummary<?,?> model) {
        super(model);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setBackground(new Color(60, 60, 60, 160));

        ProMComboCheckBox valueChooser = new ProMComboCheckBox(model.getValues().stream().distinct().toArray(), false);
        this.add(valueChooser);
    }
}
