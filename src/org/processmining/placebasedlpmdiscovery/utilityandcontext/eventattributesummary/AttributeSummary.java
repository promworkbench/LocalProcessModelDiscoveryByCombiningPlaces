package org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary;

import org.deckfour.xes.model.XAttribute;

import java.util.List;
import java.util.Map;

public abstract class AttributeSummary<T, C extends XAttribute> {
    public static final String MIN = "min";
    public static final String MAX = "max";
    public static final String COUNT = "count";
    public static final String SUM = "sum";
    public static final String MEAN = "mean";
    public static final String MEDIAN = "median";


    /**
     * Used to determine whether all values that this summary summarizes are stored or not.
     */
    protected final boolean completeList;
    protected Class<C> attributeClass;
    protected String key;
    protected List<T> values;
    protected Map<String, Number> representationFeatures;
    private boolean changeDetected;

    public AttributeSummary(String key, boolean completeList) {
        this.key = key;
        this.completeList = completeList;
        initializeRepresentationFeatures();
        this.setAttributeClass();
    }

    public String getKey() {
        return this.key;
    }

    protected abstract void setAttributeClass();

    public void addValue(XAttribute attribute) {
        if (attributeClass.isInstance(attribute)) {
            if (this.completeList) {
                this.values.add(extractAttributeValue(attribute));
            } else {
                this.addValueInSummary(attribute);
            }
            this.changeDetected = true;
        } else {
            throw new IllegalArgumentException("The provided attribute is of type: " + attribute.getClass() +
                    " while the expected type is: " + attributeClass);
        }
    }

    protected abstract void addValueInSummary(XAttribute attribute);

    protected abstract T extractAttributeValue(XAttribute attribute);

//    public abstract void summarize();

    protected abstract void initializeRepresentationFeatures();

    protected abstract void computeRepresentationFeatures();

    public Map<String, Number> getRepresentationFeatures() {
        // if we do not keep the complete list of values, the representation features are already computed
        if (!this.completeList) {
            return this.representationFeatures;
        }
        // if there is no values return some default representation features
        if (this.values.isEmpty()) {
            this.initializeRepresentationFeatures();
        }

        // if first computation or there is a change
        if (this.representationFeatures == null || this.changeDetected) {
            this.computeRepresentationFeatures(); // compute representation features
            this.changeDetected = false; // until something is added changeDetected is false
        }
        return this.representationFeatures;
    }

    public Class<C> getAttributeClass() {
        return attributeClass;
    }

    public abstract boolean acceptValue(XAttribute attribute);
}
