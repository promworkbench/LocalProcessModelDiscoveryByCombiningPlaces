package org.processmining.placebasedlpmdiscovery.lpmdiscovery.filtration;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.LPMEvaluator;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.LPMEvaluatorFactory;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.WindowLPMEvaluator;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.AbstractEvaluationResult;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.lpms.LPMFilter;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.lpms.NeedsEvaluationLPMFilter;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.additionalinfo.LPMAdditionalInfo;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.LPMTemporaryWindowInfo;

import java.util.*;

public class LPMFiltrationAndEvaluationController implements EvaluatorHub {

    private final Map<String, LPMFilter> filterMap;
    private final List<LPMFilter> finalFilters;
    private final PriorityQueue<LPMFilter> filters;
    private List<LPMFilter> beforeEvalFilters;
    private List<LPMFilter> afterEvalFilters;
    private LPMEvaluatorFactory evaluatorFactory;
    private List<WindowLPMEvaluator<?>> windowEvaluators;

    public LPMFiltrationAndEvaluationController() {
        this.filterMap = new HashMap<>();
        this.beforeEvalFilters = new ArrayList<>();
        this.afterEvalFilters = new ArrayList<>();
        this.finalFilters = new ArrayList<>();
        this.filters = new PriorityQueue<>(Comparator.comparingInt(LPMFilter::getPriority));
        this.windowEvaluators = new ArrayList<>();
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
                LPMEvaluator<? extends AbstractEvaluationResult> evaluator =
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

    public void evaluateForOneWindow(LocalProcessModel lpm, LPMTemporaryWindowInfo tempInfo, LPMAdditionalInfo additionalInfo) {
        for (WindowLPMEvaluator<?> evaluator : this.windowEvaluators) {
            if (!additionalInfo.existsInfo(evaluator.getKey())) {
                additionalInfo.addInfo(evaluator.getKey(), evaluator.createEmptyInfo());
            }
//            additionalInfo.updateInfo(
//                    evaluator.getKey(),
//                    evaluator.evaluate(lpm, tempInfo,
//                            additionalInfo.getInfo(evaluator.getKey(), evaluator.getResultClass())));
            additionalInfo.updateInfo(
                    evaluator.getKey(),
                    evaluator.evaluate(lpm, tempInfo,
                            additionalInfo.<AbstractEvaluationResult>getInfo(evaluator.getKey(), AbstractEvaluationResult.class)));
        }
    }

    @Override
    public void registerEvaluator(WindowLPMEvaluator<?> evaluator) {
        this.windowEvaluators.add(evaluator);
    }
}
