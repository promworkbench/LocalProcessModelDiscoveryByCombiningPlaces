package org.processmining.placebasedlpmdiscovery.plugins.visualization.controllers;

import org.processmining.framework.plugin.PluginContext;
import org.processmining.models.graphbased.AttributeMap;
import org.processmining.models.graphbased.ViewSpecificAttributeMap;
import org.processmining.models.graphbased.directed.DirectedGraphNode;
import org.processmining.models.graphbased.directed.petrinet.PetrinetGraph;
import org.processmining.models.graphbased.directed.petrinet.elements.Place;
import org.processmining.models.jgraph.ProMJGraphVisualizer;
import org.processmining.models.jgraph.visualization.ProMJGraphPanel;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Transition;
import org.processmining.placebasedlpmdiscovery.model.serializable.LPMResult;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.components.ComponentId;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.components.SettableComponentFactory;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.components.SettablePanelContainer;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.components.WeirdComponentController;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.components.tables.TableComposition;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.components.tables.factories.LPMResultPluginVisualizerTableFactory;
import org.processmining.placebasedlpmdiscovery.utils.LocalProcessModelUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class InteractiveLPMsDiscoveryController implements WeirdComponentController<LocalProcessModel> {

    private final LPMResult result;
    private final PluginContext context;

    // main panels
    private JComponent container;
    private JComponent tablePanel;
    private JComponent settablePanels;
    private JComponent lpmGraphPanel;

    public InteractiveLPMsDiscoveryController(LPMResult lpmResult, PluginContext context) {
        this.result = lpmResult;
        this.context = context;

        init();
    }

    private void init() {
        container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));

        initSettablePanels();
        initTablePanel();

        // set the preferred dimension of the two containers
        int windowHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        int windowWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        tablePanel.setPreferredSize(new Dimension(15 * windowWidth / 100, windowHeight));
        settablePanels.setPreferredSize(new Dimension(80 * windowWidth / 100, windowHeight));

        // add the table and LPM visualization containers and add some space between them
        container.add(tablePanel);
        container.add(Box.createRigidArea(new Dimension(windowWidth / 100, windowHeight)));
        container.add(settablePanels);
    }

    private void initTablePanel() {
        this.tablePanel = new TableComposition<>(this.result, new LPMResultPluginVisualizerTableFactory(), this);
    }

    private void initSettablePanels() {
        SettableComponentFactory scf = new SettableComponentFactory();

        // set up the layout of this component
        this.settablePanels = new JPanel();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.insets = new Insets(3, 3, 3, 3);
        this.settablePanels.setLayout(new GridBagLayout());

        addLpmGraphPanel();

        gbc.gridx = 0;
        gbc.gridy = 1;
        this.settablePanels.add(new SettablePanelContainer(scf), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        this.settablePanels.add(new SettablePanelContainer(scf), gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        this.settablePanels.add(new SettablePanelContainer(scf), gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        this.settablePanels.add(new SettablePanelContainer(scf), gbc);
    }

    private void addLpmGraphPanel() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.weighty = 1;

        this.lpmGraphPanel = new JPanel();
        this.lpmGraphPanel.setLayout(new BorderLayout());

        this.settablePanels.add(this.lpmGraphPanel, gbc);
    }

    private void updateLpmGraphPanel (LocalProcessModel selectedObject) {
        // if in the visualizer component there is a LPM drawn
        if (this.lpmGraphPanel.getComponents().length >= 1)
            this.lpmGraphPanel.remove(0); // remove it

        // create the visualizer
        PetrinetGraph net = LocalProcessModelUtils.getAcceptingPetriNetRepresentation(selectedObject).getNet();
        ViewSpecificAttributeMap map = new ViewSpecificAttributeMap();

        for (final Place place : net.getPlaces()) {
            map.putViewSpecific(place, AttributeMap.MOVEABLE, false);
            map.putViewSpecific(place, AttributeMap.RESIZABLE, false);
        }

        ProMJGraphPanel proMJGraphPanel = ProMJGraphVisualizer.instance().visualizeGraph(context, net, map);
        proMJGraphPanel.getGraph().addGraphSelectionListener(e -> {
            if (proMJGraphPanel.getSelectedNodes().size() > 1) return;

            List<DirectedGraphNode> collect = proMJGraphPanel.getSelectedNodes().stream().limit(1).collect(Collectors.toList());
            if (collect.isEmpty()) return;

            DirectedGraphNode node = collect.get(0);
            if (node instanceof Place)
                placeSelected(selectedObject.getPlace(((Place) node).getId()));
        });

        this.lpmGraphPanel.add(
                proMJGraphPanel,
                BorderLayout.CENTER);

        this.lpmGraphPanel.revalidate(); // revalidate the component
    }

    public JComponent getComponent() {
        return container;
    }

    @Override
    public void componentExpansion(ComponentId componentId, boolean expanded) {

    }

    @Override
    public void newSelection(LocalProcessModel selectedObject) {
        updateLpmGraphPanel(selectedObject);
    }

    @Override
    public void placeSelected(org.processmining.placebasedlpmdiscovery.model.Place p) {

    }

    @Override
    public void transitionSelected(Transition t) {

    }
}
