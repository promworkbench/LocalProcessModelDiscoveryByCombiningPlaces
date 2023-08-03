package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.utils;

import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.InputKeeper;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

public class TextFieldIntFilter extends DocumentFilter {

    private final String NotInteger = "The entered input is not integer.";
    private final String SmallerThanMin = "The entered input is less than the minimal allowed.";
    private final String LargerThanMax = "The entered input is larger than the maximal allowed.";


    private InputKeeper inputKeeper;
    private int minValue;
    private int maxValue;
    private String currentValue;

    public TextFieldIntFilter(InputKeeper inputKeeper, String currentValue, int minValue, int maxValue) {
        this.inputKeeper = inputKeeper;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.currentValue = currentValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
        if (Integer.parseInt(this.currentValue) < this.minValue)
            this.inputKeeper.enteredInvalidInput(this.SmallerThanMin);
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
        if (Integer.parseInt(this.currentValue) > this.maxValue)
            this.inputKeeper.enteredInvalidInput(this.LargerThanMax);
    }

    private boolean testNotInteger(String text) {
        try {
            Integer.parseInt(text);
            return false;
        } catch (NumberFormatException e) {
            return text.length() != 0;
        }
    }

    @Override
    public void remove(FilterBypass fb, int offset, int length)
            throws BadLocationException {
        Document doc = fb.getDocument();
        StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.delete(offset, offset + length);

        if (testNotInteger(sb.toString())) {
            this.inputKeeper.enteredInvalidInput(this.NotInteger);
        } else if (Integer.parseInt(sb.toString()) < this.minValue) {
            this.inputKeeper.enteredInvalidInput(this.SmallerThanMin);
        } else if (Integer.parseInt(sb.toString()) > this.maxValue) {
            this.inputKeeper.enteredInvalidInput(this.LargerThanMax);
        } else {
            super.remove(fb, offset, length);
            this.currentValue = sb.toString();
        }
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string,
                             AttributeSet attr) throws BadLocationException {

        Document doc = fb.getDocument();
        StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.insert(offset, string);

        if (testNotInteger(sb.toString())) {
            this.inputKeeper.enteredInvalidInput(this.NotInteger);
        } else if (Integer.parseInt(sb.toString()) < this.minValue) {
            this.inputKeeper.enteredInvalidInput(this.SmallerThanMin);
        } else if (Integer.parseInt(sb.toString()) > this.maxValue) {
            this.inputKeeper.enteredInvalidInput(this.LargerThanMax);
        } else {
            super.insertString(fb, offset, string, attr);
            this.currentValue = sb.toString();
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text,
                        AttributeSet attrs) throws BadLocationException {

        Document doc = fb.getDocument();
        StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.replace(offset, offset + length, text);

        if (testNotInteger(sb.toString())) {
            this.inputKeeper.enteredInvalidInput(this.NotInteger);
        } else if (Integer.parseInt(sb.toString()) < this.minValue) {
            this.inputKeeper.enteredInvalidInput(this.SmallerThanMin);
        } else if (Integer.parseInt(sb.toString()) > this.maxValue) {
            this.inputKeeper.enteredInvalidInput(this.LargerThanMax);
        } else {
            super.replace(fb, offset, length, text, attrs);
            this.currentValue = sb.toString();
        }

    }
}

