package org.processmining.placebasedlpmdiscovery.runners.distancerunner;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.InputModule;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.results.FromFileLPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.lpmdistances.dataattributes.DataAttributeModelDistanceConfig;
import org.processmining.placebasedlpmdiscovery.lpmdistances.dataattributes.DataAttributeVectorExtractor;
import org.processmining.placebasedlpmdiscovery.lpmdistances.dependencyinjection.LPMDistancesDependencyInjectionModule;
import org.processmining.placebasedlpmdiscovery.main.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.utils.LogUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DataAttributeExtractorRunner {
    public static void main(String[] args) throws Exception {
        extractDataVectors(
                LogUtils.readLogFromFile("./data/bpi2012res10939.xes"),
                new FromFileLPMDiscoveryResult("./data/bpi2012res10939_lpms.json"),
                "./data/bpi2012res10939_datavectors.csv");
    }

    private static void extractDataVectors(XLog log, LPMDiscoveryResult lpmResult, String output) throws IOException {
        Injector injector = Guice.createInjector(
                new InputModule(log),
                new LPMDistancesDependencyInjectionModule()
        );

        List<LocalProcessModel> lpms = new ArrayList<>(lpmResult.getAllLPMs());

        System.out.println("LPMs imported");

        DataAttributeVectorExtractor vectorExtractor = injector.getInstance(DataAttributeVectorExtractor.class);
        vectorExtractor.convertToVectors(lpms);

        System.out.println("Distances computed");

        writeVectors(output, lpms, vectorExtractor.getPositionMapping(), vectorExtractor.convertToVectors(lpms));
    }

    private static void writeVectors(String filePath,
                                     List<LocalProcessModel> lpms,
                                     List<String> headers,
                                     List<double[]> distances) throws IOException {
        ImmutableTable.Builder<String, String, Double> tableBuilder = new ImmutableTable.Builder<>();

        for (int i = 0; i < lpms.size(); ++i) {
            for (int j = 0; j < headers.size(); ++j) {
                tableBuilder.put(lpms.get(i).getShortString(), headers.get(j), distances.get(i)[j]);
            }
        }

        Table<String, String, Double> distanceTable = tableBuilder.build();
        try (CSVPrinter csvPrinter = CSVFormat.DEFAULT
                .builder()
                .setHeader(headers.toArray(new String[0]))
                .build()
                .print(Paths.get(filePath), StandardCharsets.UTF_8)) {
            csvPrinter.printRecords(distanceTable.rowMap().entrySet()
                    .stream().map(entry -> ImmutableList.builder()
                            .add(entry.getKey())
                            .addAll(entry.getValue().values())
                            .build())
                    .collect(Collectors.toList()));
        }
    }
}
