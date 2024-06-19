package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.settablepanels.lpmsimilarity;

import com.google.inject.Inject;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.ComponentFactory;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.LPMDViewComponent;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.LPMDViewComponentType;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.DataCommunicationControllerVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.datalisteners.DataListenerVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.EmittableDataTypeVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.EmittableDataVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.lpmdistances.MixedModelDistanceAddDistanceEmittableDataVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.lpmdistances.MixedModelDistanceRemoveDistanceEmittableDataVM;
import org.processmining.placebasedlpmdiscovery.view.model.lpmdistances.MixedModelDistanceVM;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.UUID;

public class MixedLPMSimilaritySetupPanel extends JPanel implements DataListenerVM {

    private final DataCommunicationControllerVM dc;
    private final ComponentFactory componentFactory;
    private final MixedModelDistanceVM model;

    private final DefaultTableModel measuresTableModel;
    private final JTable measuresTable;
    private JFrame addModelDistanceFrame;

    @Inject
    public MixedLPMSimilaritySetupPanel(DataCommunicationControllerVM dc, ComponentFactory componentFactory) {
        this.dc = dc;
        this.componentFactory = componentFactory;

        this.dc.registerDataListener(this, EmittableDataTypeVM.MixedModelDistanceAddDistanceVM);
        this.dc.registerDataListener(this, EmittableDataTypeVM.MixedModelDistanceRemoveDistanceVM);

        this.model = new MixedModelDistanceVM();

        this.setLayout(new BorderLayout());

        // table where measures are shown
        this.measuresTableModel = new DefaultTableModel(new Object[]{"Measure", "Weight"}, 0);
        this.measuresTable = new JTable(measuresTableModel);
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
            initAddDistMeasureInMixedFrame();
            this.addModelDistanceFrame.setVisible(true);
        });
        btnPanel.add(addBtn);

        JButton removeBtn = new JButton("Remove");
        removeBtn.addActionListener(e -> {
            dc.emit(new MixedModelDistanceRemoveDistanceEmittableDataVM(
                    (String) measuresTableModel.getValueAt(measuresTable.getSelectedRow(), 0),
                    measuresTable.getSelectedRow()));
        });
        btnPanel.add(removeBtn);

        this.add(btnPanel, BorderLayout.PAGE_END);
    }

    private void initAddDistMeasureInMixedFrame() {
        this.addModelDistanceFrame = new JFrame();
        addModelDistanceFrame.setTitle("LPM Similarity Measure Setup");
        addModelDistanceFrame.setSize(new Dimension(500, 350));
        addModelDistanceFrame.setLayout(new BorderLayout(5, 5));

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
        addModelDistanceFrame.add(pnlNameAndWeight, BorderLayout.PAGE_START);

        // similarity selection
        LPMDViewComponent component = this.componentFactory.create(LPMDViewComponentType.LPMSimilarityChooser);
        addModelDistanceFrame.add(component.getComponent(), BorderLayout.CENTER);

        // add button
        JButton btnAdd = new JButton("Add");
        btnAdd.addActionListener(e -> {
            dc.emit(new MixedModelDistanceAddDistanceEmittableDataVM(
                    txtName.getText(), Double.parseDouble(txtWeight.getText()), component.getModel()));
        });
        addModelDistanceFrame.add(btnAdd, BorderLayout.PAGE_END);
    }

    @Override
    public void receive(EmittableDataVM data) {
        if (data.getType().equals(EmittableDataTypeVM.MixedModelDistanceAddDistanceVM)) {
            MixedModelDistanceAddDistanceEmittableDataVM cData = (MixedModelDistanceAddDistanceEmittableDataVM) data;
            this.model.addDistance(cData.getName(), cData.getWeight(), cData.getModel());
            this.measuresTableModel.addRow(new Object[]{cData.getName(), cData.getWeight()});
            this.addModelDistanceFrame.dispatchEvent(
                    new WindowEvent(this.addModelDistanceFrame, WindowEvent.WINDOW_CLOSING));
        } else if (data.getType().equals(EmittableDataTypeVM.MixedModelDistanceRemoveDistanceVM)) {
            MixedModelDistanceRemoveDistanceEmittableDataVM cData = (MixedModelDistanceRemoveDistanceEmittableDataVM) data;
            this.model.removeDistance(cData.getKey());
            this.measuresTableModel.removeRow(cData.getIndex());
        }
    }
}
