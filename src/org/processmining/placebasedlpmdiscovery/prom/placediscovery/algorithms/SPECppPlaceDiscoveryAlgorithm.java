package org.processmining.placebasedlpmdiscovery.prom.placediscovery.algorithms;

import org.deckfour.xes.model.XLog;
import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.PlaceDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.StandardPlaceDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.converters.place.AbstractPlaceConverter;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.parameters.SPECppPlaceDiscoveryParameters;
import org.processmining.specpp.base.impls.SPECpp;
import org.processmining.specpp.composition.BasePlaceComposition;
import org.processmining.specpp.config.parameters.ExecutionParameters;
import org.processmining.specpp.datastructures.petri.CollectionOfPlaces;
import org.processmining.specpp.datastructures.petri.Place;
import org.processmining.specpp.datastructures.petri.ProMPetrinetWrapper;
import org.processmining.specpp.orchestra.ExecutionEnvironment;
import org.processmining.specpp.preprocessing.InputDataBundle;

import java.time.Duration;

public class SPECppPlaceDiscoveryAlgorithm extends PlaceDiscoveryAlgorithm<SPECppPlaceDiscoveryParameters,
        AcceptingPetriNet> {

    public SPECppPlaceDiscoveryAlgorithm(AbstractPlaceConverter<AcceptingPetriNet> converter,
                                         SPECppPlaceDiscoveryParameters parameters) {
        super(converter, parameters);
    }

    @Override
    public PlaceDiscoveryResult getPlaces(XLog log) {
        InputDataBundle input = InputDataBundle.process(log, parameters.getConfigBundle().getInputProcessingConfig());
        SPECpp<Place, BasePlaceComposition, CollectionOfPlaces, ProMPetrinetWrapper> specpp =
                SPECpp.build(parameters.getConfigBundle(), input);

        ExecutionEnvironment.SPECppExecution<Place, BasePlaceComposition, CollectionOfPlaces, ProMPetrinetWrapper> execution;
        try (ExecutionEnvironment ee = new ExecutionEnvironment(Runtime.getRuntime().availableProcessors())) {
            execution = ee.execute(specpp, ExecutionParameters.timeouts(
                    new ExecutionParameters.ExecutionTimeLimits(
                            Duration.ofMinutes(5), Duration.ofMinutes(50), Duration.ofMinutes(60))));

            ee.addCompletionCallback(execution, ex -> {
                ProMPetrinetWrapper petrinetWrapper = ex.getSPECpp().getPostProcessedResult();
                System.out.println(petrinetWrapper == null ? "null" : "not null" +
                        " num places: " + petrinetWrapper.getPlaces().size() +
                        "num transitions: " + petrinetWrapper.getTransitions().size() +
                        "num edges: " + petrinetWrapper.getEdges().size());
            });

            ee.join();
            StandardPlaceDiscoveryResult result = new StandardPlaceDiscoveryResult();
            AcceptingPetriNet acceptingPetriNet = execution.getSPECpp().getPostProcessedResult().asAcceptingPetrinet();
            result.setPlaces(this.converter.convert(acceptingPetriNet));
            result.setLog(log);
            return result;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
