package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.settablepanels.lpmsimilarity;

import com.google.inject.Inject;
import org.processmining.placebasedlpmdiscovery.datacommunication.datalisteners.DataListener;
import org.processmining.placebasedlpmdiscovery.datacommunication.emittabledata.EmittableData;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.ComponentFactory;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.LPMDViewComponent;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.LPMDViewComponentType;
import org.processmining.placebasedlpmdiscovery.view.model.lpmdistances.MixedModelDistanceVM;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.UUID;

public class MixedLPMSimilaritySetupPanel extends JPanel implements DataListener {

    private final ComponentFactory componentFactory;
    private MixedModelDistanceVM model;

    @Inject
    public MixedLPMSimilaritySetupPanel(ComponentFactory componentFactory) {
        this.componentFactory = componentFactory;


        this.setLayout(new BorderLayout());

        // table where measures are shown
        DefaultTableModel measuresTableModel = new DefaultTableModel(new Object[]{"Measure", "Weight"}, 0);
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
        frame.setTitle("LPM Similarity Measure Setup");
        frame.setSize(new Dimension(500, 350));
        frame.setLayout(new BorderLayout(5, 5));

        // name and weight
        JPanel pnlNameAndWeight = new JPanel();
        pnlNameAndWeight.setLayout(new BorderLayout(5, 5));
        JPanel pnlName = new JPanel();
        pnlName.setLayout(new BoxLayout(pnlName, BoxLayout.X_AXIS));
        pnlName.add(new JLabel("Name:"));
        JTextField txtName = new JTextField(UUID.randomUUID().toString());
        pnlName.add(txtName);
        pnlNameAndWeight.add(pnlName, BorderLayout.LINE_START);
        JPanel pnlWeight = new JPanel();
        pnlWeight.setLayout(new BoxLayout(pnlWeight, BoxLayout.X_AXIS));
        pnlWeight.add(new JLabel("Weight:"));
        JTextField txtWeight = new JTextField("1");
        txtWeight.setPreferredSize(new Dimension(150, txtWeight.getPreferredSize().height));
        pnlWeight.add(txtWeight);
        pnlNameAndWeight.add(pnlWeight, BorderLayout.LINE_END);
        frame.add(pnlNameAndWeight, BorderLayout.PAGE_START);

        // similarity selection
        LPMDViewComponent component = this.componentFactory.create(LPMDViewComponentType.LPMSimilarityChooser);
        frame.add(component.getComponent(), BorderLayout.CENTER);

        // add button
        JButton btnAdd = new JButton("Add");
        btnAdd.addActionListener(e -> {
            model.addDistance(txtName.getText(), Double.parseDouble(txtWeight.getText()), component.getModel());
            revalidate();
        });
        frame.add(btnAdd, BorderLayout.PAGE_END);

        return frame;
    }

    @Override
    public void receive(EmittableData data) {

    }
}
