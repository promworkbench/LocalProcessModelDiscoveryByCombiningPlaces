package org.processmining.placebasedlpmdiscovery.runners;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.google.gson.Gson;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.Main;
import org.processmining.placebasedlpmdiscovery.grouping.GroupingController;
import org.processmining.placebasedlpmdiscovery.main.LPMDiscoveryBuilder;
import org.processmining.placebasedlpmdiscovery.main.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.exporting.importers.ImporterFactory;
import org.processmining.placebasedlpmdiscovery.model.exporting.importers.JsonImporter;
import org.processmining.placebasedlpmdiscovery.model.logs.XLogWrapper;
import org.processmining.placebasedlpmdiscovery.model.serializable.PlaceSet;
import org.processmining.placebasedlpmdiscovery.prom.plugins.mining.PlaceBasedLPMDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.utils.Constants;
import org.processmining.placebasedlpmdiscovery.utils.LogUtils;
import org.python.google.common.reflect.TypeToken;

import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClusteringRunner {

    public static void main(String[] args) throws Exception {
        if (args.length < 3) return;
        System.out.println(System.getProperty("user.dir"));
        String eventLogPath = args[0];
        String placeNetsPath = args[1];
        String configPath = args[2];
//        String resPath = args[3];

        System.out.println(eventLogPath + "\n" + placeNetsPath);
        run(eventLogPath, placeNetsPath, configPath, "resPath");
    }

    private static void run(String eventLogPath, String placeNetsPath, String configPath, String resPath) throws Exception {
        XLog log = LogUtils.readLogFromFile(eventLogPath);

        PlaceBasedLPMDiscoveryParameters parameters = new PlaceBasedLPMDiscoveryParameters(new XLogWrapper(log));
        parameters.setLpmCount(Integer.MAX_VALUE);
        parameters.getPlaceChooserParameters().setPlaceLimit(100);

        JsonImporter<PlaceSet> placeSetImporter = ImporterFactory.createPlaceSetJsonImporter();
        PlaceSet placeSet = placeSetImporter.read(PlaceSet.class, Files.newInputStream(Paths.get(placeNetsPath)));

        // compute the lpms
        LPMDiscoveryBuilder builder = Main.createDefaultBuilder(
                log,
                placeSet,
                parameters);
        LPMDiscoveryResult res = builder.build().run();

        // read config
        Gson gson = new Gson();
        List<Map<String, Object>> configs = gson.fromJson(
                new FileReader(configPath),
                new TypeToken<List<Map<String, Object>>>() {
                }.getType());

        // group lpms
        ImmutableTable.Builder<String, String, Integer> tableBuilder = new ImmutableTable.Builder<>();
        for (Map<String, Object> config : configs) {
            // group the lpms for the specific config
            GroupingController groupingController = new GroupingController(log);
            groupingController.groupLPMs(res.getAllLPMs(), config);

            // store the groups
            for (LocalProcessModel lpm : res.getAllLPMs()) {
                String clusterTitle = (String) config.get(Constants.Grouping.Config.TITLE);
                int cluster = lpm.getAdditionalInfo().getGroupsInfo()
                        .getGroupingProperty(clusterTitle);
                tableBuilder.put(lpm.getId(), clusterTitle, cluster);
            }
        }
        Table<String, String, Integer> lpmGroupsTable = tableBuilder.build();
        CSVPrinter csvPrinter = CSVFormat.DEFAULT.print(System.out);
        csvPrinter.printRecords(Collections.singleton(
                ImmutableList.builder()
                        .add("LPM Id")
                        .addAll(lpmGroupsTable.columnKeySet())
                        .build()));
        csvPrinter.printRecords(lpmGroupsTable.rowMap().entrySet()
                .stream().map(entry -> ImmutableList.builder()
                        .add(entry.getKey())
                        .addAll(entry.getValue().values())
                        .build())
                .collect(Collectors.toList()));
//        LocalProcessModelUtils.exportResult(new LPMResult((StandardLPMDiscoveryResult) res), resPath);
    }
}
