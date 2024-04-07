package org.processmining.placebasedlpmdiscovery.lpmdistances.dataattributes;

import com.google.inject.Inject;
import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistance;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

import java.util.*;

public abstract class DataAttributeModelDistance implements ModelDistance {

    protected final DataAttributeVectorExtractor dataAttrVecExtractor;
    @Inject
    public DataAttributeModelDistance(DataAttributeVectorExtractor dataAttrVecExtractor) {
        this.dataAttrVecExtractor = dataAttrVecExtractor;
    }

}
