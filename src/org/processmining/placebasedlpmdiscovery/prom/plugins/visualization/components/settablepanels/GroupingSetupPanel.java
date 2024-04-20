package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.settablepanels;

import javax.swing.*;
import java.awt.*;

public class GroupingSetupPanel extends JPanel {

    public GroupingSetupPanel() {
        this.setLayout(new GridLayout(1, 2));

        this.add(new LPMSimilaritySetupPanel());
        this.add(new ClusteringSetupPanel());
    }
}