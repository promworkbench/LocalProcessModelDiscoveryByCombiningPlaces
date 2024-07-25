package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.visualizers;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.models.graphbased.ViewSpecificAttributeMap;
import org.processmining.models.jgraph.ProMJGraphVisualizer;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.serializable.PlaceSet;
import org.processmining.placebasedlpmdiscovery.model.serializable.SerializableList;
import org.processmining.placebasedlpmdiscovery.utils.LocalProcessModelUtils;
import org.processmining.placebasedlpmdiscovery.view.components.ComponentFactory;
import org.processmining.placebasedlpmdiscovery.view.components.placesetdisplay.PlaceSetDisplayComponentType;
import org.processmining.placebasedlpmdiscovery.view.dependencyinjection.ViewGuiceModule;

import javax.swing.*;


@Plugin(name = "@0 Visualize Serializable Set",
        returnLabels = {"Visualized Serializable Set"},
        returnTypes = {JComponent.class},
        parameterLabels = {"Petri Net Array"})
@Visualizer
public class PlaceSetVisualizer {

    // TODO: This should be optimized instead of having duplicate code

    @PluginVariant(requiredParameterLabels = {0})
    public JComponent visualize(UIPluginContext context, PlaceSet result) {

        if (result.size() < 1)
            return new JPanel();

        Injector guice = Guice.createInjector(new ViewGuiceModule());
        ComponentFactory componentFactory = guice.getInstance(ComponentFactory.class);

        return componentFactory.createPlaceSetDisplayComponent(PlaceSetDisplayComponentType.SimplePlaceCollection,
                result.getElements()).getComponent();
//
//        return componentFactory.createLPMSetDisplayComponent(LPMSetDisplayComponentType.SimpleLPMsCollection,
//                result.getAllLPMs()).getComponent();


//        JComponent component = new JPanel();
//        component.setLayout(new GridBagLayout());
//        GridBagConstraints gbc = new GridBagConstraints();
//
//        // component for choosing
//        JComponent choosingComponent = new JPanel();
//        choosingComponent.setLayout(new BoxLayout(choosingComponent, BoxLayout.X_AXIS));
//
//        // error label
//        JLabel errorLabel = new JLabel();
//
//        // tabbed pane where the local process models are shown
//        JTabbedPane tabbedPane = new JTabbedPane();
//
//        // start index
//        choosingComponent.add(new JLabel("Start index:"));
//        JTextField txtStart = new JTextField("1");
//        PlainDocument txtStartDocument = (PlainDocument) txtStart.getDocument();
//        txtStartDocument.setDocumentFilter(new TextFieldIntFilter(errorLabel::setText, "1", 1, result.size()));
//        choosingComponent.add(txtStart);
//
//        // end index
//        choosingComponent.add(new JLabel("End index:"));
//        JTextField txtEnd = new JTextField("10");
//        PlainDocument txtEndDocument = (PlainDocument) txtEnd.getDocument();
//        txtEndDocument.setDocumentFilter(new TextFieldIntFilter(errorLabel::setText, "10", 1, result.size()));
//        choosingComponent.add(txtEnd);
//
//        // button
//        JButton btn = new JButton("Show");
//        btn.addActionListener(e -> {
//            int start = Integer.parseInt(txtStart.getText());
//            int end = Integer.parseInt(txtEnd.getText());
//            if (start > end)
//                errorLabel.setText("Start index is larger than end index");
//            else
//                refreshTabbedPane(tabbedPane, context, start, end, result.getList());
//        });
//        choosingComponent.add(btn);
//
//        gbc.gridx = gbc.gridy = 0;
//        gbc.gridwidth = gbc.gridheight = 1;
//        gbc.fill = GridBagConstraints.BOTH;
//        gbc.anchor = GridBagConstraints.NORTHWEST;
//
//        gbc.weightx = 100;
//        gbc.weighty = 5;
//        gbc.insets = new Insets(2, 2, 2, 2);
//        component.add(choosingComponent, gbc);
//
//        gbc.gridy = 1;
//        gbc.weightx = 100;
//        gbc.weighty = 5;
//        gbc.insets = new Insets(2, 2, 2, 2);
//        component.add(errorLabel, gbc);
//
//        gbc.gridy = 2;
//        gbc.weightx = 100;
//        gbc.weighty = 85;
//        gbc.insets = new Insets(2, 2, 2, 2);
//
//        refreshTabbedPane(tabbedPane, context, 1, Math.min(10, result.size()), result.getList());
//        component.add(tabbedPane, gbc);
//
//        return component;
    }

    private void refreshTabbedPane(JTabbedPane tabbedPane, UIPluginContext context, int start, int end,
                                   SerializableList<Place> result) {
        tabbedPane.removeAll();
        for (int index = start - 1; index < end; index++) {
            String label = "Place " + (index + 1);
            LocalProcessModel lpm = new LocalProcessModel(result.getElement(index));
            tabbedPane.add(label, ProMJGraphVisualizer.instance().visualizeGraph(context,
                    LocalProcessModelUtils.getAcceptingPetriNetRepresentation(lpm).getNet(),
                    new ViewSpecificAttributeMap()));
        }
    }
}
