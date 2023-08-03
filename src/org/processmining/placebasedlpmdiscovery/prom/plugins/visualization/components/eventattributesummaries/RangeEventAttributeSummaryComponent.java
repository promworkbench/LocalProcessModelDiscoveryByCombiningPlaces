package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.eventattributesummaries;

import org.processmining.framework.util.ui.widgets.ProMTextField;
import org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary.RangeEventAttributeSummary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.ParseException;

public class RangeEventAttributeSummaryComponent extends EventAttributeSummaryComponent<RangeEventAttributeSummary<?,?>> {

    public RangeEventAttributeSummaryComponent(RangeEventAttributeSummary<?,?> model) {
        super(model);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setBackground(new Color(60, 60, 60, 160));

        this.add(new JLabel("min"));

        ProMTextField minValue = new ProMTextField(this.model.getStringMinValue());
        minValue.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
                try {
                    model.setMinValue(minValue.getText());
                } catch (ParseException e) {
                    keyEvent.consume();
                }
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

        ProMTextField maxValue = new ProMTextField(this.model.getStringMaxValue());
        maxValue.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
                try {
                    model.setMaxValue(maxValue.getText());
                } catch (ParseException e) {
                    keyEvent.consume();
                }
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
