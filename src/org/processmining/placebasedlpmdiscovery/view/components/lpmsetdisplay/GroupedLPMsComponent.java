package org.processmining.placebasedlpmdiscovery.view.components.lpmsetdisplay;

import org.processmining.placebasedlpmdiscovery.main.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.serializable.grouped.GroupedLPMResult;
import org.processmining.placebasedlpmdiscovery.model.serializable.grouped.GroupingProperty;

import javax.swing.*;
import java.awt.*;

public class GroupedLPMsComponent extends JComponent implements LPMSetDisplayComponent {

    public GroupedLPMsComponent(LPMDiscoveryResult result, String groupingKey) {

        GroupedLPMResult grouped = new GroupedLPMResult(result.getAllLPMs(), GroupingProperty.Clustering, groupingKey);

        // tabbed pane where the local process models are shown
        this.setLayout(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane();
        this.add(tabbedPane, BorderLayout.CENTER);
        refreshTabbedPane(tabbedPane, 0, grouped.size(), grouped);
    }

    private void refreshTabbedPane(JTabbedPane tabbedPane, int start, int end, GroupedLPMResult result) {
//        LPMResultVisualizer visualizer = new LPMResultVisualizer();
//        tabbedPane.removeAll();
//        for (int index = start; index < end; index++) {
//            String label = "Group " + (index + 1);
//            tabbedPane.add(label, visualizer.visualize(context, result.getElement(index)));
//        }
    }

    @Override
    public JComponent getComponent() {
        return this;
    }
}
