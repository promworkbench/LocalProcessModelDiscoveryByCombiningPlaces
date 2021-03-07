package org.processmining.placebasedlpmdiscovery.placediscovery.converters.additionalinfo;//package org.processmining.placebasedlpmdiscovery.placediscovery.converters.additionalinfo;
//
//import org.processmining.placebasedlpmdiscovery.model.placeinfo.PlaceAdditionalInfo;
//import org.processmining.placebasedlpmdiscovery.model.placeinfo.TraceVariantInfo;
//import org.processmining.v7.forViki.model.DetailedPlaceInfo;
//
//import java.util.stream.Stream;
//
//public class DetailedPlaceInfoConverter {
//
//    public static PlaceAdditionalInfo convert(DetailedPlaceInfo detailedPlaceInfo) {
//        PlaceAdditionalInfo res = new PlaceAdditionalInfo();
//
//        res.setFitnessScore(detailedPlaceInfo.getFitnessScore());
//        res.setLogFrequency(detailedPlaceInfo.getFrequencyLog());
//        res.setMissingTokensPerActivity(detailedPlaceInfo.getMissingTokensPerActivity());
//        res.setTraceVariantInfo(Stream.of(detailedPlaceInfo.getTraceVariantInfo())
//                .map(TraceVariantInfoConverter::convert).toArray(TraceVariantInfo[]::new));
//
//        return res;
//    }
//}