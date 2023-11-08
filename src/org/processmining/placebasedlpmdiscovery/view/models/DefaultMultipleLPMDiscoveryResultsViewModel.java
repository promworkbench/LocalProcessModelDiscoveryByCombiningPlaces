package org.processmining.placebasedlpmdiscovery.view.models;

import org.processmining.placebasedlpmdiscovery.main.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.main.MultipleLPMDiscoveryResults;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

import java.util.Collection;
import java.util.HashSet;

public class DefaultMultipleLPMDiscoveryResultsViewModel implements MultipleLPMDiscoveryResultsViewModel {

    private Collection<LocalProcessModel> activeLPMs;
    private final MultipleLPMDiscoveryResults result;

    public DefaultMultipleLPMDiscoveryResultsViewModel(MultipleLPMDiscoveryResults result) {
        this.result = result;
        this.activeLPMs = this.intersection(result.getResults().keySet().toArray(new String[0]));
    }

    @Override
    public LPMDiscoveryResult getLPMDiscoveryResult(String name) {
        return this.result.getResult(name);
    }

    @Override
    public Collection<LocalProcessModel> intersection(String... setNames) {
        Collection<LocalProcessModel> res = null;
        for (String name : setNames) {
            if (res == null) {
                res = new HashSet<>(this.result.getResult(name).getAllLPMs());
                continue;
            }
            res.retainAll(this.result.getResult(name).getAllLPMs());
        }
        return res;
    }

    @Override
    public Collection<LocalProcessModel> union(String... setNames) {
        Collection<LocalProcessModel> res = null;
        for (String name : setNames) {
            if (res == null) {
                res = new HashSet<>();
            }
            res.addAll(this.result.getResult(name).getAllLPMs());
        }
        return res;
    }

    @Override
    public Collection<LocalProcessModel> diff(String... setNames) {
        Collection<LocalProcessModel> res = null;
        for (String name : setNames) {
            if (res == null) {
                res = new HashSet<>(this.result.getResult(name).getAllLPMs());
                continue;
            }
            res.removeAll(this.result.getResult(name).getAllLPMs());
        }
        return res;
    }

    @Override
    public Collection<LocalProcessModel> getLPMs() {
        return this.activeLPMs;
    }

    @Override
    public void setLPMs(Collection<LocalProcessModel> lpms) {
        this.activeLPMs = lpms;
    }
}
