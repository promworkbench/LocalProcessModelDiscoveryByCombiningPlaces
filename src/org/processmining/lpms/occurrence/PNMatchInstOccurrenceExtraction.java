package org.processmining.lpms.occurrence;

import org.processmining.plugins.petrinet.replayresult.PNMatchInstancesRepResult;
import org.processmining.plugins.petrinet.replayresult.StepTypes;
import org.processmining.plugins.replayer.replayresult.AllSyncReplayResult;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PNMatchInstOccurrenceExtraction implements OccurrenceExtraction<PNMatchInstancesRepResult> {
    @Override
    public LPMOccurrenceList from(PNMatchInstancesRepResult result) {
        LPMOccurrenceList lpmOccurrenceList = LPMOccurrenceList.standard();

        for (AllSyncReplayResult traceResult : result) {
            List<StepTypes> steps = traceResult.getStepTypesLst().get(0);

            List<Integer> events = IntStream.range(0, steps.size()).
                    filter(i -> steps.get(i).equals(StepTypes.LMGOOD)).boxed()
                    .collect(Collectors.toList());

            for (int traceIndex : traceResult.getTraceIndex()) {
                events.forEach(e -> lpmOccurrenceList.push(traceIndex, e));
            }

        }

        return lpmOccurrenceList;
    }
}
