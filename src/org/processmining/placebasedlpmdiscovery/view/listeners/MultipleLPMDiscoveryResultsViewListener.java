package org.processmining.placebasedlpmdiscovery.view.listeners;

public interface MultipleLPMDiscoveryResultsViewListener {
    void selectFirstSet();

    void selectSecondSet();

    void selectIntersection();

    void selectUnion();

    void selectOnlyInFirstSet();

    void selectOnlyInSecondSet();
}
