package org.processmining.lpms.model.petrinets.nodes;

public interface Transition extends Node {

    /**
     * Creates a Transition with the given label.
     *
     * @param label The label of the transition.
     * @return A new Transition instance.
     */
    static Transition of(String label) {
        return new DefaultTransition(label);
    }

    /**
     * Creates an invisible Transition (a transition without a label).
     *
     * @return A new invisible Transition instance.
     */
    static Transition invisible() {
        return new DefaultTransition();
    }

    boolean isVisible();
}
