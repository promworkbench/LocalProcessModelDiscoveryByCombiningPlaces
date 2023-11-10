package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.math3.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class BaseLPMDiscoveryResultComponent extends JComponent {

    private JPanel tablePanel;
    private final Map<Pair<Integer, Integer>, SettablePanelContainer> settablePanels;

    public BaseLPMDiscoveryResultComponent(int countSettablePanels) {
        this.setLayout(new GridBagLayout());
        this.settablePanels = new HashMap<>();

        init(countSettablePanels);
    }

    private void init(int countSettablePanels) {
        // setting up table container
        this.tablePanel = new JPanel();
        this.tablePanel.setLayout(new BorderLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 0.66;
        if (countSettablePanels < 4) {
            c.weightx = 0.33;
            c.gridwidth = 3;
            c.gridheight = 2;
        } else if (countSettablePanels < 6) {
            c.weightx = 0.66;
            c.gridwidth = 2;
            c.gridheight = 2;
        } else {
            throw new NotImplementedException("You can not have more than 5 panels.");
        }
        this.add(this.tablePanel, c);

        // setting up settable panels containers
        int countSettableContainers = countSettablePanels < 4 ? 3 : 5;
        c.weightx = 0.33;
        c.weighty = 0.33;
        c.gridwidth = 1;
        c.gridheight = 1;
        for (int i = 0; i < countSettableContainers; ++i) {
            SettablePanelContainer container = new SettablePanelContainer();
            if (i < 3) {
                this.settablePanels.put(new Pair<>(2, i), container);
                c.gridx = 2;
                c.gridy = i;
                container.add(new JLabel(c.gridx + "," + c.gridy));
            } else {
                this.settablePanels.put(new Pair<>(i % 3, 2), new SettablePanelContainer());
                c.gridx = i % 3;
                c.gridy = 2;
                container.add(new JLabel(c.gridx + "," + c.gridy));
            }
            this.add(container, c);
        }
    }

}
