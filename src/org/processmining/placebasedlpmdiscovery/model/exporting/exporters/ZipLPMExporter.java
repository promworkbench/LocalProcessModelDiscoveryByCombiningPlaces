package org.processmining.placebasedlpmdiscovery.model.exporting.exporters;

import com.csvreader.CsvWriter;
import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.LPMEvaluationController;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.StandardLPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.aggregateoperations.EvaluationResultAggregateOperation;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.undecided.Utils;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.utils.LocalProcessModelUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Exports LPMs to a zip file named {@code <prefix>.<...>}, containing:
 * <ul>
 *     <li>{@code <prefix>.<lpmId>.pnml}: one accepting Petri net per LPM,</li>
 *     <li>{@code <prefix>.csv}: one row per LPM with the pnml file name, short string and scores,</li>
 *     <li>{@code <prefix>-coverage.csv}: which LPM covers which event, only if the result contains the event
 *     coverage computed during the discovery.</li>
 * </ul>
 * The zip can be imported again with
 * {@link org.processmining.placebasedlpmdiscovery.model.exporting.importers.ZipLPMImporter}.
 */
public class ZipLPMExporter {

    public static final String EVENT_COVERAGE_KEY = "eventCoverageSetLevel";
    private static final Map<String, StandardLPMEvaluationResultId> SCORE_COLUMNS = new LinkedHashMap<>();

    static {
        SCORE_COLUMNS.put("Fitting Windows Score", StandardLPMEvaluationResultId.FittingWindowsEvaluationResult);
        SCORE_COLUMNS.put("Trace Support Score", StandardLPMEvaluationResultId.TraceSupportEvaluationResult);
        SCORE_COLUMNS.put("Lift Score", StandardLPMEvaluationResultId.LiftEvaluationResult);
    }

    /**
     * @param context used to get the layout of the nets, can be null (e.g. when not running in ProM)
     */
    public static void export(LPMDiscoveryResult result, File file, PluginContext context) throws IOException {
        String fileName = file.getName();
        int prefixEnd = fileName.indexOf('.');
        String prefix = prefixEnd < 0 ? fileName : fileName.substring(0, prefixEnd);

        File parent = file.getAbsoluteFile().getParentFile();
        if (parent != null)
            Files.createDirectories(parent.toPath());

        Collection<LocalProcessModel> lpms = result.getAllLPMs();
        Function<LocalProcessModel, String> pnmlName = lpm -> prefix + "." + lpm.getId() + ".pnml";
        try (ZipOutputStream zip = new ZipOutputStream(Files.newOutputStream(file.toPath()))) {
            for (LocalProcessModel lpm : lpms) {
                zip.putNextEntry(new ZipEntry(pnmlName.apply(lpm)));
                AcceptingPetriNet apn = LocalProcessModelUtils.getAcceptingPetriNetRepresentation(lpm);
                if (context == null)
                    Utils.exportAcceptingPetriNetToOutputStream(apn, zip);
                else
                    Utils.exportAcceptingPetriNetToOutputStream(context, apn, zip);
                zip.closeEntry();
            }

            zip.putNextEntry(new ZipEntry(prefix + ".csv"));
            writeScores(lpms, pnmlName, zip);
            zip.closeEntry();

            Object coverage = result.getAdditionalResults() == null ?
                    null : result.getAdditionalResults().get(EVENT_COVERAGE_KEY);
            if (coverage instanceof LPMEvaluationController.EventCoverageSetLevel) {
                zip.putNextEntry(new ZipEntry(prefix + "-coverage.csv"));
                Writer writer = new OutputStreamWriter(zip, StandardCharsets.UTF_8);
                ((LPMEvaluationController.EventCoverageSetLevel) coverage)
                        .export(writer, lpms, pnmlName);
                writer.flush();
                zip.closeEntry();
            }
        }
    }

    private static void writeScores(Collection<LocalProcessModel> lpms, Function<LocalProcessModel, String> pnmlName,
                                    OutputStream out) throws IOException {
        // the csv writer is not closed, as that would close the zip
        CsvWriter csvWriter = new CsvWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8), ',');
        csvWriter.write("Name");
        csvWriter.write("Short String");
        for (String column : SCORE_COLUMNS.keySet())
            csvWriter.write(column);
        csvWriter.write("Aggregated Score");
        csvWriter.endRecord();

        EvaluationResultAggregateOperation aggregateOperation = new EvaluationResultAggregateOperation();
        for (LocalProcessModel lpm : lpms) {
            csvWriter.write(pnmlName.apply(lpm));
            csvWriter.write(lpm.getShortString());
            for (StandardLPMEvaluationResultId id : SCORE_COLUMNS.values()) {
                LPMEvaluationResult result = lpm.getAdditionalInfo()
                        .getEvaluationResult(id.name(), LPMEvaluationResult.class);
                csvWriter.write(result == null ? "" : String.valueOf(result.getResult()));
            }
            csvWriter.write(String.valueOf(
                    aggregateOperation.aggregate(lpm.getAdditionalInfo().getEvaluationResults().values())));
            csvWriter.endRecord();
        }
        csvWriter.flush();
    }
}
