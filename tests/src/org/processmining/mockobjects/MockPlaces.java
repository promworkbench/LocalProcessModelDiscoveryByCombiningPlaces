package src.org.processmining.mockobjects;

import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.Transition;

public class MockPlaces {

    public static Place getSequencePlace_ab() {
        Place p = new Place();
        p.addInputTransition(new Transition("a", false));
        p.addOutputTransition(new Transition("b", false));
        return p;
    }

    public static Place getSequencePlace_bc() {
        Place p = new Place();
        p.addInputTransition(new Transition("b", false));
        p.addOutputTransition(new Transition("c", false));
        return p;
    }

    public static Place getChoiceOutPlace() {
        Place p = new Place();
        p.addInputTransition(new Transition("a", false));
        p.addOutputTransition(new Transition("b", false));
        p.addOutputTransition(new Transition("c", false));
        return p;
    }

    public static Place getChoiceInPlace() {
        Place p = new Place();
        p.addInputTransition(new Transition("a", false));
        p.addInputTransition(new Transition("b", false));
        p.addOutputTransition(new Transition("c", false));
        return p;
    }

    public static Place getDoubleChoicePlace() {
        Place p = new Place();
        p.addInputTransition(new Transition("a", false));
        p.addInputTransition(new Transition("b", false));
        p.addOutputTransition(new Transition("c", false));
        p.addOutputTransition(new Transition("d", false));
        return p;
    }
}
