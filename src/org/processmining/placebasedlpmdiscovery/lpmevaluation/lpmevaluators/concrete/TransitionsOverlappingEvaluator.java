package org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.concrete;

import com.google.common.collect.Sets;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.AbstractLPMEvaluator;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.TransitionsOverlappingEvaluationResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.Transition;

import java.util.Set;

public class TransitionsOverlappingEvaluator extends AbstractLPMEvaluator<TransitionsOverlappingEvaluationResult> {

    @Override
    public TransitionsOverlappingEvaluationResult evaluate(LocalProcessModel lpm) {
        if (lpm.getPlaces().size() < 1)
            throw new IllegalArgumentException("The local process model doesn't have any places in it.");

        TransitionsOverlappingEvaluationResult result = new TransitionsOverlappingEvaluationResult(lpm);

        if (lpm.getPlaces().size() == 1) {
            Place place = lpm.getPlaces().iterator().next();
            result.setOverlappingScore(1 - this.getOverlappingScore(place.getInputTransitions(), place.getOutputTransitions()));
            return result;
        }

        double sum = 0;
        double min = 1;
        for (Place p1 : lpm.getPlaces()) {
            for (Place p2 : lpm.getPlaces())
                if (!p1.equals(p2)) {
                    double score = this.getOverlappingScore(p1, p2);
                    sum += score;
                    if (score < min)
                        min = score;
                }
        }

//        int count = lpm.getPlaces().size();
//        result.setOverlappingScore(sum * 1.0 / (count * (count - 1)));
        result.setOverlappingScore(min);
        return result;
    }

    private double getOverlappingScore(Place p1, Place p2) {
        // if the places do not touch we return maximum score
        if (Sets.intersection(
                Sets.union(p1.getInputTransitions(), p1.getOutputTransitions()),
                Sets.union(p2.getInputTransitions(), p2.getOutputTransitions())).size() < 1)
            return 1;

        double inOut = getOverlappingScore(p1.getInputTransitions(), p2.getOutputTransitions());
        double outIn = getOverlappingScore(p1.getOutputTransitions(), p2.getInputTransitions());
        double inIn = getOverlappingScore(p1.getInputTransitions(), p2.getInputTransitions());
        double outOut = getOverlappingScore(p1.getOutputTransitions(), p2.getOutputTransitions());

        // TODO: probably should be improved
        return (Math.max(inOut, outIn) + (1 - inIn) + (1 - outOut)) / 3.0;
    }

    private double getOverlappingScore(Set<Transition> set1, Set<Transition> set2) {
        int intersectionCount = Sets.intersection(set1, set2).size();
        int unionCount = Sets.union(set1, set2).size();
        return intersectionCount * 1.0 / unionCount;
    }
}
