package org.processmining.placebasedlpmdiscovery;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.cli.CLIContext;
import org.processmining.contexts.cli.CLIPluginContext;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.placebasedlpmdiscovery.plugins.mining.InteractiveLPMsDiscovery;
import org.processmining.placebasedlpmdiscovery.plugins.mining.PlaceBasedLPMDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.utils.LogUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainGUI extends JFrame {

    public static void main(String[] args) throws Exception {
        MainGUI gui = new MainGUI();
        gui.setVisible(true);
    }

    private JPanel contentPane;

    public MainGUI() throws Exception {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        contentPane.add(getDummyDiscovery().getComponentForContext(null));
        setContentPane(contentPane);
    }

    private InteractiveLPMsDiscovery getDummyDiscovery() throws Exception {
        XLog log = LogUtils.readLogFromFile("data/sequence_3.xes");
        PluginContext context = new CLIPluginContext(new CLIContext(), "");
        return new InteractiveLPMsDiscovery(context, new PlaceBasedLPMDiscoveryParameters(log), log);
    }
}
