package org.processmining.placebasedlpmdiscovery.plugins.visualization.components;

import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.TextDescribable;
import org.processmining.placebasedlpmdiscovery.model.serializable.SerializableCollection;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.components.tables.TableComposition;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.components.tables.factories.AbstractPluginVisualizerTableFactory;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.visualizers.LocalProcessModelVisualizer;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.visualizers.PlaceVisualizer;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class SimpleCollectionOfElementsComponent<T extends TextDescribable & Serializable>
        extends JComponent implements WeirdComponentController<T> {

    private final UIPluginContext context;
    private final SerializableCollection<T> result;
    private final AbstractPluginVisualizerTableFactory<T> tableFactory;

    private JComponent visualizerComponent;
    private JComponent tableContainer;

    public SimpleCollectionOfElementsComponent(UIPluginContext context,
                                               SerializableCollection<T> result,
                                               AbstractPluginVisualizerTableFactory<T> tableFactory) {
        this.context = context;
        this.result = result;
        this.tableFactory = tableFactory;
        init();
    }

    private void init() {
        // set up the layout of this component
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        // create the table and LPM visualization containers
        visualizerComponent = createVisualizerComponent();
        tableContainer = new TableComposition<>(this.result, this.tableFactory, this);

        // set the preferred dimension of the two containers
        int windowHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        int windowWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        tableContainer.setPreferredSize(new Dimension(15 * windowWidth / 100, windowHeight));
        visualizerComponent.setPreferredSize(new Dimension(80 * windowWidth / 100, windowHeight));

        // add the table and LPM visualization containers and add some space between them
        this.add(tableContainer);
        this.add(Box.createRigidArea(new Dimension(windowWidth / 100, windowHeight)));
        this.add(visualizerComponent);
    }

    private JComponent createVisualizerComponent() {
        JComponent lpmVisualizerComponent = new JPanel();
        lpmVisualizerComponent.setLayout(new BorderLayout());
        return lpmVisualizerComponent;
    }

    @Override
    public void newSelection(T selectedObject) {
        // if in the visualizer component there is a LPM drawn
        if (visualizerComponent.getComponents().length >= 1)
            visualizerComponent.remove(0); // remove it

        if (selectedObject instanceof LocalProcessModel) {
            // create the visualizer
            LocalProcessModelVisualizer visualizer = new LocalProcessModelVisualizer();
            // add visualization for the newly selected LPM
            LocalProcessModel lpm = (LocalProcessModel) selectedObject;
            visualizerComponent.add(
                    visualizer.visualize(context, lpm),
                    BorderLayout.CENTER);
        }

        if (selectedObject instanceof Place) {
            // create the visualizer
            PlaceVisualizer visualizer = new PlaceVisualizer();
            // add visualization for the newly selected Place
            Place place = (Place) selectedObject;
            visualizerComponent.add(
                    visualizer.visualize(context, place),
                    BorderLayout.CENTER);
        }

        visualizerComponent.revalidate(); // revalidate the component
    }

    @Override
    public void componentExpansion(ComponentId componentId, boolean expanded) {
        // change visibility of lpm container
        this.visualizerComponent.setVisible(!expanded);
    }
}
