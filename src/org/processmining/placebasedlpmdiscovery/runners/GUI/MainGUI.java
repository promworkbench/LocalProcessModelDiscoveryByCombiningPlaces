package org.processmining.placebasedlpmdiscovery.runners.GUI;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.deckfour.xes.model.XLog;
import org.processmining.contexts.cli.CLIContext;
import org.processmining.contexts.cli.CLIPluginContext;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.placebasedlpmdiscovery.InputModule;
import org.processmining.placebasedlpmdiscovery.Main;
import org.processmining.placebasedlpmdiscovery.main.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.logs.XLogWrapper;
import org.processmining.placebasedlpmdiscovery.model.serializable.PlaceSet;
import org.processmining.placebasedlpmdiscovery.prom.dependencyinjection.PromGuiceModule;
import org.processmining.placebasedlpmdiscovery.prom.plugins.mining.PlaceBasedLPMDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.BaseLPMDiscoveryResultComponent;
import org.processmining.placebasedlpmdiscovery.service.dependencyinjection.ServiceGuiceModule;
import org.processmining.placebasedlpmdiscovery.service.lpmdiscovery.LPMDiscoveryService;
import org.processmining.placebasedlpmdiscovery.utils.LogUtils;
import org.processmining.placebasedlpmdiscovery.utils.PlaceUtils;
import org.processmining.placebasedlpmdiscovery.view.controllers.DefaultLPMDiscoveryResultViewController;
import org.processmining.placebasedlpmdiscovery.view.controllers.LPMDiscoveryResultViewController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Set;

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

    private BaseLPMDiscoveryResultComponent getDummyBaseView() {
        return new BaseLPMDiscoveryResultComponent(5);
    }

    private BaseLPMDiscoveryResultComponent getDummyDefaultView() throws Exception {
        Injector guice = Guice.createInjector(new PromGuiceModule(getDummyContext()), new InputModule(getDummyLog()),
                new ServiceGuiceModule()
//                new LPMDiscoveryResultGuiceModule(getDummyResult())
        );
//        LPMDiscoveryResultView view = guice.getInstance(LPMDiscoveryResultView.class);
//        new DefaultLPMDiscoveryResultComponent(getDummyContext(), new SettablePanelFactory());
//        LPMDiscoveryResultViewModel model = guice.getInstance(LPMDiscoveryResultViewModel.class);
//        new DefaultLPMDiscoveryResultViewModel(getDummyResult());

        LPMDiscoveryService lpmDiscoveryService = guice.getInstance(LPMDiscoveryService.class);
        LPMDiscoveryResultViewController controller = guice.getInstance(LPMDiscoveryResultViewController.class);
//                new DefaultLPMDiscoveryResultViewController(view, model);
        lpmDiscoveryService.runLPMDiscovery(getDummyLog(),
                new PlaceSet(PlaceUtils.extractPlaceNets("data/artificialBig.pnml")));

        controller.getView().display(controller.getModel());

        return ((DefaultLPMDiscoveryResultViewController) controller).getView();
    }

    private PluginContext getDummyContext() {
        return new CLIPluginContext(new CLIContext(), "");
    }

    private XLog getDummyLog() throws Exception {
        return LogUtils.readLogFromFile("data/artificialBig.xes");
    }

    private LPMDiscoveryResult getDummyResult() throws Exception {
        XLog log = getDummyLog();
        Set<Place> places = PlaceUtils.extractPlaceNets("data/artificialBig.pnml");
        return Main.createDefaultBuilder(log, new PlaceSet(places),
                new PlaceBasedLPMDiscoveryParameters(new XLogWrapper(log))).build().run();
    }
}
