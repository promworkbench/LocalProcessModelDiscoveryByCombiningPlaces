package org.processmining.placebasedlpmdiscovery.prom.placediscovery.algorithms;

import org.deckfour.xes.model.XLog;
import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.Transition;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.PlaceDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.StandardPlaceDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.converters.place.AbstractPlaceConverter;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.parameters.SPECppPlaceDiscoveryParameters;
import org.processmining.specpp.base.impls.SPECpp;
import org.processmining.specpp.composition.BasePlaceComposition;
import org.processmining.specpp.config.parameters.ExecutionParameters;
import org.processmining.specpp.datastructures.petri.CollectionOfPlaces;
import org.processmining.specpp.datastructures.petri.ProMPetrinetWrapper;
import org.processmining.specpp.orchestra.ExecutionEnvironment;
import org.processmining.specpp.preprocessing.InputDataBundle;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

public class SPECppPlaceDiscoveryAlgorithm extends PlaceDiscoveryAlgorithm<SPECppPlaceDiscoveryParameters,
        AcceptingPetriNet> {

    public SPECppPlaceDiscoveryAlgorithm(AbstractPlaceConverter<AcceptingPetriNet> converter,
                                         SPECppPlaceDiscoveryParameters parameters) {
        super(converter, parameters);
    }

    @Override
    public PlaceDiscoveryResult getPlaces(XLog log) {
        InputDataBundle input = InputDataBundle.process(log, parameters.getConfigBundle().getInputProcessingConfig());
        SPECpp<org.processmining.specpp.datastructures.petri.Place, BasePlaceComposition, CollectionOfPlaces, ProMPetrinetWrapper> specpp =
                SPECpp.build(parameters.getConfigBundle(), input);

        ExecutionEnvironment.SPECppExecution<org.processmining.specpp.datastructures.petri.Place, BasePlaceComposition, CollectionOfPlaces, ProMPetrinetWrapper> execution;
        try (ExecutionEnvironment ee = new ExecutionEnvironment(Runtime.getRuntime().availableProcessors())) {
            execution = ee.execute(specpp, ExecutionParameters.timeouts(
                    new ExecutionParameters.ExecutionTimeLimits(
                            Duration.ofMinutes(5), Duration.ofMinutes(1), Duration.ofMinutes(60))));

            ee.join();
            CollectionOfPlaces specppPlaces = execution.getSPECpp().getInitialResult();
            System.out.println(specppPlaces.size());

            return getStandardPlaceDiscoveryResult(log, specppPlaces);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static StandardPlaceDiscoveryResult getStandardPlaceDiscoveryResult(XLog log, CollectionOfPlaces specppPlaces) {
        StandardPlaceDiscoveryResult result = new StandardPlaceDiscoveryResult();
        Set<Place> placeSet = new HashSet<>();
        specppPlaces.getPlaces().forEach(p -> {
            Place place = new Place();
            p.preset().forEach(t -> place.addInputTransition(new Transition(t.toString(), false)));
            p.postset().forEach(t -> place.addOutputTransition(new Transition(t.toString(), false)));
            placeSet.add(place);
        });
        result.setPlaces(placeSet);
        result.setLog(log);
        return result;
    }
}
