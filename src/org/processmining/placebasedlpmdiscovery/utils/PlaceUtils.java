package org.processmining.placebasedlpmdiscovery.utils;


import com.google.common.collect.Sets;
import org.apache.commons.math3.util.Pair;
import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.acceptingpetrinet.models.impl.AcceptingPetriNetImpl;
import org.processmining.models.connections.GraphLayoutConnection;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.impl.PetrinetFactory;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.Transition;
import org.processmining.placebasedlpmdiscovery.model.additionalinfo.Passage;
import org.processmining.placebasedlpmdiscovery.model.exporting.importers.ImporterFactory;
import org.processmining.placebasedlpmdiscovery.model.exporting.importers.JsonImporter;
import org.processmining.placebasedlpmdiscovery.model.serializable.PlaceSet;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.converters.place.PetriNetPlaceConverter;
import org.processmining.plugins.pnml.base.FullPnmlElementFactory;
import org.processmining.plugins.pnml.base.Pnml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Utility functions that can be used over places
 */
public class PlaceUtils {

    public static class Mapper {

        public static <T> String getStringRepresentation(Set<T> inputTransitions,
                                                         Set<T> outputTransitions) {
            StringBuilder sb = new StringBuilder();
            sb.append('(');
            inputTransitions.forEach(tr -> sb.append(tr.toString()).append(", "));
            sb.delete(sb.length() - 2, sb.length());
            sb.append(" |");
            outputTransitions.forEach(tr -> sb.append(" ").append(tr.toString()).append(", "));
            sb.delete(sb.length() - 2, sb.length());
            sb.append(')');
            return sb.toString();
        }
    }

