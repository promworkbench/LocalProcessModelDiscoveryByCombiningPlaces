package org.processmining.placebasedlpmdiscovery.view.components.settablepanels.grouping.lpmsimilarity;

import com.google.inject.Inject;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.ConfigurationComponent;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.ConfigurationComponentFactory;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.configurations.StandardConfigurationComponentType;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.configurations.ViewConfiguration;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.configurations.lpmsimilarity.LPMSimilarityViewConfiguration;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.configurations.lpmsimilarity.MixedLPMSimilarityViewConfiguration;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.DataCommunicationControllerVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.datalisteners.DataListenerVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.EmittableDataTypeVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.EmittableDataVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.lpmdistances.MixedModelDistanceAddDistanceEmittableDataVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.lpmdistances.MixedModelDistanceRemoveDistanceEmittableDataVM;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.UUID;

public class MixedLPMSimilaritySetupPanel extends JPanel implements DataListenerVM, LPMSimilaritySetupComponent {

    // services
    private final DataCommunicationControllerVM dc;
    private final ConfigurationComponentFactory componentFactory;
    //    private final MixedModelDistanceVM model;
    private MixedLPMSimilarityViewConfiguration configuration;

    // components
    private final DefaultTableModel measuresTableModel;
    private final JTable measuresTable;
    private JFrame addModelDistanceFrame;

    @Inject
    public MixedLPMSimilaritySetupPanel(DataCommunicationControllerVM dc,
                                        ConfigurationComponentFactory componentFactory) {
        this.dc = dc;
        this.componentFactory = componentFactory;

        this.dc.registerDataListener(this, EmittableDataTypeVM.MixedModelDistanceAddDistanceVM);
        this.dc.registerDataListener(this, EmittableDataTypeVM.MixedModelDistanceRemoveDistanceVM);

//        this.model = new MixedModelDistanceVM();
        this.configuration = new MixedLPMSimilarityViewConfiguration();

        this.setLayout(new BorderLayout());

        // table where measures are shown
        this.measuresTableModel = new DefaultTableModel(new Object[]{"Measure", "Weight"}, 0);
        this.measuresTable = new JTable(measuresTableModel);
        JScrollPane js = new JScrollPane(measuresTable,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        js.setPreferredSize(new Dimension(260, 150));
        this.add(js, BorderLayout.CENTER);

        // button palette for add and remove
        JPanel btnPanel = getButtonPanel();
        this.add(btnPanel, BorderLayout.PAGE_END);
    }

    private JPanel getButtonPanel() {
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));

        JButton addBtn = new JButton("Add");
        addBtn.addActionListener(e -> {
            initAddDistMeasureInMixedFrame();
            this.addModelDistanceFrame.setVisible(true);
        });
        btnPanel.add(addBtn);

        JButton removeBtn = new JButton("Remove");
        removeBtn.addActionListener(e -> dc.emit(new MixedModelDistanceRemoveDistanceEmittableDataVM(
                (String) measuresTableModel.getValueAt(measuresTable.getSelectedRow(), 0),
                measuresTable.getSelectedRow())));
        btnPanel.add(removeBtn);
        return btnPanel;
    }

    private void initAddDistMeasureInMixedFrame() {
        this.addModelDistanceFrame = new JFrame();
        addModelDistanceFrame.setTitle("LPM Similarity Measure Setup");
        addModelDistanceFrame.setSize(new Dimension(500, 350));
        addModelDistanceFrame.setLayout(new BorderLayout(5, 5));

        // name and weight
        JPanel pnlNameAndWeight = new JPanel();
        pnlNameAndWeight.setLayout(new BorderLayout(5, 5));
        addModelDistanceFrame.add(pnlNameAndWeight, BorderLayout.PAGE_START);

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

        // similarity selection
        ConfigurationComponent component =
                this.componentFactory.create(StandardConfigurationComponentType.LPMSimilarityConfigurationComponent);
        addModelDistanceFrame.add(component.getComponent(), BorderLayout.CENTER);

        // add button
        JButton btnAdd = new JButton("Add");
        btnAdd.addActionListener(e -> {
            dc.emit(new MixedModelDistanceAddDistanceEmittableDataVM(
                    txtName.getText(), Double.parseDouble(txtWeight.getText()), component.getConfiguration().getMap()));
        });
        addModelDistanceFrame.add(btnAdd, BorderLayout.PAGE_END);
    }

    @Override
    public void receive(EmittableDataVM data) {
        if (data.getType().equals(EmittableDataTypeVM.MixedModelDistanceAddDistanceVM)) { // add distance
            MixedModelDistanceAddDistanceEmittableDataVM cData = (MixedModelDistanceAddDistanceEmittableDataVM) data;
//            this.model.addDistance(cData.getName(), cData.getWeight(), cData.getModelDistanceConfig());
            this.configuration.addDistance(cData.getName(), cData.getWeight(),
                    new LPMSimilarityViewConfiguration(cData.getModelDistanceConfig()));
            this.measuresTableModel.addRow(new Object[]{cData.getName(), cData.getWeight()});
            this.addModelDistanceFrame.dispatchEvent(
                    new WindowEvent(this.addModelDistanceFrame, WindowEvent.WINDOW_CLOSING));
        } else if (data.getType().equals(EmittableDataTypeVM.MixedModelDistanceRemoveDistanceVM)) { // remove distance
            MixedModelDistanceRemoveDistanceEmittableDataVM cData =
                    (MixedModelDistanceRemoveDistanceEmittableDataVM) data;
//            this.model.removeDistance(cData.getKey());
            this.configuration.removeDistance(cData.getKey());
            this.measuresTableModel.removeRow(cData.getIndex());
        }
    }

    @Override
    public JComponent getComponent() {
        return this;
    }

    @Override
    public ViewConfiguration getConfiguration() {
        return configuration;
    }
}
