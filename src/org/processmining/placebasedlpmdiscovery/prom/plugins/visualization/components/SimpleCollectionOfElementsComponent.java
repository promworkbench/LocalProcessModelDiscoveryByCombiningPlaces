package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components;

import org.processmining.framework.plugin.PluginContext;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.TextDescribable;
import org.processmining.placebasedlpmdiscovery.model.serializable.LPMResult;
import org.processmining.placebasedlpmdiscovery.model.serializable.PlaceSet;
import org.processmining.placebasedlpmdiscovery.model.serializable.SerializableCollection;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.tables.TableComposition;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.tables.TableListener;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.tables.factories.PluginVisualizerTableFactory;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.visualizers.PlaceVisualizer;
import org.processmining.placebasedlpmdiscovery.view.components.LPMDisplayComponent;
import org.processmining.placebasedlpmdiscovery.view.components.lpmsetdisplay.LPMSetDisplayComponent;
import org.processmining.placebasedlpmdiscovery.view.components.lpmdisplay.LPMPetriNetComponent;
import org.processmining.placebasedlpmdiscovery.view.components.placesetdisplay.PlaceSetDisplayComponent;
import org.processmining.placebasedlpmdiscovery.view.listeners.NewElementSelectedListener;
import org.processmining.plugins.utils.ProvidedObjectHelper;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.Collection;

public class SimpleCollectionOfElementsComponent<T extends TextDescribable & Serializable>
        extends JComponent implements TableListener<T>, ComponentListener, LPMSetDisplayComponent, PlaceSetDisplayComponent {

    private final PluginContext context;
    private final Collection<T> result;
    private final PluginVisualizerTableFactory<T> tableFactory;
    private final NewElementSelectedListener<T> newElementSelectedListener;

    private JComponent visualizerComponent;

    public SimpleCollectionOfElementsComponent(PluginContext context,
                                               Collection<T> result,
                                               PluginVisualizerTableFactory<T> tableFactory,
                                               NewElementSelectedListener<T> newElementSelectedListener) {
        this.context = context;
        this.result = result;
        this.tableFactory = tableFactory;
        this.newElementSelectedListener = newElementSelectedListener;
        init();
    }

    private void init() {
        // set up the layout of this component
        this.setLayout(new BorderLayout());

        // create the table and LPM visualization containers
        visualizerComponent = createVisualizerComponent();
        JComponent tableContainer = new TableComposition<>(this.result, this.tableFactory, this);

        // set the preferred dimension of the two containers
//        int windowHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
//        int windowWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
//        tableContainer.setPreferredSize(new Dimension(15 * windowWidth / 100, windowHeight));
//        visualizerComponent.setPreferredSize(new Dimension(80 * windowWidth / 100, windowHeight));

        // add the table and LPM visualization containers and add some space between them
        this.add(tableContainer, BorderLayout.LINE_START);
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
        this.newElementSelectedListener.newLPMSelected(selectedObject);
        // if in the visualizer component there is an LPM drawn
        if (visualizerComponent.getComponents().length >= 1)
            visualizerComponent.remove(0); // remove it

        if (selectedObject instanceof LocalProcessModel) {
            // add Petri net component for the newly selected LPM
            LocalProcessModel lpm = (LocalProcessModel) selectedObject;
            LPMDisplayComponent lpmDisplayComponent = new LPMPetriNetComponent(lpm);
            visualizerComponent.add(
                    lpmDisplayComponent.getComponent(),
                    BorderLayout.CENTER);
        }

        if (selectedObject instanceof Place) {
            // create the visualizer
            PlaceVisualizer visualizer = new PlaceVisualizer();
            // add visualization for the newly selected Place
            Place place = (Place) selectedObject;
            visualizerComponent.add(
                    visualizer.createPlaceNetDisplayComponent(place),
                    BorderLayout.CENTER);
        }

        visualizerComponent.revalidate(); // revalidate the component
    }

    @Override
    public void export(SerializableCollection<T> collection) {
        if (collection instanceof LPMResult) {
            LPMResult lpmResult = (LPMResult) collection;
            context.getProvidedObjectManager()
                    .createProvidedObject("Collection exported from LPM Discovery plugin", lpmResult, LPMResult.class, context);
            ProvidedObjectHelper.setFavorite(context, lpmResult);
        }

        if (collection instanceof PlaceSet) {
            PlaceSet places = (PlaceSet) collection;
            context.getProvidedObjectManager()
                    .createProvidedObject("Collection exported from LPM Discovery plugin", places, PlaceSet.class, context);
            ProvidedObjectHelper.setFavorite(context, places);
        }
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
}
