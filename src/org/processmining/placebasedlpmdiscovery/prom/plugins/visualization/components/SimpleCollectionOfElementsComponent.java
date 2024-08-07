package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components;

import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.TextDescribable;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.visualizers.PlaceVisualizer;
import org.processmining.placebasedlpmdiscovery.view.components.general.tables.TableComposition;
import org.processmining.placebasedlpmdiscovery.view.components.general.tables.TableListener;
import org.processmining.placebasedlpmdiscovery.view.components.general.tables.factories.PluginVisualizerTableFactory;
import org.processmining.placebasedlpmdiscovery.view.components.lpmdisplay.LPMDisplayComponent;
import org.processmining.placebasedlpmdiscovery.view.components.lpmdisplay.LPMPetriNetComponent;
import org.processmining.placebasedlpmdiscovery.view.components.lpmsetdisplay.LPMSetDisplayComponent;
import org.processmining.placebasedlpmdiscovery.view.components.placesetdisplay.PlaceSetDisplayComponent;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.DataCommunicationControllerVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.datalisteners.DataListenerVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.EmittableDataTypeVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.EmittableDataVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.tableselection.NewLPMSelectedEmittableDataVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.tableselection.NewPlaceSelectedEmittableDataVM;
import org.processmining.placebasedlpmdiscovery.view.listeners.NewElementSelectedListener;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

public class SimpleCollectionOfElementsComponent<T extends TextDescribable & Serializable> extends JComponent implements TableListener<T>, ComponentListener, LPMSetDisplayComponent, PlaceSetDisplayComponent, DataListenerVM {

    private final Collection<T> result;
    private final PluginVisualizerTableFactory<T> tableFactory;
    private final NewElementSelectedListener<T> newElementSelectedListener;

    private final DataCommunicationControllerVM dcVM;

    // components
    private JComponent visualizerComponent;
    private TableComposition<T> tableComponent;

    public SimpleCollectionOfElementsComponent(Collection<T> result, PluginVisualizerTableFactory<T> tableFactory,
                                               NewElementSelectedListener<T> newElementSelectedListener,
                                               DataCommunicationControllerVM dcVM) {
        this.result = result;
        this.tableFactory = tableFactory;
        this.newElementSelectedListener = newElementSelectedListener;
        this.dcVM = dcVM;
        init();
    }

    private void init() {
        // set up the layout of this component
        this.setLayout(new BorderLayout());

        // create the table and LPM visualization containers
        visualizerComponent = createVisualizerComponent();
        tableComponent = new TableComposition<>(this.result, this.tableFactory, this, this.dcVM);
        this.dcVM.registerDataListener(this,
                String.join("/", EmittableDataTypeVM.NewLPMSelectedVM.name(), tableComponent.getId()));
        this.dcVM.registerDataListener(this,
                String.join("/", EmittableDataTypeVM.NewPlaceSelectedVM.name(), tableComponent.getId()));
        tableComponent.reselect();

        // set the preferred dimension of the two containers
//        int windowHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
//        int windowWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
//        tableContainer.setPreferredSize(new Dimension(15 * windowWidth / 100, windowHeight));
//        visualizerComponent.setPreferredSize(new Dimension(80 * windowWidth / 100, windowHeight));

        // add the table and LPM visualization containers and add some space between them
        this.add(tableComponent, BorderLayout.LINE_START);
//        this.add(Box.createRigidArea(new Dimension(windowWidth / 100, windowHeight)));
        this.add(visualizerComponent, BorderLayout.CENTER);
    }

    private JComponent createVisualizerComponent() {
        JComponent lpmVisualizerComponent = new JPanel();
        lpmVisualizerComponent.setLayout(new BorderLayout());
        return lpmVisualizerComponent;
    }

    @Override
    public void newSelection(T selectedObject) {
        if (selectedObject instanceof LocalProcessModel) {
            // add Petri net component for the newly selected LPM
            newLPMSelected((LocalProcessModel) selectedObject);
        }

        if (selectedObject instanceof Place) {
            // create the visualizer
            newPlaceSelected((Place) selectedObject);
        }
    }

    public void reselect() {
        this.tableComponent.reselect();
    }

    private void newPlaceSelected(Place selectedObject) {
        // if in the visualizer component there is a place drawn
        if (visualizerComponent.getComponents().length >= 1) visualizerComponent.remove(0); // remove it

        PlaceVisualizer visualizer = new PlaceVisualizer();
        // add visualization for the newly selected Place
        Place place = selectedObject;
        visualizerComponent.add(visualizer.createPlaceNetDisplayComponent(place), BorderLayout.CENTER);

        visualizerComponent.revalidate(); // revalidate the component
    }

    private void newLPMSelected(LocalProcessModel selectedObject) {
        // if in the visualizer component there is an LPM drawn
        if (visualizerComponent.getComponents().length >= 1) visualizerComponent.remove(0); // remove it

        LocalProcessModel lpm = selectedObject;
        LPMDisplayComponent lpmDisplayComponent = new LPMPetriNetComponent(lpm);
        visualizerComponent.add(lpmDisplayComponent.getComponent(), BorderLayout.CENTER);

        visualizerComponent.revalidate(); // revalidate the component
    }

    @Override
    public void componentExpansion(ComponentId componentId, boolean expanded) {
        // change visibility of lpm container
//        this.visualizerComponent.setVisible(!expanded);
    }

    @Override
    public JComponent getComponent() {
        return this;
    }

    @Override
    public void receive(EmittableDataVM data) {
        if (data instanceof NewLPMSelectedEmittableDataVM) {
            NewLPMSelectedEmittableDataVM cData = (NewLPMSelectedEmittableDataVM) data;
            this.newLPMSelected(cData.getLpm());
        } else if (data instanceof NewPlaceSelectedEmittableDataVM) {
            NewPlaceSelectedEmittableDataVM cData = (NewPlaceSelectedEmittableDataVM) data;
            this.newPlaceSelected(cData.getPlace());
        }
    }
}
