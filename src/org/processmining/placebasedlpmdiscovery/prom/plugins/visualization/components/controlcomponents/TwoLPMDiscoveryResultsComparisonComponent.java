package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.controlcomponents;

import org.processmining.placebasedlpmdiscovery.view.listeners.MultipleLPMDiscoveryResultsViewListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class TwoLPMDiscoveryResultsComparisonComponent extends JPanel {

    private final Map<ButtonId, JButton> buttons;

    public TwoLPMDiscoveryResultsComparisonComponent(MultipleLPMDiscoveryResultsViewListener listener) {
        buttons = new HashMap<>();

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel infoPanel = new JPanel();
        this.add(infoPanel);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        JButton btn = new JButton("LPM1");
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deselectButtons();
                buttons.get(ButtonId.SINGLE1).setSelected(true);
                listener.selectFirstSet();
            }
        });
        controlPanel.add(btn);
        buttons.put(ButtonId.SINGLE1, btn);

        btn = new JButton("LPM2");
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deselectButtons();
                buttons.get(ButtonId.SINGLE2).setSelected(true);
                listener.selectSecondSet();
            }
        });
        controlPanel.add(btn);
        buttons.put(ButtonId.SINGLE2, btn);

        btn = new JButton("LPM1 ∩ LPM2");
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deselectButtons();
                buttons.get(ButtonId.INTERSECTION).setSelected(true);
                listener.selectIntersection();
            }
        });
        controlPanel.add(btn);
        buttons.put(ButtonId.INTERSECTION, btn);

        btn = new JButton("LPM1 ∪ LPM2");
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deselectButtons();
                buttons.get(ButtonId.UNION).setSelected(true);
                listener.selectUnion();
            }
        });
        btn.setSelected(true);
        controlPanel.add(btn);
        buttons.put(ButtonId.UNION, btn);

        btn = new JButton("LPM1 \\ LPM2");
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deselectButtons();
                buttons.get(ButtonId.DIFF1).setSelected(true);
                listener.selectOnlyInFirstSet();
            }
        });
        controlPanel.add(btn);
        buttons.put(ButtonId.DIFF1, btn);

        btn = new JButton("LPM2 \\ LPM1");
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deselectButtons();
                buttons.get(ButtonId.DIFF2).setSelected(true);
                listener.selectOnlyInSecondSet();
            }
        });
        controlPanel.add(btn);
        buttons.put(ButtonId.DIFF2, btn);

        this.add(controlPanel);
    }

    private void deselectButtons() {
        for (JButton btn : buttons.values()) {
            btn.setSelected(false);
        }
    }

    private enum ButtonId {
        INTERSECTION,
        UNION,
        DIFF1,
        DIFF2,
        SINGLE1,
        SINGLE2
    }
}
