package org.processmining.placebasedlpmdiscovery.runners;

import org.deckfour.xes.model.XLog;
import org.processmining.models.connections.GraphLayoutConnection;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.impl.PetrinetFactory;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.placebasedlpmdiscovery.Main;
import org.processmining.placebasedlpmdiscovery.main.LPMDiscoveryBuilder;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.logs.XLogWrapper;
import org.processmining.placebasedlpmdiscovery.model.serializable.LPMResult;
import org.processmining.placebasedlpmdiscovery.model.serializable.PlaceSet;
import org.processmining.placebasedlpmdiscovery.prom.plugins.mining.PlaceBasedLPMDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.utils.LocalProcessModelUtils;
import org.processmining.placebasedlpmdiscovery.utils.LogUtils;
import org.processmining.placebasedlpmdiscovery.utils.PlaceUtils;
import org.processmining.plugins.pnml.base.FullPnmlElementFactory;
import org.processmining.plugins.pnml.base.Pnml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileInputStream;
import java.util.Set;

public class DefaultLogAndPetriNetRunner {

    public static void main(String[] args) throws Exception {
        if (args.length < 3) return;
        String eventLogPath = args[0] + ".xes";
        String placeNetsPath = args[1] + ".pnml";
        String resultPath = args[2] + ".zip";

        System.out.println(eventLogPath + "\n" + placeNetsPath + "\n" + resultPath);
        run(eventLogPath, placeNetsPath, resultPath);
    }

    private static void run(String eventLogPath, String petriNet, String resultPath) throws Exception {
        XLog log = LogUtils.readLogFromFile(eventLogPath);

        PlaceBasedLPMDiscoveryParameters parameters = new PlaceBasedLPMDiscoveryParameters(new XLogWrapper(log));
        parameters.setLpmCount(Integer.MAX_VALUE);
        parameters.getPlaceChooserParameters().setPlaceLimit(100);

        LPMDiscoveryBuilder builder = Main.createDefaultBuilder(
                log,
                new PlaceSet(PlaceUtils.extractPlaceNets(petriNet)),
                parameters);
        LocalProcessModelUtils.exportResult((LPMResult) builder.build().run(), resultPath);
    }


}
