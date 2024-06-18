package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.settablepanels.lpmsimilarity;

import com.google.inject.Inject;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.ComponentFactory;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.LPMDViewComponent;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.LPMDViewComponentType;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MixedLPMSimilaritySetupPanel extends JPanel {

    private final ComponentFactory componentFactory;

    @Inject
    public MixedLPMSimilaritySetupPanel(ComponentFactory componentFactory) {
        this. componentFactory = componentFactory;


        this.setLayout(new BorderLayout());

        // table where measures are shown
        DefaultTableModel measuresTableModel = new DefaultTableModel(new Object[] { "Measure", "Weight" }, 0);
        JTable measuresTable = new JTable(measuresTableModel);
        JScrollPane js = new JScrollPane(measuresTable,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        js.setPreferredSize(new Dimension(260, 150));
        this.add(js, BorderLayout.CENTER);

        // button palette for add, remove, and edit
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));
        JButton addBtn = new JButton("Add");
        addBtn.addActionListener(e -> {
            getAddDistMeasureInMixedFrame().setVisible(true);
        });
        btnPanel.add(addBtn);

        JButton editBtn = new JButton("Edit");
        btnPanel.add(editBtn);

        JButton removeBtn = new JButton("Remove");
        btnPanel.add(removeBtn);

        this.add(btnPanel, BorderLayout.PAGE_END);
    }

    private JFrame getAddDistMeasureInMixedFrame() {
        JFrame frame = new JFrame();
        frame.setSize(new Dimension(500, 350));

        LPMDViewComponent component = this.componentFactory.create(LPMDViewComponentType.LPMSimilarityChooser);
        frame.add(component.getComponent());

        return frame;
    }
}
