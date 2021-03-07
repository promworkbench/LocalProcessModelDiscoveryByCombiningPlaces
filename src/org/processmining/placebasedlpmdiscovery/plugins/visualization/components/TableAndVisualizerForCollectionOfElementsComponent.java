package org.processmining.placebasedlpmdiscovery.plugins.visualization.components;

import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.placebasedlpmdiscovery.model.TextDescribable;
import org.processmining.placebasedlpmdiscovery.model.serializable.SerializableCollection;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.components.tables.PluginVisualizerTable;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.components.tables.PluginVisualizerTableColumnModel;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.components.tables.factories.AbstractPluginVisualizerTableFactory;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.utils.RegexConverter;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.Serializable;

public class TableAndVisualizerForCollectionOfElementsComponent<T extends TextDescribable & Serializable>
        extends JComponent {

    private final UIPluginContext context;
    private final SerializableCollection<T> result;
    private final AbstractPluginVisualizerTableFactory<T> tableFactory;

    public TableAndVisualizerForCollectionOfElementsComponent(UIPluginContext context,
                                                              SerializableCollection<T> result,
                                                              AbstractPluginVisualizerTableFactory<T> tableFactory) {
        this.context = context;
        this.result = result;
        this.tableFactory = tableFactory;
        init();
    }

    private void init() {
        // setup the layout of this component
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        // create the table and LPM visualization containers
        JComponent lpmVisualizerComponent = createVisualizerComponent();
        JComponent tableContainer = createTableContainer(lpmVisualizerComponent);

        // set the preferred dimension of the two containers
        int windowHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        int windowWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        tableContainer.setPreferredSize(new Dimension(15 * windowWidth / 100, windowHeight));
        lpmVisualizerComponent.setPreferredSize(new Dimension(80 * windowWidth / 100, windowHeight));

        // add the table and LPM visualization containers and add some space between them
        this.add(tableContainer);
        this.add(Box.createRigidArea(new Dimension(windowWidth / 100, windowHeight)));
        this.add(lpmVisualizerComponent);
    }

    private JComponent createVisualizerComponent() {
        JComponent lpmVisualizerComponent = new JPanel();
        lpmVisualizerComponent.setLayout(new BorderLayout());
        return lpmVisualizerComponent;
    }

    private JComponent createTableContainer(JComponent lpmVisualizerComponent) {
        JComponent tableContainer = new JPanel();
        tableContainer.setLayout(new BorderLayout());

        // create the table
        PluginVisualizerTable<T> table = this.tableFactory.getPluginVisualizerTable(
                this.result, lpmVisualizerComponent, context); // create the table
        JScrollPane scrollPane = new JScrollPane(table); // add the table in a scroll pane

        // create the filter form
        JPanel filterForm = new JPanel();
        filterForm.setLayout(new FlowLayout());
        filterForm.add(new JLabel("Filter:"));
        filterForm.add(createRowFilter(table));
        filterForm.setVisible(false);

        JToggleButton expandBtn = new JToggleButton(); // create an expand/shrink button
        expandBtn.setText("Expand"); // in the beginning set the text to Expand
        expandBtn.setSelected(false); // and selected to false
        // when the button state is selected the table is shown entirely and the text is Shrink,
        // and when is not selected only the first column is shown and the text is Expand
        expandBtn.addActionListener(actionEvent -> {
            if (expandBtn.isSelected()) { // if the new state is selected
                expandBtn.setText("Shrink"); // set the text to Shrink
                ((PluginVisualizerTableColumnModel) table.getColumnModel()).setAllColumnsVisible(); // show all columns
                lpmVisualizerComponent.setVisible(false); // and make the lpm visualization to invisible
                filterForm.setVisible(true);
            } else { // otherwise
                expandBtn.setText("Expand"); // set the text to Expand
                ((PluginVisualizerTableColumnModel) table.getColumnModel()).keepOnlyFirstColumn(); // keep only the first column
                lpmVisualizerComponent.setVisible(true); // and make the lpm visualization to visible
                filterForm.setVisible(false);
            }
        });

        tableContainer.add(filterForm, BorderLayout.PAGE_START); // add the filter field in the table container
        tableContainer.add(scrollPane, BorderLayout.CENTER); // add the scroll pane in the table container
        tableContainer.add(expandBtn, BorderLayout.PAGE_END); // add the expand/shrink button in the table container
        return tableContainer;
    }

    /**
     * Creating the text filed for the table filter
     *
     * @param table: the table for which the filter is created
     * @return created text field that will be used to filer the values in the table
     */
    private JTextField createRowFilter(JTable table) {
        RowSorter<? extends TableModel> rs = table.getRowSorter();
        if (rs == null) {
            table.setAutoCreateRowSorter(true);
            rs = table.getRowSorter();
        }

        TableRowSorter<? extends TableModel> rowSorter =
                (rs instanceof TableRowSorter) ? (TableRowSorter<? extends TableModel>) rs : null;

        if (rowSorter == null) {
            throw new RuntimeException("Cannot find appropriate rowSorter: " + rs);
        }

        final JTextField tf = new JTextField(50);
        tf.setToolTipText("Available operators are: 'and', 'or' and '{}'. It doesn't support nested parenthesis.");

        tf.addActionListener(actionEvent -> {
            String text = tf.getText();
            RegexConverter regexConverter = new RegexConverter();
            String regex = regexConverter.getRegex(text);
            System.out.println(regex);
            if (text.trim().length() == 0) {
                rowSorter.setRowFilter(null);
            } else {
                rowSorter.setRowFilter(RowFilter.regexFilter(regex, 1));
            }
        });

        return tf;
    }
}
