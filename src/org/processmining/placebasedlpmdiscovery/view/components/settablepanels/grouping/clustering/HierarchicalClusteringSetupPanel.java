package org.processmining.placebasedlpmdiscovery.view.components.settablepanels.grouping.clustering;

import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.ConfigurationComponent;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.configurations.SimpleMapViewConfiguration;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.configurations.ViewConfiguration;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class HierarchicalClusteringSetupPanel extends JComponent implements ConfigurationComponent {

    // components
    private JComboBox<String> linkageComboBox;
    private JRadioButton btnHeight;
    private JTextField txtHeight;
    private JRadioButton btnNumClusters;
    private JTextField txtNumClusters;

    public HierarchicalClusteringSetupPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // set num clusters or height
        addNumClustersPanel();

        // add empty space
        this.add(Box.createRigidArea(new Dimension(0, 5)));

        // add linkage panel
        addLinkagePanel();
    }

    private void addLinkagePanel() {
        JPanel linkagePanel = new JPanel();
        linkagePanel.setLayout(new BoxLayout(linkagePanel, BoxLayout.X_AXIS));
        linkagePanel.add(new JLabel("Linkage"));
        linkagePanel.add(Box.createRigidArea(new Dimension(5, 0)));
        this.linkageComboBox = new JComboBox<>(new String[]{
                "single", "complete", "wpgma", "upgma", "ward"});
        linkagePanel.add(linkageComboBox);
        this.add(linkagePanel);
    }

    private void addNumClustersPanel() {
        JPanel numClustersPanel = new JPanel();
        numClustersPanel.setLayout(new BoxLayout(numClustersPanel, BoxLayout.X_AXIS));

        // create buttons
        btnNumClusters = new JRadioButton("Num Clusters");
        btnHeight = new JRadioButton("Height");
        ButtonGroup btnGroup = new ButtonGroup();
        btnGroup.add(btnNumClusters);
        btnGroup.add(btnHeight);

        // create text fields
        txtNumClusters = new JTextField("30");
        txtNumClusters.setPreferredSize(new Dimension(50, txtNumClusters.getSize().height));
        txtHeight = new JTextField("0.3");
        txtHeight.setPreferredSize(new Dimension(50, txtHeight.getSize().height));

        // selection action
        btnNumClusters.addActionListener(e -> {
            txtHeight.setEnabled(false);
            txtNumClusters.setEnabled(true);
        });
        btnHeight.addActionListener(e -> {
            txtHeight.setEnabled(true);
            txtNumClusters.setEnabled(false);
        });

        // initial setup
        btnNumClusters.setSelected(true);
        txtHeight.setEnabled(false);

        // add everything to the panel
        numClustersPanel.add(btnNumClusters);
        numClustersPanel.add(txtNumClusters);
        numClustersPanel.add(btnHeight);
        numClustersPanel.add(txtHeight);

        this.add(numClustersPanel);
    }

    @Override
    public JComponent getComponent() {
        return this;
    }

    @Override
    public ViewConfiguration getConfiguration() {
        Map<String, String> map = new HashMap<>();
        map.put("linkage", (String) this.linkageComboBox.getSelectedItem());
        if (btnHeight.isSelected()) {
            map.put("height", this.txtHeight.getText());
        } else if (btnNumClusters.isSelected()) {
            map.put("num_clusters", this.txtNumClusters.getText());
        }

        return new SimpleMapViewConfiguration(map);
    }
}