    public static void print(Set<Place> places) {
        StringBuilder sb = new StringBuilder();
        for (Place place : places) {
            sb.append(place.getShortString());
        }

        File file = new File("data/temporary/myplaces.txt");
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(sb.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Place revertPlace(Place place) {
        Place reverted = new Place();

        // all input transitions are output transitions in the reverted
        for (Transition inTr : place.getInputTransitions()) {
            reverted.addOutputTransition(inTr);
        }

        // all output transitions are input transitions in the reverted
        for (Transition outTr : place.getOutputTransitions()) {
            reverted.addInputTransition(outTr);
        }

        return reverted;
    }

    /**
     * Returns a map where every transition is mapped to all the places that have it as input
     * transition or all the places that have it as output transition depending on the input
     *
     * @param transitionLabels:  transition labels for which we want to create the map
     * @param places:            places for which we want to create the map
     * @param transitionIsInput: whether we want to map the places for input or output transitions
     * @param labelMap:          map between the original transition labels and what we want to return
     * @param <T>:               the type of the returning transition label
     * @return map of a transition label to set of places
     */
    public static <T> Map<T, Set<Place>> getTransitionPlaceSetMapping(Set<String> transitionLabels,
                                                                      Set<Place> places,
                                                                      boolean transitionIsInput,
                                                                      Map<String, T> labelMap,
                                                                      boolean ignoreSelfLoops) {
        Map<T, Set<Place>> res = new HashMap<>();
//        Collection<String> activities = LogUtils.getActivitiesFromLog(this.log);

        for (String transitionLabel : transitionLabels) {
            Set<Place> trPlaces = new HashSet<>();
            for (Place place : places) {
                if (ignoreSelfLoops && place.isSelfLoop(transitionLabel))
                    continue;

                if (transitionIsInput &&
                        place.isInputTransitionLabel(transitionLabel)) {
                    trPlaces.add(place);
                }
                if (!transitionIsInput && place.isOutputTransitionLabel(transitionLabel)) {
                    trPlaces.add(place);
                }
            }
            res.put(labelMap.get(transitionLabel), trPlaces);
        }

        return res;
    }

    public static Set<Transition> getAllTransitions(Set<Place> places) {
        Set<Transition> transitions = new HashSet<>();
        for (Place place : places) {
            transitions.addAll(place.getInputTransitions());
            transitions.addAll(place.getOutputTransitions());
        }
        return transitions;
    }

    public static PlaceSet getPlaceSetFromInputStream(InputStream input) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(input);
        Object object = ois.readObject();
        ois.close();
        if (object instanceof PlaceSet) {
            return (PlaceSet) object;
        } else {
            System.err.println("File could not be parsed as valid PlaceSet object");
        }
        return null;
    }

    /**
     * Returns a map where every pair of input output transitions in the places is mapped to all places that
     * have the same input output transition pair
     *
     * @param places   the places we want to group
     * @param labelMap the label mapping between the transitions in the places and the result we want to return
     * @param <T>      the new type of the transition labels
     * @return map from which we can get all places that have a specific input output transitions pair
     */
    public static <T> Map<Pair<T, T>, Set<Place>> getInoutTransitionPlaceSetMapping(Set<Place> places,
                                                                                    Map<String, T> labelMap) {
        Map<Pair<T, T>, Set<Place>> resMap = new HashMap<>();
        for (Place place : places) {
            for (Transition inTr : place.getInputTransitions()) {
                if (place.isSelfLoop(inTr.getLabel()))
                    continue;
                for (Transition outTr : place.getOutputTransitions()) {
                    Pair<T, T> pair = new Pair<>(labelMap.get(inTr.getLabel()), labelMap.get(outTr.getLabel()));
                    Set<Place> set = resMap.getOrDefault(pair, new HashSet<>());
                    set.add(place);
                    resMap.put(pair, set);
                }
            }
        }
        return resMap;
    }

    public static void postProcessSilentTransitions(Set<Place> places) {
        for (Place place : places) {
            keepOnlyOneSilentTransition(place);
        }
    }

    private static void keepOnlyOneSilentTransition(Place place) {
        boolean foundOne = false;
        for (Transition tr : place.getInputTransitions())
            if (!foundOne && tr.isInvisible())
                foundOne = true;
            else if (foundOne && tr.isInvisible())
                place.removeTransition(tr, true);

        foundOne = false;
        for (Transition tr : place.getOutputTransitions())
            if (!foundOne && tr.isInvisible())
                foundOne = true;
            else if (foundOne && tr.isInvisible())
                place.removeTransition(tr, false);
    }

    public static Map<Pair<Integer, Integer>, Set<List<Place>>> getInoutTransitionPlaceSetMappingViaSilent(
            Set<Place> places, Map<String, Integer> labelMap) {
        Map<Pair<Integer, Integer>, Set<List<Place>>> inoutTransitionPlaceMap = new HashMap<>();

        Set<Place> silentIn = places // get all places that have silent transition as input
                .stream()
                .filter(p -> p.getInputTransitions()
                        .stream()
                        .filter(Transition::isInvisible)
                        .collect(Collectors.toSet()).size() > 0)
                .collect(Collectors.toSet());
        Set<Place> silentOut = places // get all places that have silent transition as output
                .stream()
                .filter(p -> p.getOutputTransitions()
                        .stream()
                        .filter(Transition::isInvisible)
                        .collect(Collectors.toSet()).size() > 0)
                .collect(Collectors.toSet());

        for (Place first : silentOut) {
            for (Place second : silentIn) {
                // check if the two places have common silent transition
                if (Sets.intersection(first.getOutputTransitions(), second.getInputTransitions()
                        .stream().filter(Transition::isInvisible).collect(Collectors.toSet())).size() > 0) {
                    for (Transition trIn : first.getInputTransitions()) {
                        for (Transition trOut : second.getOutputTransitions()) {
                            if (trIn.getLabel().equals(trOut.getLabel()))
                                continue;
                            int mappedIn = labelMap.get(trIn.getLabel());
                            int mappedOut = labelMap.get(trOut.getLabel());
                            Pair<Integer, Integer> pair = new Pair<>(mappedIn, mappedOut);
                            Set<List<Place>> paths = inoutTransitionPlaceMap.getOrDefault(
                                    pair, new HashSet<>());
                            paths.add(Arrays.asList(first, second));
                            inoutTransitionPlaceMap.put(pair, paths);
                        }
                    }
                }
            }
        }
        return inoutTransitionPlaceMap;
    }

    public static Transition getCommonSilentTransition(Place first, Place second) {
        List<Transition> silent = Sets.intersection(first.getOutputTransitions(), second.getInputTransitions())
                .stream()
                .filter(Transition::isInvisible)
                .collect(Collectors.toList());
        return silent.size() > 0 ? silent.get(0) : null;
    }

    /**
     * Calculates the pairs of all combination of input and output transitions in a place.
     *
     * @param place: the place for which we want to calculate all combinations of input output transitions.
     * @return the pair of labels for all input output transition pairs
     */
    public static Set<Pair<String, String>> getVisibleInoutPairsInPlace(Place place) {
        Set<Pair<String, String>> resSet = new HashSet<>();
        for (Transition in : place.getInputTransitions()) {
            if (in.isInvisible())
                continue;
            for (Transition out : place.getOutputTransitions()) {
                if (out.isInvisible())
                    continue;
                resSet.add(new Pair<>(in.getLabel(), out.getLabel()));
            }
        }
        return resSet;
    }

    public static Set<Passage> getPassages(Place place) {
        Set<Passage> passages = new HashSet<>();
        for (Transition inTr : place.getInputTransitions()) {
            for (Transition outTr : place.getOutputTransitions()) {
                passages.add(new Passage(inTr.getLabel(), outTr.getLabel()));
            }
        }
        return passages;
    }

    public static Set<Place> getPlacesFromPetriNet(Petrinet petrinet) {
        AcceptingPetriNet acceptingPetriNet = new AcceptingPetriNetImpl(petrinet);
        PetriNetPlaceConverter converter = new PetriNetPlaceConverter();
        return converter.convert(acceptingPetriNet);
    }

    public static Set<Place> extractPlaceNets(String placeNetsInputFilePath) throws Exception {
        if (placeNetsInputFilePath.endsWith("pnml")) {
            Petrinet net = extractPetriNet(placeNetsInputFilePath);
            return PlaceUtils.getPlacesFromPetriNet(net);
        } else {
            JsonImporter<PlaceSet> importer = ImporterFactory.createPlaceSetJsonImporter();
            return importer.read(PlaceSet.class, Files.newInputStream(Paths.get(placeNetsInputFilePath))).getElements();
        }
    }

    public static Petrinet extractPetriNet(String petriNetFileName) throws XmlPullParserException, IOException {
        FullPnmlElementFactory pnmlFactory = new FullPnmlElementFactory();
        Petrinet net = PetrinetFactory.newPetrinet("place nets");

        FileInputStream input = new FileInputStream(petriNetFileName);

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(input, (String) null);
        int eventType = xpp.getEventType();
        Pnml pnml = new Pnml();
        synchronized (pnmlFactory) {
            Pnml.setFactory(pnmlFactory);

            while (eventType != 2) {
                eventType = xpp.next();
            }

            if (xpp.getName().equals("pnml")) {
                pnml.importElement(xpp, pnml);
            } else {
                pnml.log("pnml", xpp.getLineNumber(), "Expected pnml");
            }


            Marking marking = new Marking();
            GraphLayoutConnection layout = new GraphLayoutConnection(net);
            pnml.convertToNet(net, marking, layout);
        }
        return net;
    }

    /**
     * Computes the place matching cost such that the overlap between the input transition labels and output
     * transition labels between the two places is measured and then combined with an equal weight. Since this
     * would denote how similar the two places are, we subtract the result from one to get a cost.
     * @param p1 the first place
     * @param p2 the second place
     * @return the place matching cost
     */
    public static double computePlaceMatchingCost(Place p1, Place p2) {
        Set<String> inTr1 = p1.getInputTransitions().stream().map(Transition::getLabel).collect(Collectors.toSet());
        Set<String> inTr2 = p2.getInputTransitions().stream().map(Transition::getLabel).collect(Collectors.toSet());
        Set<String> outTr1 = p1.getOutputTransitions().stream().map(Transition::getLabel).collect(Collectors.toSet());
        Set<String> outTr2 = p2.getOutputTransitions().stream().map(Transition::getLabel).collect(Collectors.toSet());

        return 1 - (1.0 / 2 * (2.0 * Sets.intersection(inTr1, inTr2).size() / (inTr1.size() + inTr2.size())) +
                1.0 / 2 * (2.0 * Sets.intersection(outTr1, outTr2).size() / (outTr1.size() + outTr2.size())));
    }

    public static Map<Place, Integer> mapPlacesToIndices(Set<Place> places) {
        AtomicInteger counter = new AtomicInteger(0);
        return places
                .stream()
                .collect(Collectors.toMap(p -> p, p -> counter.getAndIncrement()));
    }
}
