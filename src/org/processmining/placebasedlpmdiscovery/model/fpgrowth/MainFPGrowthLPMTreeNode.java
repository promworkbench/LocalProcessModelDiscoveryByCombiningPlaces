package org.processmining.placebasedlpmdiscovery.model.fpgrowth;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.WindowsEvaluationResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class MainFPGrowthLPMTreeNode {
    private final Set<MainFPGrowthLPMTreeNode> children;
    private final MainFPGrowthLPMTreeNode parent;
    private Place place;
    private WindowsEvaluationResult windowsEvaluationResult;

    public MainFPGrowthLPMTreeNode(MainFPGrowthLPMTreeNode parent) {
        this.parent = parent;
        this.children = new HashSet<>();
    }

    public MainFPGrowthLPMTreeNode(MainFPGrowthLPMTreeNode parent, Place place) {
        this.parent = parent;
        this.place = place;
        this.children = new HashSet<>();
    }

    public boolean hasChild(Place place) {
        for (MainFPGrowthLPMTreeNode child : children) {
            if (child.contains(place))
                return true;
        }
        return false;
    }

    public Set<MainFPGrowthLPMTreeNode> getChildren() {
        return children;
    }

    private boolean contains(Place place) {
        return this.place.equals(place);
    }

    public MainFPGrowthLPMTreeNode getChild(Place place) {
        for (MainFPGrowthLPMTreeNode child : children) {
            if (child.contains(place))
                return child;
        }
        return null;
    }

    public WindowsEvaluationResult getWindowsEvaluationResult() {
        return windowsEvaluationResult;
    }

    public void setWindowsEvaluationResult(WindowsEvaluationResult windowsEvaluationResult) {
        this.windowsEvaluationResult = windowsEvaluationResult;
    }

    public MainFPGrowthLPMTreeNode add(MainFPGrowthLPMTreeNode parent, Place place) {
        MainFPGrowthLPMTreeNode child = new MainFPGrowthLPMTreeNode(parent, place);
        children.add(child);
        return child;
    }

    public void updateEvaluation(int count, List<Integer> window, LPMTemporaryInfo lpmTemporaryInfo, Integer traceVariantId) {
        this.windowsEvaluationResult.updatePositive(count, window, lpmTemporaryInfo, traceVariantId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MainFPGrowthLPMTreeNode that = (MainFPGrowthLPMTreeNode) o;
        return Objects.equals(place, that.place) &&
                Objects.equals(children, that.children) &&
                Objects.equals(windowsEvaluationResult, that.windowsEvaluationResult);
    }

    @Override
    public int hashCode() {
        return Objects.hash(place, children, windowsEvaluationResult);
    }

    public LocalProcessModel getLPM() {
        LocalProcessModel lpm = this.parent == null ? new LocalProcessModel() : parent.getLPM();
        if (this.place != null)
            lpm.addPlace(place);
        return lpm;
    }

    public int getHeight() {
        if (children.size() <= 0)
            return 0;
        return children.stream().mapToInt(MainFPGrowthLPMTreeNode::getHeight).max().getAsInt() + 1;
    }
}
