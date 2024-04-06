package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.settablepanels;

import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.ComponentId;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Random;

public class SettablePanelContainer extends JPanel {

    private ComponentId componentId;
    private JPanel header;
    private JPanel content;

    public SettablePanelContainer() {
        this(new ComponentId(ComponentId.Type.Empty));
    }

    public SettablePanelContainer(ComponentId componentId) {
        this.componentId = componentId;

//        int windowHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
//        int windowWidth = Toolkit.getDefaultToolkit().getScreenSize().width;

        this.setLayout(new BorderLayout());
        this.setBorder(new EmptyBorder(5, 5, 5, 5));
        Random random = new Random();
        this.setBackground(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()));
//        this.setMaximumSize(new Dimension(25 * windowWidth / 100, 50 * windowHeight / 100));

//        this.header = new JPanel();
//        this.header.setLayout(new FlowLayout());
//        ProMComboBox<ComponentId.Type> possiblePanelsComp = getChoosePanelComponent();
//        this.header.add(possiblePanelsComp);
//        this.add(this.header, BorderLayout.PAGE_START);

//        this.content = new JPanel();
//        this.content.setMaximumSize(new Dimension(25 * windowWidth / 100, 45 * windowHeight / 100));
//        this.content.setMinimumSize(new Dimension(25 * windowWidth / 100, 45 * windowHeight / 100));
//        this.content.setPreferredSize(new Dimension(25 * windowWidth / 100, 45 * windowHeight / 100));
//        this.content.setLayout(new BorderLayout());
//        this.add(this.content, BorderLayout.CENTER);
    }

//    private ProMComboBox<ComponentId.Type> getChoosePanelComponent() {
//        ProMComboBox<ComponentId.Type> possiblePanelsComp = new ProMComboBox<>(new ComponentId.Type[]{
//                ComponentId.Type.Empty,
//                ComponentId.Type.LogStatistics,
//                ComponentId.Type.BasicLPMEvalMetrics
//        });
//        possiblePanelsComp.setPreferredSize(new Dimension(new Dimension(200, 30)));
//        possiblePanelsComp.addItemListener(e -> {
//            if (e.getStateChange() == ItemEvent.SELECTED) {
//                if (e.getItem() instanceof ComponentId.Type) {
//                    getNewContentForType((ComponentId.Type) e.getItem());
//                }
//            }
//        });
//        return possiblePanelsComp;
//    }
//
//    private void getNewContentForType(ComponentId.Type type) {
//        if (this.content.getComponents().length > 0) {
//            this.content.remove(0);
//        }
//        this.content.add(this.settableComponentsFactory.getComponent(type), BorderLayout.CENTER);
//        this.content.revalidate();
//    }

    public ComponentId getComponentId() {
        return componentId;
    }

    public void setComponentId(ComponentId componentId) {
        this.componentId = componentId;
    }

    public void setContent(JPanel content) {
        this.content = content;
        this.add(this.content);
    }

    public JPanel getContent() {
        return this.content;
    }
}
