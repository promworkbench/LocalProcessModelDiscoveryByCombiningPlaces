package org.processmining.placebasedlpmdiscovery.plugins.visualization.components;

import javax.swing.*;
import java.awt.*;

public class SettablePanelContainer extends JPanel {

    private JPanel header;
    private JPanel content;

    public SettablePanelContainer() {
        this.setLayout(new BorderLayout());

        this.header = new JPanel();
        this.header.setLayout(new FlowLayout());
        this.header.add(new Button("Choose"));
        this.add(this.header, BorderLayout.PAGE_START);

        this.content = new JPanel();
        this.add(this.content, BorderLayout.CENTER);
    }
}
