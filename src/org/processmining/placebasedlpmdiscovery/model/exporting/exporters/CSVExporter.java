package org.processmining.placebasedlpmdiscovery.model.exporting.exporters;

import com.csvreader.CsvWriter;
import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.LPMEvaluationController;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.StandardLPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.aggregateoperations.EvaluationResultAggregateOperation;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.undecided.Utils;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.utils.LocalProcessModelUtils;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CSVExporter {

    public static void export(LPMDiscoveryResult object, String directory) {
        try {
            Path dirPath = Paths.get(directory);
            Files.createDirectories(dirPath);

            // coverage
            LPMEvaluationController.EventCoverageSetLevel coverage = ((LPMEvaluationController.EventCoverageSetLevel)
                    object.getAdditionalResults().get("eventCoverageSetLevel"));

            coverage.export(dirPath.resolve("coverage.csv").toFile());

            // lpms
            String csvFileName = "index.csv";
            CsvWriter csvWriter = new CsvWriter(new OutputStreamWriter(Files.newOutputStream(dirPath.resolve(csvFileName).toFile().toPath()), StandardCharsets.UTF_8), ',');
            csvWriter.writeRecord(new String[]{"Name", "Short String", "Fitting Windows Score", "Trace Support Score",
                    "Aggregated Score"});

            EvaluationResultAggregateOperation aggregateOperation = new EvaluationResultAggregateOperation();
            StandardLPMEvaluationResultId[] ids = new StandardLPMEvaluationResultId[]{
                    StandardLPMEvaluationResultId.FittingWindowsEvaluationResult,
                    StandardLPMEvaluationResultId.TraceSupportEvaluationResult
            };

            for (LocalProcessModel lpm : object.getAllLPMs()) {
                // convert lpm to accepting petri net
                AcceptingPetriNet apn = LocalProcessModelUtils.getAcceptingPetriNetRepresentation(lpm);

                String apnFileName = lpm.getId() + ".pnml";

                // add the net file to the zip folder
                Utils.exportAcceptingPetriNetToOutputStream(apn,
                        Files.newOutputStream(dirPath.resolve(apnFileName).toFile().toPath()));

                // write an entry in the csv
                csvWriter.write(apnFileName);
                csvWriter.write(lpm.getShortString());
                for (LPMEvaluationResultId id : ids) {
                    csvWriter.write(String.format("%.5f", lpm.getAdditionalInfo()
                            .getEvaluationResult(id.name(), LPMEvaluationResult.class).getResult()));
                }
                csvWriter.write(String.valueOf(aggregateOperation.aggregate(lpm.getAdditionalInfo().getEvaluationResults().values())));
                csvWriter.endRecord();
                csvWriter.flush();
            }
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
