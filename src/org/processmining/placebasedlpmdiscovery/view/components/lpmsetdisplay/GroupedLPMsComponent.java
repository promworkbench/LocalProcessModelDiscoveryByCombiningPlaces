package org.processmining.placebasedlpmdiscovery.view.components.lpmsetdisplay;

import org.processmining.placebasedlpmdiscovery.main.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.serializable.grouped.GroupedLPMResult;
import org.processmining.placebasedlpmdiscovery.model.serializable.grouped.GroupingProperty;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.visualizers.LPMResultVisualizer;
import org.processmining.placebasedlpmdiscovery.view.components.ComponentFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.HashMap;

public class GroupedLPMsComponent extends JComponent implements LPMSetDisplayComponent {

    private final LPMSetDisplayComponentFactory lpmSetDisplayComponentFactory;

    public GroupedLPMsComponent(Collection<LocalProcessModel> lpms,
                                String groupingKey,
                                LPMSetDisplayComponentFactory lpmSetDisplayComponentFactory) {

        this.lpmSetDisplayComponentFactory = lpmSetDisplayComponentFactory;

        GroupedLPMResult grouped = new GroupedLPMResult(lpms, GroupingProperty.Clustering, groupingKey);

        // tabbed pane where the local process models are shown
        this.setLayout(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane();
        this.add(tabbedPane, BorderLayout.CENTER);
        refreshTabbedPane(tabbedPane, 0, grouped.size(), grouped);
    }

    private void refreshTabbedPane(JTabbedPane tabbedPane, int start, int end, GroupedLPMResult result) {
        tabbedPane.removeAll();
        for (int index = start; index < end; index++) {
            String label = "Group " + (index + 1);
            tabbedPane.add(label, this.lpmSetDisplayComponentFactory
                    .createLPMSetDisplayComponent(
                            LPMSetDisplayComponentType.SimpleLPMsCollection,
                            result.getElement(index).getAllLPMs(),
                            new HashMap<>())
                    .getComponent());
        }
    }

    @Override
    public JComponent getComponent() {
        return this;
    }
}
