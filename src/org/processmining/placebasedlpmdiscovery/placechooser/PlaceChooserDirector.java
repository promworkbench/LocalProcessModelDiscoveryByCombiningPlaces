package org.processmining.placebasedlpmdiscovery.placechooser;

public class PlaceChooserDirector {

    private PlaceChooserBuilder placeChooserBuilder;

    public PlaceChooserDirector() {
    }

    public PlaceChooser buildPlaceChooser() {
        return placeChooserBuilder.build();
    }
}
