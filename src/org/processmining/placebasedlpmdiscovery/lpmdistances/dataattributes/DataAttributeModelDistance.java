package org.processmining.placebasedlpmdiscovery.lpmdistances.dataattributes;

import com.google.inject.Inject;
import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistance;

public abstract class DataAttributeModelDistance implements ModelDistance {

    protected final DataAttributeVectorExtractor dataAttrVecExtractor;
    @Inject
    public DataAttributeModelDistance(DataAttributeVectorExtractor dataAttrVecExtractor) {
        this.dataAttrVecExtractor = dataAttrVecExtractor;
    }

}
