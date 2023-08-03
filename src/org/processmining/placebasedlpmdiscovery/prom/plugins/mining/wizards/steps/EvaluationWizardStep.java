package org.processmining.placebasedlpmdiscovery.prom.plugins.mining.wizards.steps;//package org.processmining.placebasedlpmdiscovery.plugins.mining.wizards.steps;
//
//import com.fluxicon.slickerbox.components.NiceDoubleSlider;
//import com.fluxicon.slickerbox.components.NiceIntegerSlider;
//import com.fluxicon.slickerbox.components.NiceSlider;
//import com.fluxicon.slickerbox.factory.SlickerFactory;
//import org.processmining.framework.util.ui.widgets.ProMPropertiesPanel;
//import org.processmining.framework.util.ui.wizard.ProMWizardStep;
//import org.processmining.placebasedlpmdiscovery.plugins.mining.PlaceBasedLPMDiscoveryParameters;
//
//import javax.swing.*;
//
//public class EvaluationWizardStep extends ProMPropertiesPanel implements ProMWizardStep<PlaceBasedLPMDiscoveryParameters> {
//
//    private static final String TITLE = "Evaluation Configuration";
//
//    private NiceIntegerSlider windowSizeSlider;
//    private NiceDoubleSlider evalThresholdSlider;
//
//    public EvaluationWizardStep() {
//        super(TITLE);
//
//        // window size slider
//        windowSizeSlider = SlickerFactory.instance()
//                .createNiceIntegerSlider("",
//                        4, 10, 7, NiceSlider.Orientation.HORIZONTAL);
//        addProperty("Window Size", windowSizeSlider);
//
//        // evaluation threshold slider
//        evalThresholdSlider = SlickerFactory.instance()
//                .createNiceDoubleSlider("",
//                        0, 1, 0.2, NiceSlider.Orientation.HORIZONTAL);
//        addProperty("Evaluation Threshold", evalThresholdSlider);
//    }
//
//    @Override
//    public PlaceBasedLPMDiscoveryParameters apply(PlaceBasedLPMDiscoveryParameters placeBasedLPMDiscoveryParameters, JComponent jComponent) {
//        if (!canApply(placeBasedLPMDiscoveryParameters, jComponent))
//            return placeBasedLPMDiscoveryParameters;
//        placeBasedLPMDiscoveryParameters.getLpmEvaluationParameters().setWindowSize(windowSizeSlider.getValue());
//        placeBasedLPMDiscoveryParameters.getLpmFilterParameters().setAboveWindowsEvaluationThreshold(evalThresholdSlider.getValue());
//        return placeBasedLPMDiscoveryParameters;
//    }
//
//    @Override
//    public boolean canApply(PlaceBasedLPMDiscoveryParameters placeBasedLPMDiscoveryParameters, JComponent jComponent) {
//        return jComponent instanceof EvaluationWizardStep;
//    }
//
//    @Override
//    public JComponent getComponent(PlaceBasedLPMDiscoveryParameters placeBasedLPMDiscoveryParameters) {
//        return this;
//    }
//
//    @Override
//    public String getTitle() {
//        return TITLE;
//    }
//}
