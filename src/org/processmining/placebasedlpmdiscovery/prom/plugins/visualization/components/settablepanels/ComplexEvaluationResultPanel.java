package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.settablepanels;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResult;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Map;

public class ComplexEvaluationResultPanel extends JPanel {

    private final DefaultTableModel evalResTableModel;
    private Map<String, LPMEvaluationResult> model;

    public ComplexEvaluationResultPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.evalResTableModel = new DefaultTableModel(new Object[] { "Measure", "Value" }, 0);
        JTable evalResTable = new JTable(evalResTableModel);
        JScrollPane js = new JScrollPane(evalResTable);
        this.add(js);
    }

    public void setModel(Map<String, LPMEvaluationResult> model) {
        this.model = model;
        updateView();
    }

    private void updateView() {
        this.evalResTableModel.setRowCount(0);
        for (String evalResKey : this.model.keySet()) {
            LPMEvaluationResult res = this.model.get(evalResKey);
            if (res.getResult() == res.getNormalizedResult()) {
                this.evalResTableModel.addRow(
                        new Object[]{evalResKey, this.model.get(evalResKey).getResult()});
            } else {
                this.evalResTableModel.addRow(
                        new Object[]{evalResKey, this.model.get(evalResKey).getResult()});
                this.evalResTableModel.addRow(
                        new Object[]{evalResKey + " - Normalized", this.model.get(evalResKey).getNormalizedResult()});
            }
        }
    }
}
