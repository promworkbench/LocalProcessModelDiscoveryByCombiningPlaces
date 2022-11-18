package org.processmining.placebasedlpmdiscovery.placechooser.placepredicates;

public class PlacePredicateFactory {

    public PlacePredicate selfLoop() {
        return new NonSelfLoopPlacePredicate();
    }

    public PlacePredicate emptyIOTransitionSet() {
        return new EmptyIOTransitionSetPlacePredicate();
    }

    public PlacePredicate getPredicate(PlacePredicateConfig config) {
        return getPredicate(config.getRoot());
    }

    private PlacePredicate getPredicate(ConfigElement el) {
        if (el.isSimple())
            return getSimplePredicate(el.getType());

        return getComplexPredicate(
                el.getType(),
                el.getChildren().stream().map(this::getPredicate).toArray(PlacePredicate[]::new)
        );
    }

    private PlacePredicate getComplexPredicate(PlacePredicateType type, PlacePredicate... subPredicates) {
        if (type == PlacePredicateType.AND) {
            return new AndPredicate(subPredicates);
        } else if (type == PlacePredicateType.OR) {
            return new OrPredicate(subPredicates);
        } else {
            throw new IllegalArgumentException("The type: " + type + " is not complex or it doesn't exist.");
        }
    }

    private PlacePredicate getSimplePredicate(PlacePredicateType type) {
        if (type == PlacePredicateType.SELF_LOOP) {
            return selfLoop();
        } else if (type == PlacePredicateType.EMPTY_IO_TRANSITION_SET) {
            return emptyIOTransitionSet();
        } else {
            throw new IllegalArgumentException("The type: " + type + " is not simple or it doesn't exist.");
        }
    }
}
