package org.processmining.lpms.quality.alignments.dp.dependency;

import org.processmining.models.graphbased.directed.petrinet.elements.Place;

import java.util.Objects;

public class PlaceDependency implements DependencyExpression {

    private final Place place;

    public PlaceDependency(Place place) {
        this.place = place;
    }

    public Place getPlace() {
        return place;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaceDependency that = (PlaceDependency) o;
        return Objects.equals(place, that.place);
    }

    @Override
    public int hashCode() {
        return Objects.hash(place);
    }

    @Override
    public String toString() {
        return place.getLabel();
    }
}