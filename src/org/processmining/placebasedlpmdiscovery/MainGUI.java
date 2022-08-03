package org.processmining.placebasedlpmdiscovery;

import org.deckfour.xes.model.impl.XLogImpl;
import org.processmining.contexts.uitopia.UIContext;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.framework.connections.ConnectionManager;
import org.processmining.framework.plugin.*;
import org.processmining.framework.providedobjects.ProvidedObjectManager;
import org.processmining.placebasedlpmdiscovery.plugins.mining.InteractiveLPMsDiscovery;
import org.processmining.placebasedlpmdiscovery.plugins.mining.PlaceBasedLPMDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.controllers.InteractiveLPMsDiscoveryController;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.controllers.InteractiveLPMsDiscoveryTryout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainGUI extends JFrame {

    public static void main(String[] args) {
        MainGUI gui = new MainGUI();
        gui.setVisible(true);
    }

    private JPanel contentPane;

    public MainGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        InteractiveLPMsDiscoveryTryout tryout = new InteractiveLPMsDiscoveryTryout();
        contentPane.add(tryout.getComponent());
        setContentPane(contentPane);
    }
}
