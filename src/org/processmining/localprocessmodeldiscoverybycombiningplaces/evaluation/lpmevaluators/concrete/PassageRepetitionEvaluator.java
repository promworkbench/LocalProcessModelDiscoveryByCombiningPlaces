package org.processmining.localprocessmodeldiscoverybycombiningplaces.evaluation.lpmevaluators.concrete;

import org.processmining.localprocessmodeldiscoverybycombiningplaces.evaluation.lpmevaluators.AbstractLPMEvaluator;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.evaluation.results.concrete.PassageRepetitionEvaluationResult;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.model.LocalProcessModel;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.model.Place;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.model.additionalinfo.Passage;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.utils.PlaceUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PassageRepetitionEvaluator extends AbstractLPMEvaluator<PassageRepetitionEvaluationResult> {

    @Override
    public PassageRepetitionEvaluationResult evaluate(LocalProcessModel lpm) {
        Map<Passage, Integer> passageRepetitionMap = new HashMap<>();
        for (Place p : lpm.getPlaces()) {
            Set<Passage> placePassages = PlaceUtils.getPassages(p);
            for (Passage passage : placePassages) {
                int passageCount = passageRepetitionMap.getOrDefault(passage, 0);
                passageRepetitionMap.put(passage, passageCount + 1);
            }
        }
        return new PassageRepetitionEvaluationResult(lpm, passageRepetitionMap);
    }
}
