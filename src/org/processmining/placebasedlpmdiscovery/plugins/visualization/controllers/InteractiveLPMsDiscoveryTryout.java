package org.processmining.placebasedlpmdiscovery.plugins.visualization.controllers;

import org.processmining.placebasedlpmdiscovery.plugins.visualization.components.SettablePanelContainer;

import javax.swing.*;
import java.awt.*;

public class InteractiveLPMsDiscoveryTryout {

    private final JPanel container;

    private final JPanel tablePanel;
    private final JComponent settablePanels;

    public InteractiveLPMsDiscoveryTryout() {
//        result = interactiveLPMsDiscovery.getResult();

        container = new JPanel();
        tablePanel = new JPanel();
        settablePanels = new JPanel();

        init();
    }

    private void init() {
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));

        initTablePanel();
        container.add(tablePanel);

        initSettablePanels();
        container.add(settablePanels);

        // create the table and LPM visualization containers
//        visualizerComponent = createVisualizerComponent();
//        tableContainer = createTableContainer(visualizerComponent);
//
//        // set the preferred dimension of the two containers
//        int windowHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
//        int windowWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
//        tableContainer.setPreferredSize(new Dimension(15 * windowWidth / 100, windowHeight));
//        visualizerComponent.setPreferredSize(new Dimension(80 * windowWidth / 100, windowHeight));
//
//        // add the table and LPM visualization containers and add some space between them
//        this.add(tableContainer);
//        this.add(Box.createRigidArea(new Dimension(windowWidth / 100, windowHeight)));
//        this.add(visualizerComponent);
    }

    private void initTablePanel() {

    }

    private void initSettablePanels() {
        // set up the layout of this component
        settablePanels.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;

        gbc.gridx = 0;
        gbc.gridy = 0;
        settablePanels.add(new SettablePanelContainer(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        settablePanels.add(new SettablePanelContainer(), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        settablePanels.add(new SettablePanelContainer(), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        settablePanels.add(new SettablePanelContainer(), gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        settablePanels.add(new SettablePanelContainer(), gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        settablePanels.add(new SettablePanelContainer(), gbc);
    }

    public JComponent getComponent() {
        return settablePanels;
    }
}
