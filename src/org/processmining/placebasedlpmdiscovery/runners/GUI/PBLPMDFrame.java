package org.processmining.placebasedlpmdiscovery.runners.GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;

public abstract class PBLPMDFrame extends JFrame {

    protected JPanel contentPane;

    public PBLPMDFrame() throws IOException {
        initFrame();
    }

    protected void initFrame() throws IOException {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 600));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        contentPane.add(getComponent(), BorderLayout.CENTER);
    }

    protected abstract JComponent getComponent() throws IOException;

}
