package org.processmining.placebasedlpmdiscovery.runners.lpmdiscovery;

import org.processmining.placebasedlpmdiscovery.lpmdiscovery.LPMDiscovery;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.logs.EventLog;
import org.processmining.placebasedlpmdiscovery.model.logs.XLogWrapper;
import org.processmining.placebasedlpmdiscovery.prom.PlacesProvider;
import org.processmining.placebasedlpmdiscovery.utils.LogUtils;

public class SingleLPMDiscoveryRunner {

    public static void main(String[] args) throws Exception {
        String logPath = "data/logs/artificialBig.xes";
        String placesPath = "data/petrinets/artificialBig.pnml";
        String resultPath = "data/lpms/artificialBig.json";
//        String logPath = "data/logs/bpi2012_res10939.xes";
//        String placesPath = "data/placenets/bpi2012_res10939.json";
//        String resultPath = "data/lpms/bpi2012_res10939.json";
        EventLog eventLog = new XLogWrapper(LogUtils.readLogFromFile(logPath));

        LPMDiscoveryResult result = LPMDiscovery.placeBased(PlacesProvider.fromFile(placesPath))
                .from(eventLog.getOriginalLog());
        System.out.println(result.getAllLPMs().size());
        result.toFile(resultPath);
    }
}
