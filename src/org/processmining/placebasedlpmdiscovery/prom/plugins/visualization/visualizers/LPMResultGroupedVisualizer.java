package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.visualizers;

import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.placebasedlpmdiscovery.grouping.GroupingController;
import org.processmining.placebasedlpmdiscovery.main.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.serializable.grouped.GroupedLPMResult;
import org.processmining.placebasedlpmdiscovery.model.serializable.grouped.GroupingProperty;

import javax.swing.*;
import java.util.HashMap;


@Plugin(name = "@1 Visualize LPM Result in Groups",
        returnLabels = {"Visualized LPM Result"},
        returnTypes = {JComponent.class},
        parameterLabels = {"Petri Net Array"})
@Visualizer
public class LPMResultGroupedVisualizer {

    @PluginVariant(requiredParameterLabels = {0})
    public JComponent visualize(UIPluginContext context, LPMDiscoveryResult result) {
//        JComponent component = new JPanel();
//        component.setLayout(new GridBagLayout());
//        GridBagConstraints gbc = new GridBagConstraints();
//
//        GroupingProperty property = showChooseGroupingPropertyDialog(new JFrame());
//
//        // TODO: See whether you don't have to do this if it has already be done
//        GroupedLPMResult grouped = new GroupedLPMResult(result, property);
//
////        // sort the groups by how many windows they cover
////        grouped.sort((LPMResultGroup gr1, LPMResultGroup gr2) -> gr1.getWindowCount() < gr2.getWindowCount());
//
//        // component for choosing
//        JComponent choosingComponent = new JPanel();
//        choosingComponent.setLayout(new BoxLayout(choosingComponent, BoxLayout.X_AXIS));
//
//        // error label
//        JLabel errorLabel = new JLabel();
//        errorLabel.setText("There are " + grouped.size() + " groups in total.");
//
//        // tabbed pane where the local process models are shown
//        JTabbedPane tabbedPane = new JTabbedPane();
//
//        // start index
//        choosingComponent.add(new JLabel("Start index:"));
//        JTextField txtStart = new JTextField("1");
//        PlainDocument txtStartDocument = (PlainDocument) txtStart.getDocument();
//        txtStartDocument.setDocumentFilter(new TextFieldIntFilter(errorLabel::setText, "1", 1, grouped.size()));
//        choosingComponent.add(txtStart);
//
//        // end index
//        choosingComponent.add(new JLabel("End index:"));
//        JTextField txtEnd = new JTextField("10");
//        PlainDocument txtEndDocument = (PlainDocument) txtEnd.getDocument();
//        txtEndDocument.setDocumentFilter(new TextFieldIntFilter(errorLabel::setText, "10", 1, grouped.size()));
//        choosingComponent.add(txtEnd);
//
//        // button
//        JButton btn = new JButton("Show");
//        btn.addActionListener(e -> {
//            int start = Integer.parseInt(txtStart.getText());
//            int end = Integer.parseInt(txtEnd.getText());
//            if (start > end)
//                errorLabel.setText("Start index is larger than end index");
//            else
//                refreshTabbedPane(tabbedPane, context, start, end, grouped);
//        });
//        choosingComponent.add(btn);
//
//        gbc.gridx = gbc.gridy = 0;
//        gbc.gridwidth = gbc.gridheight = 1;
//        gbc.fill = GridBagConstraints.BOTH;
//        gbc.anchor = GridBagConstraints.NORTHWEST;
//
//        gbc.weightx = 1;
//        gbc.weighty = 0.1;
//        gbc.insets = new Insets(2, 2, 2, 2);
//        component.add(choosingComponent, gbc);
//
//        gbc.gridy = 1;
//        gbc.weightx = 1;
//        gbc.weighty = 0.1;
//        gbc.insets = new Insets(2, 2, 2, 2);
//        component.add(errorLabel, gbc);
//
//        gbc.gridy = 2;
//        gbc.weightx = 1;
//        gbc.weighty = 0.7;
//        gbc.insets = new Insets(2, 2, 2, 2);
//        refreshTabbedPane(tabbedPane, context, 1, Math.min(10, grouped.size()), grouped);
//        component.add(tabbedPane, gbc);

//        return component;

        GroupingProperty property = showChooseGroupingPropertyDialog(new JFrame());
        // TODO: See whether you don't have to do this if it has already be done
        if (property.equals(GroupingProperty.Clustering)) {
            GroupingController groupingController = new GroupingController();
            groupingController.groupLPMs(result.getAllLPMs(), new HashMap<>());
        }
        GroupedLPMResult grouped = new GroupedLPMResult(result.getAllLPMs(), property);

        // tabbed pane where the local process models are shown
        JTabbedPane tabbedPane = new JTabbedPane();
        refreshTabbedPane(tabbedPane, context, 0, grouped.size(), grouped);

        return tabbedPane;
    }

    private GroupingProperty showChooseGroupingPropertyDialog(JFrame frame) {
        return (GroupingProperty) JOptionPane
                .showInputDialog(frame,
                        "Choose Grouping Property:",
                        "Grouping Property",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        GroupingProperty.values(),
                        GroupingProperty.Clustering);
    }

    private void refreshTabbedPane(JTabbedPane tabbedPane, UIPluginContext context, int start, int end, GroupedLPMResult result) {
        LPMResultVisualizer visualizer = new LPMResultVisualizer();
        tabbedPane.removeAll();
        for (int index = start; index < end; index++) {
            String label = "Group " + (index + 1);
            tabbedPane.add(label, visualizer.visualize(context, result.getElement(index)));
        }
    }
}
