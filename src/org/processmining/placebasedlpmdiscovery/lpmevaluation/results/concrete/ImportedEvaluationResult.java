package org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.SimpleEvaluationResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.StandardLPMEvaluationResultId;

import java.util.Objects;

/**
 * Evaluation result whose value was read from a file (e.g. the csv in an exported zip) instead of being computed
 * on a log. Only the final score is known, so it is used as the normalized result as well.
 */
public class ImportedEvaluationResult extends SimpleEvaluationResult {

    // the field name is used by the json deserializer to recognize imported results
    public static final String RESULT_FIELD_NAME = "importedResult";
    private static final long serialVersionUID = 4127663512077095142L;
    private final double importedResult;

    public ImportedEvaluationResult(LPMEvaluationResultId id, double importedResult) {
        super(id);
        this.importedResult = importedResult;
    }

    @Override
    public double getResult() {
        return importedResult;
    }

    @Override
    public double getNormalizedResult() {
        // lift is unbounded, so it is normalized the same way as when it is computed
        if (StandardLPMEvaluationResultId.LiftEvaluationResult.equals(getId()))
            return LiftEvaluationResult.normalize(importedResult);
        return importedResult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImportedEvaluationResult that = (ImportedEvaluationResult) o;
        return Double.compare(importedResult, that.importedResult) == 0 && getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), importedResult);
    }
}
