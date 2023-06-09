package org.processmining.placebasedlpmdiscovery.lpmevaluation.undecided;

import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.framework.connections.ConnectionCannotBeObtained;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.models.connections.GraphLayoutConnection;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.serializable.PlaceSet;
import org.processmining.plugins.pnml.base.FullPnmlElementFactory;
import org.processmining.plugins.pnml.base.Pnml;
import org.processmining.plugins.pnml.base.PnmlElementFactory;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class Utils {

//    /**
//     * Persist into files the result from eST Miner for each of the logs in the input directory
//     *
//     * @param inDirPath:  path to the directory where the log files are
//     * @param outDirPath: path to the directory where the results from the eST miner should be saved
//     */
//    public static void persistInputDataUsingESTMiner(String inDirPath, String outDirPath) throws Exception {
//        File inDir = new File(inDirPath);
//        for (File xesFile : Objects.requireNonNull(inDir.listFiles())) {
//            if (!xesFile.getName().endsWith(".xes"))
//                continue;
//            XLog log = LogUtils.readLogFromFile(xesFile.getPath());
//            PlaceDiscoveryResult result = PlaceDiscovery.discover(log, new EstMinerPlaceDiscoveryParameters());
//            PlaceSet set = new PlaceSet(result.getPlaces());
//
//            File outFile = new File(outDirPath + "/" +
//                    xesFile.getName().substring(0, xesFile.getName().lastIndexOf('.')) + ".promspl");
//            try (FileOutputStream fos = new FileOutputStream(outFile);
//                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
//                oos.writeObject(set);
//            }
//
//            outFile = new File(outDirPath + "/" +
//                    xesFile.getName().substring(0, xesFile.getName().lastIndexOf('.')) + "-modified.xes");
//            try (FileOutputStream fos = new FileOutputStream(outFile);
//                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
//                oos.writeObject(log);
//            }
//        }
//    }

    private static void writePlaceSet(Set<Place> placeSet, File outFile) {
        try (FileOutputStream fos = new FileOutputStream(outFile);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeInt(placeSet.size());
            for (Place place : placeSet) {
                oos.writeObject(place);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Set<Place> readPlacesFromFile(String filePath) throws ClassNotFoundException {
        Set<Place> resSet = new HashSet<>();
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             ObjectInputStream ois = new ObjectInputStream(fis)) {

//            int size = ois.readInt();
//            while (size > 0) {
//                Place place = (Place) ois.readObject();
//                resSet.add(place);
//                size--;
//            }
            PlaceSet placeSet = (PlaceSet) ois.readObject();
            resSet = placeSet.getElements();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return resSet;
    }

    public static ByteArrayOutputStream exportAcceptingPetriNetToOutputStream(AcceptingPetriNet apn) throws IOException {
        return exportAcceptingPetriNetToOutputStream(apn, new GraphLayoutConnection(apn.getNet()));
    }

    public static ByteArrayOutputStream exportAcceptingPetriNetToOutputStream(AcceptingPetriNet apn, GraphLayoutConnection layout) throws IOException {
        PnmlElementFactory factory = new FullPnmlElementFactory();
        Pnml pnml = new Pnml();
        synchronized(factory) {
            Pnml.setFactory(factory);
            pnml = (new Pnml()).convertFromNet(apn.getNet(), apn.getInitialMarking(), apn.getFinalMarkings(), layout);
            pnml.setType(Pnml.PnmlType.PNML);
        }

        String text = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" + pnml.exportElement(pnml);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(text.getBytes());
        return os;
    }
    public static ByteArrayOutputStream exportAcceptingPetriNetToOutputStream(PluginContext context, AcceptingPetriNet apn) throws IOException {
        GraphLayoutConnection layout;
        try {
            layout = (GraphLayoutConnection) context.getConnectionManager().getFirstConnection(GraphLayoutConnection.class, context, new Object[]{apn.getNet()});
        } catch (ConnectionCannotBeObtained var9) {
            layout = new GraphLayoutConnection(apn.getNet());
        }

        return exportAcceptingPetriNetToOutputStream(apn, layout);
    }
}
