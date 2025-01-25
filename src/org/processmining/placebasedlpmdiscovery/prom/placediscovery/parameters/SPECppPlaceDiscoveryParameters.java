package org.processmining.placebasedlpmdiscovery.prom.placediscovery.parameters;

import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.algorithms.PlaceDiscoveryAlgorithm;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.algorithms.PlaceDiscoveryAlgorithmFactory;
import org.processmining.specpp.componenting.data.ParameterRequirements;
import org.processmining.specpp.componenting.evaluation.EvaluatorConfiguration;
import org.processmining.specpp.componenting.system.GlobalComponentRepository;
import org.processmining.specpp.config.ConfigPresets;
import org.processmining.specpp.config.SPECppConfigBundle;
import org.processmining.specpp.config.components.Configurators;
import org.processmining.specpp.config.parameters.*;
import org.processmining.specpp.config.presets.BaseComponentConfig;
import org.processmining.specpp.evaluation.fitness.BaselineFitnessEvaluator;
import org.processmining.specpp.evaluation.markings.LogHistoryMaker;

public class SPECppPlaceDiscoveryParameters extends PlaceDiscoveryParameters {
    private final SPECppConfigBundle configBundle;

    public SPECppPlaceDiscoveryParameters() {
        this.configBundle = ConfigPresets.PLACE_ORACLE;
//        this.configBundle = ConfigFactory.create(
//                new PreProcessingParameters(new XEventNameClassifier(), true),
//                new DataExtractionParameters(Lexicographic.class),
//                new LPMDiscoverySPECppComponentConfig(),
//                new LPMDiscoverySPECppParameters(0.5),
//                CodeDefinedConfigurationSample.createSpecificParameters());
    }

    public SPECppConfigBundle getConfigBundle() {
        return configBundle;
    }

    @Override
    public PlaceDiscoveryAlgorithm<SPECppPlaceDiscoveryParameters, AcceptingPetriNet> getAlgorithm(PlaceDiscoveryAlgorithmFactory factory) {
        return factory.createPlaceDiscoveryAlgorithm(this);
    }

    private class LPMDiscoverySPECppParameters extends ParameterProvider {

        LPMDiscoverySPECppParameters(double tauThreshold) {
            globalComponentSystem().provide(ParameterRequirements.OUTPUT_PATH_PARAMETERS.fulfilWithStatic(OutputPathParameters.getDefault()))
                    .provide(ParameterRequirements.SUPERVISION_PARAMETERS.fulfilWithStatic(SupervisionParameters.getDefault()))
                    .provide(ParameterRequirements.TAU_FITNESS_THRESHOLDS.fulfilWithStatic(new TauFitnessThresholds(tauThreshold)))
                    .provide(ParameterRequirements.REPLAY_COMPUTATION.fulfilWithStatic(ReplayComputationParameters.getDefault()))
                    .provide(ParameterRequirements.IMPLICITNESS_TESTING.fulfilWithStatic(ImplicitnessTestingParameters.getDefault()))
                    .provide(ParameterRequirements.PLACE_GENERATOR_PARAMETERS.fulfilWithStatic(PlaceGeneratorParameters.getDefault()));
        }
    }

    private class LPMDiscoverySPECppComponentConfig extends BaseComponentConfig {
        @Override
        public EvaluatorConfiguration getEvaluatorConfiguration(GlobalComponentRepository gcr) {
            return Configurators.evaluators()
                    .addEvaluatorProvider(LogHistoryMaker::new)
                    .addEvaluatorProvider(new BaselineFitnessEvaluator.Builder())
                    .build(gcr);
        }
    }
}
