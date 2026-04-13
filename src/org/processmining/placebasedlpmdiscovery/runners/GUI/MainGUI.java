package org.processmining.placebasedlpmdiscovery.runners.GUI;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.processmining.contexts.cli.CLIContext;
import org.processmining.contexts.cli.CLIPluginContext;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.placebasedlpmdiscovery.InputModule;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.dependencyinjection.LPMDiscoveryGuiceModule;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.dependencyinjection.LPMDiscoveryResultGuiceModule;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.service.LPMDiscoveryService;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.logs.EventLog;
import org.processmining.placebasedlpmdiscovery.model.logs.XLogWrapper;
import org.processmining.placebasedlpmdiscovery.model.serializable.PlaceSet;
import org.processmining.placebasedlpmdiscovery.prom.dependencyinjection.PromGuiceModule;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.BaseLPMDiscoveryResultComponent;
import org.processmining.placebasedlpmdiscovery.utils.LogUtils;
import org.processmining.placebasedlpmdiscovery.utils.PlaceUtils;
import org.processmining.placebasedlpmdiscovery.view.controllers.DefaultLPMDiscoveryResultViewController;
import org.processmining.placebasedlpmdiscovery.view.controllers.LPMDiscoveryResultViewController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainGUI extends JFrame {

    public static void main(String[] args) throws Exception {
        MainGUI gui = new MainGUI();
        gui.setVisible(true);
    }

    public MainGUI() throws Exception {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
//        contentPane.add(getDummyDiscovery().getComponentForContext());
        contentPane.add(getDummyDefaultView());
        setContentPane(contentPane);
    }


//    private InteractiveLPMsDiscovery getDummyDiscovery() throws Exception {
//        XLog log = getDummyLog();
//        return new InteractiveLPMsDiscovery(getDummyContext(), new PlaceBasedLPMDiscoveryParameters(new XLogWrapper
//        (log)), log);
//    }
//
//    private BaseLPMDiscoveryResultComponent getDummyBaseView() {
//        return new BaseLPMDiscoveryResultComponent(dcVM, 5);
//    }

    private BaseLPMDiscoveryResultComponent getDummyDefaultView() throws Exception {
        // discovery
        EventLog log = new XLogWrapper(LogUtils.readLogFromFile("data/logs/bpi2012_res10939.xes"));
        Injector guice = Guice.createInjector(new InputModule(log.getOriginalLog()), new LPMDiscoveryGuiceModule());
        LPMDiscoveryService lpmDiscoveryService = guice.getInstance(LPMDiscoveryService.class);
        LPMDiscoveryResult result = lpmDiscoveryService.runLPMDiscovery(log,
                new PlaceSet(PlaceUtils.extractPlaceNets("data/placenets/bpi2012_res10939.json")));

        // visualization
        guice = Guice.createInjector(new LPMDiscoveryResultGuiceModule(result), new PromGuiceModule(getDummyContext()));
        LPMDiscoveryResultViewController controller = guice.getInstance(LPMDiscoveryResultViewController.class);
        controller.getView().display(controller.getModel());

        return ((DefaultLPMDiscoveryResultViewController) controller).getView();
    }

    private PluginContext getDummyContext() {
        return new CLIPluginContext(new CLIContext(), "");
    }

    private EventLog getDummyLog() throws Exception {
        return new XLogWrapper(LogUtils.readLogFromFile("data/logs/artificialBig.xes"));
    }
}
