package org.processmining.placebasedlpmdiscovery.model.representationconverters;

public interface EntityRepresentationConverter<IN_TYPE, OUT_TYPE> {

    OUT_TYPE convert(IN_TYPE object);
}
