package org.processmining.placebasedlpmdiscovery.runners.lpmutils;

import nl.tue.astar.AStarException;
import org.deckfour.xes.model.XLog;
import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.lpms.discovery.DiscoveryParameters;
import org.processmining.lpms.occurrence.LPMOccurrenceList;
import org.processmining.lpms.occurrence.OccurrenceExtraction;
import org.processmining.lpms.quality.alignments.PNAlignments;
import org.processmining.placebasedlpmdiscovery.analysis.analyzers.loganalyzer.LEFRMatrix;
import org.processmining.placebasedlpmdiscovery.analysis.analyzers.loganalyzer.LogAnalyzer;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.LPMDiscovery;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.results.FromFileLPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.logs.EventLog;
import org.processmining.placebasedlpmdiscovery.model.logs.XLogWrapper;
import org.processmining.placebasedlpmdiscovery.model.logs.activities.Activity;
import org.processmining.placebasedlpmdiscovery.placechooser.MainPlaceChooser;
import org.processmining.placebasedlpmdiscovery.placechooser.PlaceChooser;
import org.processmining.placebasedlpmdiscovery.placechooser.PlaceChooserParameters;
import org.processmining.placebasedlpmdiscovery.prom.FromFilePlacesProvider;
import org.processmining.placebasedlpmdiscovery.prom.PlacesProvider;
import org.processmining.placebasedlpmdiscovery.utils.LocalProcessModelUtils;
import org.processmining.placebasedlpmdiscovery.utils.LogUtils;
import org.processmining.plugins.petrinet.replayresult.PNMatchInstancesRepResult;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LPMCoverageRunner {

    public static LPMOccurrenceList singleLPMtoLogAlignmentTax(LocalProcessModel lpm, EventLog log) throws AStarException {
        AcceptingPetriNet apn = LocalProcessModelUtils.getAcceptingPetriNetRepresentation(lpm);
        XLog xlog = log.getOriginalLog();

        PNMatchInstancesRepResult alignments = PNAlignments.tax().compute(apn, xlog);

        return OccurrenceExtraction.consider(PNMatchInstancesRepResult.class).from(alignments);
    }

    public static void main(String[] args) throws Exception {
        EventLog log = new XLogWrapper(LogUtils.readLogFromFile("data/logs/bpi2012_res10939.xes"));
        LPMDiscoveryResult savedLpms = new FromFileLPMDiscoveryResult("data/lpms/bpi2012_res10939.json");
        PlacesProvider placesProvider = new FromFilePlacesProvider("data/placenets/bpi2012_res10939.json");
        Set<Place> placeSet = placesProvider.provide();
        List<LocalProcessModel> placeLPMs = placeSet.stream().map(p -> {
            LocalProcessModel lpm = new LocalProcessModel();
            lpm.addPlace(p);
            return lpm;
        }).collect(Collectors.toList());
        LPMDiscoveryResult discoveredLpms = LPMDiscovery.placeBased(placesProvider).from(log.getOriginalLog());
        LogAnalyzer logAnalyzer = new LogAnalyzer(log.getOriginalLog());
        LEFRMatrix lefrMatrix = logAnalyzer.getLEFRMatrix(DiscoveryParameters.Default.proximity);
        PlaceChooser placeChooser = new MainPlaceChooser(log.getOriginalLog(),
                new PlaceChooserParameters(log.getActivities().stream().map(Activity::getName).collect(Collectors.toSet())), lefrMatrix);
        Set<Place> usedPlaceSet = placeChooser.choose(placeSet, DiscoveryParameters.PlaceBased.placeLimit);
        List<LocalProcessModel> usedPlaceLPMs = usedPlaceSet.stream().map(p -> {
            LocalProcessModel lpm = new LocalProcessModel();
            lpm.addPlace(p);
            return lpm;
        }).collect(Collectors.toList());

        LPMOccurrenceList completeOccurrence = LPMOccurrenceList.standard();
        savedLpms.getAllLPMs().forEach(lpm -> {
            try {
                LPMOccurrenceList occurrenceList = singleLPMtoLogAlignmentTax(lpm, log);
                occurrenceList.forEach(v -> completeOccurrence.push(v.getFirst(), v.getSecond()));
            } catch (AStarException e) {
                throw new RuntimeException(e);
            }
        });

//        LPMOccurrenceList placeOccurrence = LPMOccurrenceList.standard();
//        placeLPMs.forEach(lpm -> {
//            try {
//                LPMOccurrenceList occurrenceList = singleLPMtoLogAlignmentTax(lpm, log);
//                occurrenceList.forEach(v -> placeOccurrence.push(v.getFirst(), v.getSecond()));
//            } catch (AStarException e) {
//                throw new RuntimeException(e);
//            }
//        });

        LPMOccurrenceList completeOccurrenceDiscovered = LPMOccurrenceList.standard();
        discoveredLpms.getAllLPMs().forEach(lpm -> {
            try {
                LPMOccurrenceList occurrenceList = singleLPMtoLogAlignmentTax(lpm, log);
                occurrenceList.forEach(v -> completeOccurrenceDiscovered.push(v.getFirst(), v.getSecond()));
            } catch (AStarException e) {
                throw new RuntimeException(e);
            }
        });

        LPMOccurrenceList placeOccurrenceUsed = LPMOccurrenceList.standard();
        usedPlaceLPMs.forEach(lpm -> {
            try {
                LPMOccurrenceList occurrenceList = singleLPMtoLogAlignmentTax(lpm, log);
                occurrenceList.forEach(v -> placeOccurrenceUsed.push(v.getFirst(), v.getSecond()));
            } catch (AStarException e) {
                throw new RuntimeException(e);
            }
        });

        System.out.println("Covered Events Read LPMs:" + completeOccurrence.size());
        System.out.println("LPMs in Total: " + savedLpms.getAllLPMs().size());
//        System.out.println("Place Covered Events:" + placeOccurrence.size());
//        System.out.println("Place LPMs in Total: " + placeLPMs.size());
        System.out.println("Covered Events Discovered LPMs:" + completeOccurrenceDiscovered.size());
        System.out.println("Discovered LPMs in Total: " + discoveredLpms.getAllLPMs().size());
        System.out.println("Used Place Covered Events:" + placeOccurrenceUsed.size());
        System.out.println("Used Place LPMs in Total: " + usedPlaceLPMs.size());
        int totalEvents = log.getOriginalLog().stream().mapToInt(List::size).sum();
        System.out.println("Total Events: " + totalEvents);

    }
}
