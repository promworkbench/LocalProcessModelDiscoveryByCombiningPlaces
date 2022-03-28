package org.processmining.placebasedlpmdiscovery.placechooser.placepredicates;


import java.util.Collection;

public class PlacePredicateConfig {

    private ConfigElement root;

    public ConfigElement getRoot() {
        return root;
    }
}

class ConfigElement {
    PlacePredicateType type;
    Collection<ConfigElement> children;

    public boolean isSimple() {
        return children == null || children.isEmpty();
    }

    public PlacePredicateType getType() {
        return type;
    }

    public Collection<ConfigElement> getChildren() {
        return children;
    }
}