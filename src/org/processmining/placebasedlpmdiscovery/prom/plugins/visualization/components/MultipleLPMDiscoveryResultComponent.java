package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components;

import com.google.inject.Inject;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.controlcomponents.TwoLPMDiscoveryResultsComparisonComponent;
import org.processmining.placebasedlpmdiscovery.view.components.ComponentFactory;
import org.processmining.placebasedlpmdiscovery.view.components.lpmsetdisplay.LPMSetDisplayComponentType;
import org.processmining.placebasedlpmdiscovery.view.controllers.MultipleLPMDiscoveryResultsViewController;
import org.processmining.placebasedlpmdiscovery.view.listeners.MultipleLPMDiscoveryResultsViewListener;
import org.processmining.placebasedlpmdiscovery.view.models.MultipleLPMDiscoveryResultsViewModel;
import org.processmining.placebasedlpmdiscovery.view.views.MultipleLPMDiscoveryResultsView;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class MultipleLPMDiscoveryResultComponent extends JComponent implements MultipleLPMDiscoveryResultsView {

    private final ComponentFactory componentFactory;
    private MultipleLPMDiscoveryResultsViewListener listener;
    private boolean setupFinished;

    // sub-components
    private JComponent tablePanel;

    @Inject
    public MultipleLPMDiscoveryResultComponent(ComponentFactory componentFactory) {
        this.componentFactory = componentFactory;
    }

    private void initView() {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        JPanel settablePanels = new TwoLPMDiscoveryResultsComparisonComponent(this.listener);

        // set the preferred dimension of the two containers
        int windowHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        int windowWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        tablePanel.setPreferredSize(new Dimension(80 * windowWidth / 100, windowHeight));
        settablePanels.setPreferredSize(new Dimension(15 * windowWidth / 100, windowHeight));

        // add the table and LPM visualization containers and add some space between them
        this.add(tablePanel);
        this.add(Box.createRigidArea(new Dimension(windowWidth / 100, windowHeight)));
        this.add(settablePanels);

        // take a union of the nets

        // for each lpm measures on both logs are shown

        // passage usage numbers can be shown on both logs
        setupFinished = true;
    }

    @Override
    public void setListener(MultipleLPMDiscoveryResultsViewController listener) {
        this.listener = listener;
    }

    @Override
    public void display(MultipleLPMDiscoveryResultsViewModel model) {
        if (!setupFinished) initView();

        updateTable(model);
    }

    @Override
    public JComponent getComponent() {
        return this;
    }

    private void updateTable(MultipleLPMDiscoveryResultsViewModel model) {
        // if in the visualizer component there is an LPM drawn
        if (tablePanel.getComponents().length >= 1)
            tablePanel.remove(0); // remove it

        tablePanel.add(
                this.componentFactory.createLPMSetDisplayComponent(LPMSetDisplayComponentType.SimpleLPMsCollection,
                        model.getLPMs(), new HashMap<>()).getComponent(),
                BorderLayout.CENTER);

        tablePanel.revalidate();
    }
}
