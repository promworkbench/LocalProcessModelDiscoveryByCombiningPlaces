package org.processmining.lpms.model.petrinets;

public class DefaultPetriNetTest implements PetriNetTest {

    @Override
    public PetriNet getPetriNetInstance() {
        return new DefaultPetriNet();
    }
}
