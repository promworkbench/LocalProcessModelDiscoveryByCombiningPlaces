package org.processmining.placebasedlpmdiscovery.view.components.lpmsetdisplay;

import org.processmining.placebasedlpmdiscovery.main.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.serializable.grouped.GroupedLPMResult;
import org.processmining.placebasedlpmdiscovery.model.serializable.grouped.GroupingProperty;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.visualizers.LPMResultVisualizer;
import org.processmining.placebasedlpmdiscovery.utils.LocalProcessModelUtils;
import org.processmining.placebasedlpmdiscovery.view.components.ComponentFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

public class GroupedLPMsComponent extends JComponent implements LPMSetDisplayComponent {

    private final LPMSetDisplayComponentFactory lpmSetDisplayComponentFactory;

    // content
    private final Collection<LocalProcessModel> lpms;

    // components
    private final JTabbedPane groupsTabbedPane;

    public GroupedLPMsComponent(Collection<LocalProcessModel> lpms,
                                String groupingKey,
                                LPMSetDisplayComponentFactory lpmSetDisplayComponentFactory) {
        this.lpms = lpms;
        this.lpmSetDisplayComponentFactory = lpmSetDisplayComponentFactory;


        this.setLayout(new BorderLayout(0, 10));

        // header with parameters
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.add(new JLabel("Identifier:"));
        header.add(Box.createRigidArea(new Dimension(20, 0)));
        JComboBox<String> idComboBox =
                new JComboBox<>(LocalProcessModelUtils.extractGroupIds(lpms).toArray(new String[]{}));
        idComboBox.setSelectedItem(groupingKey);
        idComboBox.addActionListener(e -> {
            this.setContent(Objects.requireNonNull(idComboBox.getSelectedItem()).toString());
        });
        header.add(idComboBox);
        this.add(header, BorderLayout.PAGE_START);

        // tabbed pane where the local process model groups are shown
        this.groupsTabbedPane = new JTabbedPane();
        this.add(this.groupsTabbedPane, BorderLayout.CENTER);

        this.setContent(groupingKey);
    }

    private void setContent(String groupingKey) {
        GroupedLPMResult grouped = new GroupedLPMResult(lpms, GroupingProperty.Clustering, groupingKey);
        refreshTabbedPane(this.groupsTabbedPane, 0, grouped.size(), grouped);
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
