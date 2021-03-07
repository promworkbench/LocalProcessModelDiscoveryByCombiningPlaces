package org.processmining.placebasedlpmdiscovery.plugins.mining.wizards;//package org.processmining.placebasedlpmdiscovery.plugins.mining.wizards;
//
//import com.fluxicon.slickerbox.components.NiceDoubleSlider;
//import com.fluxicon.slickerbox.components.NiceIntegerSlider;
//import com.fluxicon.slickerbox.components.NiceSlider;
//import com.fluxicon.slickerbox.factory.SlickerFactory;
//import org.deckfour.uitopia.api.event.TaskListener;
//import org.processmining.placebasedlpmdiscovery.Main;
//import org.processmining.placebasedlpmdiscovery.placediscovery.PlaceDiscoveryAlgorithmId;
//import org.processmining.placebasedlpmdiscovery.plugins.mining.PlaceBasedLPMDiscoveryParameters;
//import org.processmining.placebasedlpmdiscovery.utils.Constants;
//import org.processmining.v7.forViki.MyMinerWizardStep;
//
//import javax.swing.*;
//import javax.swing.border.LineBorder;
//import javax.swing.border.TitledBorder;
//import java.awt.*;
//
//public class PlaceBasedLPMDiscoveryDialog extends JPanel {
//
//    private PlaceBasedLPMDiscoveryParameters parameters;
//
//
//    public PlaceBasedLPMDiscoveryDialog(PlaceBasedLPMDiscoveryParameters parameters) {
//        this.parameters = parameters;
//
//        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//
//        addPlaceGeneratorParameters();
//        add(Box.createRigidArea(new Dimension(0, 10)));
//        addLPMDiscoveryParameters();
//        add(Box.createRigidArea(new Dimension(0, 10)));
//        addEvaluationParameters();
//        add(Box.createVerticalGlue());
//
//    }
//
//    private void addPlaceGeneratorParameters() {
//        JPanel placeGeneratorAlgChoicePanel = new JPanel();
//        placeGeneratorAlgChoicePanel.setBackground(Constants.Visualization.Colors.TransparentColor);
//        placeGeneratorAlgChoicePanel.setLayout(new BoxLayout(placeGeneratorAlgChoicePanel, BoxLayout.X_AXIS));
//
//        // set border
//        placeGeneratorAlgChoicePanel
//                .setBorder(new TitledBorder(new LineBorder(Color.GRAY), "Place Generator Chooser"));
//
//        // add combo box
//        JComboBox<String> algComboBox = new JComboBox<>();
//        for (PlaceDiscoveryAlgorithmId algId : PlaceDiscoveryAlgorithmId.values())
//            algComboBox.addItem(algId.name());
//        algComboBox.setMaximumSize(new Dimension(algComboBox.getMaximumSize().width,
//                algComboBox.getPreferredSize().height));
//        algComboBox.addActionListener(e -> {
//            if (algComboBox.getSelectedItem() == null)
//                return;
//
//            parameters.setPlaceDiscoveryAlgorithmId(PlaceDiscoveryAlgorithmId
//                    .valueOf((String) algComboBox.getSelectedItem()));
//            openPlaceGeneratorParameterChooserDialog();
//        });
//
//        placeGeneratorAlgChoicePanel.add(algComboBox);
//
//        // add space between the components
//        placeGeneratorAlgChoicePanel.add(Box.createHorizontalGlue());
//
//        // add change parameters button
//        JButton btnChangeParameters = new JButton("Change Parameters");
//        btnChangeParameters.addActionListener(e -> openPlaceGeneratorParameterChooserDialog());
//        placeGeneratorAlgChoicePanel.add(btnChangeParameters);
//
//        add(placeGeneratorAlgChoicePanel);
//    }
//
//    private void openPlaceGeneratorParameterChooserDialog() {
//        switch (this.parameters.getPlaceDiscoveryAlgorithmId()) {
//            case ESTMiner:
//                MyMinerWizardStep wizardStep = new MyMinerWizardStep(parameters.getLog());
//                TaskListener.InteractionResult result = Main.getContext()
//                        .showWizard("Configure eST-Miner parameters", true, true, wizardStep);
//                if (result != TaskListener.InteractionResult.FINISHED)
//                    return;
//            case InductiveMiner:
//                break;
//            case HeuristicMiner:
//                break;
//            default:
//                throw new IllegalStateException("Unexpected value: " + this.parameters.getPlaceDiscoveryAlgorithmId());
//        }
//    }
//
//    private void addEvaluationParameters() {
//        JPanel evaluationParametersPanel = new JPanel();
//        evaluationParametersPanel.setBackground(Constants.Visualization.Colors.DialogBackgroundColor);
//        evaluationParametersPanel.setLayout(new BoxLayout(evaluationParametersPanel, BoxLayout.Y_AXIS));
//
//        // set border
//        evaluationParametersPanel
//                .setBorder(new TitledBorder(new LineBorder(Color.GRAY), "Evaluation Parameters"));
//
//
//        // window size slider
//        NiceIntegerSlider windowSizeSlider = SlickerFactory.instance()
//                .createNiceIntegerSlider("Choose size of window",
//                        4, 10, 7, NiceSlider.Orientation.HORIZONTAL);
//        windowSizeSlider.addChangeListener(e -> parameters.setWindowSize(windowSizeSlider.getValue()));
//        evaluationParametersPanel.add(windowSizeSlider);
//
//        // evaluation threshold slider
//        NiceDoubleSlider evalThresholdSlider = SlickerFactory.instance()
//                .createNiceDoubleSlider("Choose value of evaluation threshold",
//                        0, 1, 0.2, NiceSlider.Orientation.HORIZONTAL);
//        evalThresholdSlider.addChangeListener(e -> parameters.setEvalThreshold(evalThresholdSlider.getValue()));
//
//        evaluationParametersPanel.add(evalThresholdSlider);
//
//        add(evaluationParametersPanel);
//    }
//
//    private void addLPMDiscoveryParameters() {
//        JPanel lpmDiscoveryParametersPanel = new JPanel();
//        lpmDiscoveryParametersPanel.setBackground(Constants.Visualization.Colors.DialogBackgroundColor);
//        lpmDiscoveryParametersPanel.setLayout(new BoxLayout(lpmDiscoveryParametersPanel, BoxLayout.Y_AXIS));
//
//        // set border
//        lpmDiscoveryParametersPanel
//                .setBorder(new TitledBorder(new LineBorder(Color.GRAY), "LPM Discovery Parameters"));
//
//        // min and max places slider
//        NiceIntegerSlider minPlacesSlider = SlickerFactory.instance()
//                .createNiceIntegerSlider("Choose minimum number of places per local process model",
//                        1, 5, 2, NiceSlider.Orientation.HORIZONTAL);
//        NiceIntegerSlider maxPlacesSlider = SlickerFactory.instance()
//                .createNiceIntegerSlider("Choose maximum number of places per local process model",
//                        1, 5, 5, NiceSlider.Orientation.HORIZONTAL);
//
//        minPlacesSlider.addChangeListener(e -> {
//            if (minPlacesSlider.getValue() > maxPlacesSlider.getValue())
//                minPlacesSlider.setValue(maxPlacesSlider.getValue());
//            parameters.setMinNumPlace(minPlacesSlider.getValue());
//        });
//        maxPlacesSlider.addChangeListener(e -> {
//            if (maxPlacesSlider.getValue() < minPlacesSlider.getValue())
//                maxPlacesSlider.setValue(minPlacesSlider.getValue());
//            parameters.setMaxNumPlaces(maxPlacesSlider.getValue());
//        });
//
//        lpmDiscoveryParametersPanel.add(minPlacesSlider, "0, 2");
//        lpmDiscoveryParametersPanel.add(maxPlacesSlider, "0, 3");
//
//        add(lpmDiscoveryParametersPanel);
//    }
//}
