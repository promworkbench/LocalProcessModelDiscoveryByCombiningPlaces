package org.processmining.lpms.quality.alignments;

import nl.tue.astar.AStarException;
import org.deckfour.xes.model.XLog;
import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.lpms.occurrence.LPMOccurrenceList;
import org.processmining.lpms.occurrence.OccurrenceExtraction;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.results.FromFileLPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.logs.EventLog;
import org.processmining.placebasedlpmdiscovery.model.logs.XLogWrapper;
import org.processmining.placebasedlpmdiscovery.prom.FromFilePlacesProvider;
import org.processmining.placebasedlpmdiscovery.utils.LocalProcessModelUtils;
import org.processmining.placebasedlpmdiscovery.utils.LogUtils;
import org.processmining.plugins.petrinet.replayresult.PNMatchInstancesRepResult;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TestAlignment {

    public static LPMOccurrenceList singleLPMtoLogAlignmentTax(LocalProcessModel lpm, EventLog log) throws AStarException {
        AcceptingPetriNet apn = LocalProcessModelUtils.getAcceptingPetriNetRepresentation(lpm);
        XLog xlog = log.getOriginalLog();

        PNMatchInstancesRepResult alignments = PNAlignments.tax().compute(apn, xlog);

        return OccurrenceExtraction.consider(PNMatchInstancesRepResult.class).from(alignments);
    }

    public static void main(String[] args) throws Exception {
        EventLog log = new XLogWrapper(LogUtils.readLogFromFile("data/logs/bpi2012_res10939.xes"));
        LPMDiscoveryResult lpms = new FromFileLPMDiscoveryResult("data/lpms/bpi2012_res10939.json");
        Set<Place> placeSet = new FromFilePlacesProvider("data/placenets/bpi2012_res10939.json").provide();
        List<LocalProcessModel> placeLPMs = placeSet.stream().map(p -> {
            LocalProcessModel lpm = new LocalProcessModel();
            lpm.addPlace(p);
            return lpm;
        }).collect(Collectors.toList());

        LPMOccurrenceList completeOccurrence = LPMOccurrenceList.standard();

        lpms.getAllLPMs().forEach(lpm -> {
            try {
                LPMOccurrenceList occurrenceList = singleLPMtoLogAlignmentTax(lpm, log);
                occurrenceList.forEach(v -> completeOccurrence.push(v.getFirst(), v.getSecond()));
            } catch (AStarException e) {
                throw new RuntimeException(e);
            }
        });

        LPMOccurrenceList placeOccurrence = LPMOccurrenceList.standard();
        placeLPMs.forEach(lpm -> {
            try {
                LPMOccurrenceList occurrenceList = singleLPMtoLogAlignmentTax(lpm, log);
                occurrenceList.forEach(v -> placeOccurrence.push(v.getFirst(), v.getSecond()));
            } catch (AStarException e) {
                throw new RuntimeException(e);
            }
        });

        int coveredEvents = completeOccurrence.size();
        System.out.println("Covered Events:" + coveredEvents);
        System.out.println("LPMs in Total: " + lpms.getAllLPMs().size());
        System.out.println("Place Covered Events:" + placeOccurrence.size());
        System.out.println("Place LPMs in Total: " + placeLPMs.size());
        int totalEvents = log.getOriginalLog().stream().mapToInt(List::size).sum();
        System.out.println("Total Events: " + totalEvents);

    }
}
