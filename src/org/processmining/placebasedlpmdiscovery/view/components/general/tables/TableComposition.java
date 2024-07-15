package org.processmining.placebasedlpmdiscovery.view.components.general.tables;

import org.processmining.placebasedlpmdiscovery.model.TextDescribable;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.ComponentId;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.ICommunicativePanel;
import org.processmining.placebasedlpmdiscovery.view.components.general.tables.factories.PluginVisualizerTableFactory;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.utils.RegexConverter;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.Serializable;
import java.util.Collection;

public class TableComposition<T extends TextDescribable & Serializable> extends JComponent implements ICommunicativePanel {

    private final ComponentId componentId;
    private final Collection<T> result;
    private final PluginVisualizerTableFactory<T> tableFactory;
    private final TableListener<T> controller;

    public TableComposition(Collection<T> result,
                            PluginVisualizerTableFactory<T> tableFactory,
                            TableListener<T> controller) {
        this.componentId = new ComponentId(ComponentId.Type.TableComponent);
        this.result = result;
        this.tableFactory = tableFactory;
        this.controller = controller;

        init();
    }

    private void init() {
        this.setLayout(new BorderLayout());

        // create the table
        GenericTextDescribableTableComponent<T> table = this.tableFactory.getPluginVisualizerTable(this.result, controller);
        JScrollPane scrollPane = new JScrollPane(table); // add the table in a scroll pane

        // create the filter form
        JPanel filterForm = new JPanel();
        filterForm.setLayout(new FlowLayout());
        filterForm.add(new JLabel("Filter:"));
        filterForm.add(createRowFilter(table));
        filterForm.setVisible(false);

        // make it expandable
        JToggleButton expandBtn = new JToggleButton(); // create an expand/shrink button
        expandBtn.setText("Expand"); // in the beginning set the text to Expand
        expandBtn.setSelected(false); // and selected to false
        // when the button state is selected the table is shown entirely and the text is Shrink,
        // and when is not selected only the first column is shown and the text is Expand
        expandBtn.addActionListener(actionEvent -> {
            if (expandBtn.isSelected()) { // if the new state is selected
                expandBtn.setText("Shrink"); // set the text to Shrink
                ((VisibilityControllableTableColumnModel) table.getColumnModel()).setAllColumnsVisible(); // show all columns
                controller.componentExpansion(componentId, true);
                filterForm.setVisible(true);
            } else { // otherwise
                expandBtn.setText("Expand"); // set the text to Expand
                ((VisibilityControllableTableColumnModel) table.getColumnModel()).keepOnlyFirstColumn(); // keep only the first column
                controller.componentExpansion(componentId, false);
                filterForm.setVisible(false);
            }
        });

        this.add(filterForm, BorderLayout.PAGE_START); // add the filter field in the table container
        this.add(scrollPane, BorderLayout.CENTER); // add the scroll pane in the table container
        this.add(expandBtn, BorderLayout.PAGE_END); // add the expand/shrink button in the table container
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

    @Override
    public ComponentId getComponentId() {
        return this.componentId;
    }
}
