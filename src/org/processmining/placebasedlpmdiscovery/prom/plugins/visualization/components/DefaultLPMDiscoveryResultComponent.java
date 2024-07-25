package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components;

import com.google.inject.Inject;
import org.apache.commons.math3.util.Pair;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.settablepanels.ComplexEvaluationResultPanel;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.settablepanels.SettablePanelContainer;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.settablepanels.SettablePanelFactory;
import org.processmining.placebasedlpmdiscovery.view.components.ComponentFactory;
import org.processmining.placebasedlpmdiscovery.view.components.lpmsetdisplay.LPMSetDisplayComponent;
import org.processmining.placebasedlpmdiscovery.view.components.lpmsetdisplay.LPMSetDisplayComponentType;
import org.processmining.placebasedlpmdiscovery.view.controllers.LPMDiscoveryResultViewController;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.DataCommunicationControllerVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.datalisteners.DataListenerVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.EmittableDataTypeVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.EmittableDataVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.componentchange.LPMSetDisplayComponentChangeEmittableDataVM;
import org.processmining.placebasedlpmdiscovery.view.listeners.LPMDiscoveryResultViewListener;
import org.processmining.placebasedlpmdiscovery.view.models.DefaultLPMDiscoveryResultViewModel;
import org.processmining.placebasedlpmdiscovery.view.models.LPMDiscoveryResultViewModel;
import org.processmining.placebasedlpmdiscovery.view.views.LPMDiscoveryResultView;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DefaultLPMDiscoveryResultComponent extends BaseLPMDiscoveryResultComponent implements LPMDiscoveryResultView, DataListenerVM {

    private LPMDiscoveryResultViewListener listener;
    private final SettablePanelFactory settablePanelFactory;
    private final ComponentFactory componentFactory;
    private final DefaultLPMDiscoveryResultViewModel model;

    @Inject
    public DefaultLPMDiscoveryResultComponent(DataCommunicationControllerVM dcVM,
                                              SettablePanelFactory settablePanelFactory,
                                              ComponentFactory componentFactory,
                                              DefaultLPMDiscoveryResultViewModel model) {
        super(dcVM, 3);
        this.settablePanelFactory = settablePanelFactory;
        this.componentFactory = componentFactory;
        this.model = model;

        createDefaultPanels();
    }

    private void createDefaultPanels() {
        SettablePanelContainer container = this.settablePanels.get(new Pair<>(2, 0));
        container.setComponentId(new ComponentId(ComponentId.Type.BasicLPMEvalMetrics));
        container.setContent(this.settablePanelFactory.getSettablePanel(ComponentId.Type.BasicLPMEvalMetrics));

        container = this.settablePanels.get(new Pair<>(2, 1));
        container.setComponentId(new ComponentId(ComponentId.Type.Grouping));
        container.setContent(this.settablePanelFactory.getSettablePanel(ComponentId.Type.Grouping));
    }

    @Override
    @Inject
    public void setListener(LPMDiscoveryResultViewController listener) {
        this.listener = listener;
    }

    @Override
    public void display(LPMDiscoveryResultViewModel model) {
        LPMSetDisplayComponent lpmSetDisplayComponent =
                this.componentFactory.createLPMSetDisplayComponent(LPMSetDisplayComponentType.SimpleLPMsCollection,
                        model.getLPMs(), this.listener, new HashMap<>());
        this.lpmSetDisplayContainer.add(lpmSetDisplayComponent.getComponent());

        this.lpmSetDisplayContainer.setBorder(new TitledBorder("Simple LPM Collection"));

        displaySelectedLPM(model);
    }

    @Override
    public JComponent getComponent() {
        return this;
    }

    public void displaySelectedLPM(LPMDiscoveryResultViewModel model) {
        if (model.getSelectedLPM() == null) {
            return;
        }

//        SettablePanelContainer container = this.settablePanels.values()
//                .stream()
//                .filter(panel -> panel.getComponentId().getType().equals(ComponentId.Type.BasicLPMEvalMetrics))
//                .findFirst()
//                .orElse(this.settablePanels.get(new Pair<>(2, 1)));
//        container = container.getComponentId().getType().equals(ComponentId.Type.Empty) ?
//                container : this.settablePanels.values().stream().filter(panel -> panel.getComponentId().getType().equals(ComponentId.Type.Empty)).findFirst().get();

        for (SettablePanelContainer container : this.settablePanels.values()) {
            if (container.getComponentId().getType().equals(ComponentId.Type.BasicLPMEvalMetrics)) {
                ((ComplexEvaluationResultPanel) container.getContent()).setModel(model.getSelectedLPM().getAdditionalInfo().getEvaluationResults());
            }
        }
    }

    @Override
    public void receive(EmittableDataVM data) {
        if (data.getType().equals(EmittableDataTypeVM.LPMSetDisplayComponentChangeVM)) {
            LPMSetDisplayComponentChangeEmittableDataVM cData = (LPMSetDisplayComponentChangeEmittableDataVM) data;
            this.setLPMSetDisplayComponent(cData.getComponentType(), cData.getParameters());
        }
    }

    private void setLPMSetDisplayComponent(LPMSetDisplayComponentType componentType, Map<String, Object> parameters) {
        if (this.lpmSetDisplayContainer.getComponents().length > 0) {
            this.lpmSetDisplayContainer.remove(0);
        }

        if (componentType.equals(LPMSetDisplayComponentType.SimpleLPMsCollection)) {
            this.display(this.model);
        } else if (componentType.equals(LPMSetDisplayComponentType.GroupedLPMs)) {
            LPMSetDisplayComponent lpmSetDisplayComponent = this.componentFactory.createLPMSetDisplayComponent(
                    LPMSetDisplayComponentType.GroupedLPMs,
                    model.getLPMs(),
                    this.listener,
                    Collections.singletonMap("identifier", parameters.get("identifier")));
            this.lpmSetDisplayContainer.add(lpmSetDisplayComponent.getComponent());
            this.lpmSetDisplayContainer.setBorder(new TitledBorder("Grouped LPMs"));
        }
        this.revalidate();
    }
}
