package org.processmining.placebasedlpmdiscovery.plugins.visualization.controllers;

import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.serializable.LPMResult;
import org.processmining.placebasedlpmdiscovery.plugins.mining.InteractiveLPMsDiscovery;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.components.ComponentId;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.components.SettablePanelContainer;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.components.WeirdComponentController;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.components.tables.TableComposition;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.components.tables.factories.LPMResultPluginVisualizerTableFactory;

import javax.swing.*;
import java.awt.*;

public class InteractiveLPMsDiscoveryController implements WeirdComponentController<LocalProcessModel> {

    private LPMResult result;

    private JComponent container;
    private JComponent tablePanel;
    private JComponent settablePanels;

    public InteractiveLPMsDiscoveryController(UIPluginContext context, InteractiveLPMsDiscovery interactiveLPMsDiscovery) {
        result = interactiveLPMsDiscovery.getResult();

        init();
    }

    private void init() {
        container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));

        initTablePanel();
        container.add(tablePanel);

        initSettablePanels();
        container.add(settablePanels);
    }

    private void initTablePanel() {
        this.tablePanel = new TableComposition<>(this.result, new LPMResultPluginVisualizerTableFactory(), this);
    }

    private void initSettablePanels() {
        // set up the layout of this component
        this.settablePanels = new JPanel();
        GridBagConstraints gbc = new GridBagConstraints();
        this.settablePanels.setLayout(new GridBagLayout());

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        this.settablePanels.add(new SettablePanelContainer(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        this.settablePanels.add(new SettablePanelContainer(), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        this.settablePanels.add(new SettablePanelContainer(), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        this.settablePanels.add(new SettablePanelContainer(), gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        this.settablePanels.add(new SettablePanelContainer(), gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        this.settablePanels.add(new SettablePanelContainer(), gbc);


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
        return container;
    }

    @Override
    public void componentExpansion(ComponentId componentId, boolean expanded) {

    }

    @Override
    public void newSelection(LocalProcessModel selectedObject) {

    }
}
