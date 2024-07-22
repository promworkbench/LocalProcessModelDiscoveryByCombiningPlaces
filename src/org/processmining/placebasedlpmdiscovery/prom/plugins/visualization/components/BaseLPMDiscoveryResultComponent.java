package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.math3.util.Pair;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.settablepanels.SettablePanelContainer;
import org.processmining.placebasedlpmdiscovery.view.components.lpmsetdisplay.LPMSetDisplayComponentType;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.DataCommunicationControllerVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.componentchange.LPMSetDisplayComponentChangeEmittableDataVM;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BaseLPMDiscoveryResultComponent extends JComponent {

    // services
    protected final DataCommunicationControllerVM dcVM;
    protected JPanel lpmSetDisplayContainer;
    protected final Map<Pair<Integer, Integer>, SettablePanelContainer> settablePanels;

    public BaseLPMDiscoveryResultComponent(DataCommunicationControllerVM dcVM, int countSettablePanels) {
        this.dcVM = dcVM;

        this.setLayout(new GridBagLayout());
        this.settablePanels = new HashMap<>();

        init(countSettablePanels);
    }

    private void init(int countSettablePanels) {
        JPanel pnlLpmSetContainer = new JPanel();
        pnlLpmSetContainer.setLayout(new BorderLayout());
        pnlLpmSetContainer.setPreferredSize(new Dimension(50, 50));

        // lpm set component chooser
        JPanel pnlLpmSetChooser = new JPanel();
        pnlLpmSetChooser.setLayout(new BoxLayout(pnlLpmSetChooser, BoxLayout.X_AXIS));
        pnlLpmSetChooser.add(new JLabel("LPM Set Visualizer:"));
        pnlLpmSetChooser.add(Box.createRigidArea(new Dimension(5, 0)));
        JComboBox<LPMSetDisplayComponentType> cbxLpmSetDisplayComponent =
                new JComboBox<>(LPMSetDisplayComponentType.values());
        cbxLpmSetDisplayComponent.setSelectedItem(LPMSetDisplayComponentType.SimpleLPMsCollection);
        cbxLpmSetDisplayComponent.addActionListener(e -> {
            this.dcVM.emit(new LPMSetDisplayComponentChangeEmittableDataVM(
                    (LPMSetDisplayComponentType) cbxLpmSetDisplayComponent.getSelectedItem(),
                    Collections.singletonMap("identifier", "default")));
        });
        pnlLpmSetChooser.add(cbxLpmSetDisplayComponent);
        pnlLpmSetContainer.add(pnlLpmSetChooser, BorderLayout.PAGE_START);

        // setting up lpmSetDisplay panel
        this.lpmSetDisplayContainer = new JPanel();
        this.lpmSetDisplayContainer.setPreferredSize(new Dimension(50, 50));
        this.lpmSetDisplayContainer.setLayout(new BorderLayout());
        pnlLpmSetContainer.add(this.lpmSetDisplayContainer, BorderLayout.CENTER);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
//        c.weightx = 0.5;
//        c.weighty = 0.5;
        c.insets = new Insets(5, 5, 5, 5);
        if (countSettablePanels < 4) {
            c.weightx = 0.66;
            c.weighty = 1;
            c.gridwidth = 2;
            c.gridheight = 3;
        } else if (countSettablePanels < 6) {
            c.weightx = 0.33;
            c.weighty = 0.66;
            c.gridwidth = 2;
            c.gridheight = 2;
        } else {
            throw new NotImplementedException("You can not have more than 5 panels.");
        }
        this.add(pnlLpmSetContainer, c);

        // setting up settable panels containers
        int countSettableContainers = countSettablePanels < 4 ? 3 : 5;
        c.weightx = 0.33;
        c.weighty = 0.33;
        c.gridwidth = 1;
        c.gridheight = 1;
        for (int i = 0; i < countSettableContainers; ++i) {
            SettablePanelContainer container = new SettablePanelContainer();
            if (i < 3) {
                c.gridx = 2;
                c.gridy = i;
            } else {
                c.gridx = i % 3;
                c.gridy = 2;
            }
            container.setPreferredSize(new Dimension(100,50));
            this.settablePanels.put(new Pair<>(c.gridx, c.gridy), container);
            container.add(new JLabel(c.gridx + "," + c.gridy));
            this.add(container, c);
        }
    }
}
