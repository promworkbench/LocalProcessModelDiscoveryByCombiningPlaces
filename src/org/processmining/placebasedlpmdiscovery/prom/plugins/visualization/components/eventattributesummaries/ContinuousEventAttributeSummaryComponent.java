package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.eventattributesummaries;

import org.processmining.framework.util.ui.widgets.ProMTextField;
import org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary.ContinuousAttributeSummary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ContinuousEventAttributeSummaryComponent extends EventAttributeSummaryComponent<ContinuousAttributeSummary> {

    public ContinuousEventAttributeSummaryComponent(ContinuousAttributeSummary model) {
        super(model);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setBackground(new Color(60, 60, 60, 160));

        this.add(new JLabel("min"));

        ProMTextField minValue = new ProMTextField(String.valueOf(this.model.getMinValue()));
        minValue.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
                if (!Character.isDigit(keyEvent.getKeyChar()) &&
                        !(keyEvent.getKeyCode() == KeyEvent.VK_BACK_SPACE) &&
                        !(keyEvent.getKeyCode() == KeyEvent.VK_DECIMAL)) {
                    keyEvent.consume();
                }
                model.setMinValue(Double.parseDouble(minValue.getText()));
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.isControlDown() && keyEvent.getKeyCode() == KeyEvent.VK_V) {
                    keyEvent.consume();
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        });
        this.add(minValue);

        this.add(new JLabel("max"));

        ProMTextField maxValue = new ProMTextField(String.valueOf(this.model.getMaxValue()));
        maxValue.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
                if (!Character.isDigit(keyEvent.getKeyChar()) &&
                        !(keyEvent.getKeyCode() == KeyEvent.VK_BACK_SPACE) &&
                        !(keyEvent.getKeyCode() == KeyEvent.VK_DECIMAL)) {
                    keyEvent.consume();
                }
                model.setMaxValue(Double.parseDouble(maxValue.getText()));
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.isControlDown() && keyEvent.getKeyCode() == KeyEvent.VK_V) {
                    keyEvent.consume();
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        });
        this.add(maxValue);
    }
}
