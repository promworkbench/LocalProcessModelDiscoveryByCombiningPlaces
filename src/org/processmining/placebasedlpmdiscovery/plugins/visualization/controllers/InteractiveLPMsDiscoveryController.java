package org.processmining.placebasedlpmdiscovery.plugins.visualization.controllers;

import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.placebasedlpmdiscovery.model.serializable.LPMResult;
import org.processmining.placebasedlpmdiscovery.plugins.mining.InteractiveLPMsDiscovery;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.components.SettablePanelContainer;

import javax.swing.*;
import java.awt.*;

public class InteractiveLPMsDiscoveryController {

    private LPMResult result;

    private final JComponent content;

    public InteractiveLPMsDiscoveryController(UIPluginContext context, InteractiveLPMsDiscovery interactiveLPMsDiscovery) {
        result = interactiveLPMsDiscovery.getResult();

        content = new JPanel();

        init();
    }

    private void init() {
        // set up the layout of this component
        GridBagConstraints gbc = new GridBagConstraints();
        content.setLayout(new GridBagLayout());

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        content.add(new SettablePanelContainer(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        content.add(new SettablePanelContainer(), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        content.add(new SettablePanelContainer(), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        content.add(new SettablePanelContainer(), gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        content.add(new SettablePanelContainer(), gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        content.add(new SettablePanelContainer(), gbc);


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

    public JComponent getComponent() {
        return content;
    }
}
