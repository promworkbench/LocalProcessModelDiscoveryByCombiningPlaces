package org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.tableselection;

import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.EmittableDataTypeVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.EmittableDataVM;

public class NewPlaceSelectedEmittableDataVM implements EmittableDataVM {
    private final Place place;
    public NewPlaceSelectedEmittableDataVM(Place place) {
        this.place = place;
    }

    public Place getPlace() {
        return place;
    }

    @Override
    public EmittableDataTypeVM getType() {
        return EmittableDataTypeVM.NewPlaceSelectedVM;
    }
}
