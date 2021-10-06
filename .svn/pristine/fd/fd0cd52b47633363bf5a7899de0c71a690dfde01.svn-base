package org.processmining.placebasedlpmdiscovery.evaluation.undecided;

public class LocalProcessModelEvaluationResults {

    private int frequency;
    private double support;
    private double confidence;
    private double determinism;
    private double languageFit;
    private double coverage;

    public LocalProcessModelEvaluationResults(int frequency, double support, double confidence, double determinism,
                                              double languageFit, double coverage) {
        this.frequency = frequency;
        this.support = support;
        this.confidence = confidence;
        this.determinism = determinism;
        this.languageFit = languageFit;
        this.coverage = coverage;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public double getSupport() {
        return support;
    }

    public void setSupport(double support) {
        this.support = support;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public double getDeterminism() {
        return determinism;
    }

    public void setDeterminism(double determinism) {
        this.determinism = determinism;
    }

    public double getLanguageFit() {
        return languageFit;
    }

    public void setLanguageFit(double languageFit) {
        this.languageFit = languageFit;
    }

    public double getCoverage() {
        return coverage;
    }

    public void setCoverage(double coverage) {
        this.coverage = coverage;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("frequency:").append("\t").append(this.frequency).append("\n");
        sb.append("support:").append("\t").append(this.support).append("\n");
        sb.append("confidence:").append("\t").append(this.confidence).append("\n");
        sb.append("coverage:").append("\t").append(this.coverage).append("\n");
        sb.append("language_fit:").append("\t").append(this.languageFit).append("\n");
        sb.append("determinism:").append("\t").append(this.determinism).append("\n");
        return sb.toString();
    }
}
