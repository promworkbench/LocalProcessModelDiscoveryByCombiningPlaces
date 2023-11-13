package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components;

import org.apache.commons.math3.util.Pair;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.tables.factories.LPMResultPluginVisualizerTableFactory;
import org.processmining.placebasedlpmdiscovery.view.controllers.LPMDiscoveryResultViewController;
import org.processmining.placebasedlpmdiscovery.view.listeners.LPMDiscoveryResultViewListener;
import org.processmining.placebasedlpmdiscovery.view.models.LPMDiscoveryResultViewModel;
import org.processmining.placebasedlpmdiscovery.view.views.LPMDiscoveryResultView;

public class DefaultLPMDiscoveryResultComponent extends BaseLPMDiscoveryResultComponent implements LPMDiscoveryResultView {

    private LPMDiscoveryResultViewListener listener;
    private final PluginContext context;

    public DefaultLPMDiscoveryResultComponent(PluginContext context) {
        super(3);
        this.context = context;
    }

    @Override
    public void setListener(LPMDiscoveryResultViewController listener) {
        this.listener = listener;
    }

    @Override
    public void display(LPMDiscoveryResultViewModel model) {
        this.tablePanel.add(new SimpleCollectionOfElementsComponent<>(
                this.context, model.getLPMs(), new LPMResultPluginVisualizerTableFactory(), this.listener));

        displaySelectedLPM(model);
    }

    public void displaySelectedLPM(LPMDiscoveryResultViewModel model) {
        if (model.getSelectedLPM() != null) {
            for (SettablePanelContainer c : this.settablePanels.values()) {
                c.removeAll();
                c.add(ComponentFactory.getComplexEvaluationResultComponent(
                        model.getSelectedLPM().getAdditionalInfo().getEvalResults().values()));
            }
        }
    }
}
