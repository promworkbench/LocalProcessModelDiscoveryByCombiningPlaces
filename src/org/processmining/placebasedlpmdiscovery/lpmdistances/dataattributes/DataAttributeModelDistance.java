package org.processmining.placebasedlpmdiscovery.lpmdistances.dataattributes;

import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistance;

import java.util.Collection;

public abstract class DataAttributeModelDistance implements ModelDistance {

    protected Collection<String> attributes;
    protected final DataAttributeVectorExtractor dataAttrVecExtractor;

    public DataAttributeModelDistance(DataAttributeVectorExtractor dataAttrVecExtractor) {
        this.dataAttrVecExtractor = dataAttrVecExtractor;
    }

    public void setAttributes(Collection<String> attributes) {
        this.attributes = attributes;
    }

}
