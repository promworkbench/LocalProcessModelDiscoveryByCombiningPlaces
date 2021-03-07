package org.processmining.placebasedlpmdiscovery.lpmdiscovery.filtration;

import org.processmining.placebasedlpmdiscovery.evaluation.lpmevaluators.AbstractLPMEvaluator;
import org.processmining.placebasedlpmdiscovery.evaluation.lpmevaluators.LPMEvaluatorFactory;
import org.processmining.placebasedlpmdiscovery.evaluation.results.AbstractEvaluationResult;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.lpms.LPMFilter;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.lpms.NeedsEvaluationLPMFilter;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

import java.util.*;

public class LPMFiltrationAndEvaluationController {

    private final Map<String, LPMFilter> filterMap;
    private final List<LPMFilter> finalFilters;
    private final PriorityQueue<LPMFilter> filters;
    private List<LPMFilter> beforeEvalFilters;
    private List<LPMFilter> afterEvalFilters;
    private LPMEvaluatorFactory evaluatorFactory;

    public LPMFiltrationAndEvaluationController() {
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
                AbstractLPMEvaluator<? extends AbstractEvaluationResult> evaluator =
                        this.evaluatorFactory.getEvaluator(needsEvaluationLPMFilter.getEvaluatorId());
                if (lpm.getAdditionalInfo().getEvaluationResult().getEvaluationResult(needsEvaluationLPMFilter.getEvaluationId()) == null)
                    lpm.getAdditionalInfo().getEvaluationResult().addResult(evaluator.evaluate(lpm));
            }
            if (!filter.shouldKeep(lpm))
                return false;
        }
        return true;
    }

    public void setBeforeEvalFilters(List<LPMFilter> beforeEvalFilters) {
        this.beforeEvalFilters = beforeEvalFilters;
        this.clearFiltersInMap(false);
        this.addFiltersInMap(beforeEvalFilters, false);
    }

    public void setAfterEvalFilters(List<LPMFilter> afterEvalFilters) {
        this.afterEvalFilters = afterEvalFilters;
        this.clearFiltersInMap(true);
        this.addFiltersInMap(afterEvalFilters, true);
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

    private void clearFiltersInMap() {
        this.filterMap.clear();
    }

    public void addLPMFilter(LPMFilter filter, boolean needsEvaluation) {
        this.addFilterInMap(filter, needsEvaluation);
        if (needsEvaluation)
            this.afterEvalFilters.add(filter);
        else
            this.beforeEvalFilters.add(filter);
    }

    public void addFinalLPMFilter(LPMFilter filter) {
        this.finalFilters.add(filter);
    }

    public void setEvaluatorFactory(LPMEvaluatorFactory evaluatorFactory) {
        this.evaluatorFactory = evaluatorFactory;
    }
}
