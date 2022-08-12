package org.processmining.placebasedlpmdiscovery.plugins.visualization.components;

import org.processmining.framework.util.ui.widgets.ProMComboBox;

import javax.swing.*;
import javax.swing.plaf.BorderUIResource;
import java.awt.*;
import java.awt.event.ItemEvent;

public class SettablePanelContainer extends JPanel {

    private JPanel header;
    private JPanel content;

    private SettableComponentFactory settableComponentsFactory;

    public SettablePanelContainer(SettableComponentFactory settableComponentsFactory) {
        this.settableComponentsFactory = settableComponentsFactory;

        this.setLayout(new BorderLayout());
        this.setBorder(BorderUIResource.getEtchedBorderUIResource());

        this.header = new JPanel();
        this.header.setLayout(new FlowLayout());
        ProMComboBox<ComponentId.Type> possiblePanelsComp = getChoosePanelComponent();
        this.header.add(possiblePanelsComp);
        this.add(this.header, BorderLayout.PAGE_START);

        this.content = new JPanel();
        this.content.setLayout(new BoxLayout(this.content, BoxLayout.X_AXIS));
        this.add(this.content, BorderLayout.CENTER);
    }

    private ProMComboBox<ComponentId.Type> getChoosePanelComponent() {
        ProMComboBox<ComponentId.Type> possiblePanelsComp = new ProMComboBox<>(new ComponentId.Type[]{
                ComponentId.Type.Empty,
                ComponentId.Type.LogStatistics,
                ComponentId.Type.BasicLPMEvalMetrics
        });
        possiblePanelsComp.setPreferredSize(new Dimension(new Dimension(200, 30)));
        possiblePanelsComp.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (e.getItem() instanceof ComponentId.Type) {
                    getNewContentForType((ComponentId.Type) e.getItem());
                }
            }
        });
        return possiblePanelsComp;
    }

    private void getNewContentForType(ComponentId.Type type) {
        if (this.content.getComponents().length > 0) {
            this.content.remove(0);
        }
        this.content.add(this.settableComponentsFactory.getComponent(type));
        this.content.revalidate();
    }
}
