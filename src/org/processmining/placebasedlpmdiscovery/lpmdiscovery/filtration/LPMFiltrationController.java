package org.processmining.placebasedlpmdiscovery.lpmdiscovery.filtration;

import org.processmining.placebasedlpmdiscovery.RunningContext;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.EvaluatorHub;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.LPMEvaluator;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.LPMEvaluatorFactory;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.WindowLPMEvaluator;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.AbstractEvaluationResult;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.lpms.LPMFilter;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.lpms.NeedsEvaluationLPMFilter;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.additionalinfo.LPMAdditionalInfo;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.LPMTemporaryWindowInfo;

import java.util.*;

public class LPMFiltrationController {

    private RunningContext runningContext;
    private final Map<String, LPMFilter> filterMap;
    private final List<LPMFilter> finalFilters;
    private final PriorityQueue<LPMFilter> filters;
    private List<LPMFilter> beforeEvalFilters;
    private List<LPMFilter> afterEvalFilters;


    public LPMFiltrationController(RunningContext runningContext) {
        this.runningContext = runningContext;

        this.filterMap = new HashMap<>();
        this.beforeEvalFilters = new ArrayList<>();
        this.afterEvalFilters = new ArrayList<>();
        this.finalFilters = new ArrayList<>();
        this.filters = new PriorityQueue<>(Comparator.comparingInt(LPMFilter::getPriority));
    }

    public Set<LocalProcessModel> filterFinals(Set<LocalProcessModel> lpms) {
        System.out.println(lpms.size());

        for (LPMFilter filter : this.finalFilters)
            lpms = filter.filter(lpms);

        System.out.println(lpms.size());
        return lpms;
    }

    public Set<LocalProcessModel> filterLPMs(Set<LocalProcessModel> lpms) {
        Set<LocalProcessModel> filtered = new HashSet<>();
        for (LocalProcessModel lpm : lpms)
            if (shouldKeepLPM(lpm))
                filtered.add(lpm);
        return filtered;
    }

    public boolean shouldKeepLPM(LocalProcessModel lpm) {
        for (LPMFilter filter : this.filters) {
            if (filter.needsEvaluation()) {
                NeedsEvaluationLPMFilter needsEvaluationLPMFilter = (NeedsEvaluationLPMFilter) filter;
                // evaluate the lpms that haven't been evaluated
                if (lpm.getAdditionalInfo().getEvaluationResult(
                        needsEvaluationLPMFilter.getEvaluationId().name(),
                        LPMEvaluationResult.class) == null)
                    lpm.getAdditionalInfo().addEvaluationResult(
                            needsEvaluationLPMFilter.getEvaluationId().name(),
                            this.runningContext.getLpmEvaluationController()
                                    .evaluate(needsEvaluationLPMFilter.getEvaluatorId(), lpm));
            }
            if (!filter.shouldKeep(lpm))
                return false;
        }
        return true;
    }

    private void addFiltersInMap(List<LPMFilter> filters, boolean needsEvaluation) {
        for (LPMFilter filter : filters)
            this.addFilterInMap(filter, needsEvaluation);
    }

    private void addFilterInMap(LPMFilter filter, boolean needsEvaluation) {
        this.filterMap.put(filter.getClass().getSimpleName() + "-" + (needsEvaluation ? "After" : "Before"), filter);
        this.filters.add(filter);
    }

    private void clearFiltersInMap(boolean needsEvaluation) {
        this.filterMap.entrySet().removeIf(e -> e.getKey().endsWith(needsEvaluation ? "After" : "Before"));
    }

    public void addLPMFilter(LPMFilter filter, boolean needsEvaluation) {
        this.addFilterInMap(filter, needsEvaluation);
        if (needsEvaluation)
            this.afterEvalFilters.add(filter);
        else
            this.beforeEvalFilters.add(filter);
    }

}
