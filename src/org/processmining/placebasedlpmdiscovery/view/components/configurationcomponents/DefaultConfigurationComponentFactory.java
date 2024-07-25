package org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents;

import com.google.inject.Inject;
import org.apache.commons.lang.NotImplementedException;
import org.processmining.placebasedlpmdiscovery.grouping.GroupingController;
import org.processmining.placebasedlpmdiscovery.service.eventlog.EventLogService;
import org.processmining.placebasedlpmdiscovery.service.lpms.LPMSetService;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.configurations.StandardConfigurationComponentType;
import org.processmining.placebasedlpmdiscovery.view.components.settablepanels.grouping.GroupingSetupPanel;
import org.processmining.placebasedlpmdiscovery.view.components.settablepanels.grouping.lpmsimilarity.*;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.DataCommunicationControllerVM;


public class DefaultConfigurationComponentFactory implements ConfigurationComponentFactory {

    private final DataCommunicationControllerVM dcVM;
    private final LPMSetService lpmSetService;
    private final EventLogService eventLogService;
    private final GroupingController groupingController;
    @Inject
    public DefaultConfigurationComponentFactory(DataCommunicationControllerVM dcVM,
                                                LPMSetService lpmSetService,
                                                EventLogService eventLogService,
                                                GroupingController groupingController) {
        this.dcVM = dcVM;
        this.lpmSetService = lpmSetService;
        this.eventLogService = eventLogService;
        this.groupingController = groupingController;
    }

    @Override
    public ConfigurationComponent create(ConfigurationComponentType type) {
        if (type.name().equals(StandardConfigurationComponentType.GroupingConfigurationComponent.name())) {
            return new GroupingSetupPanel(this.dcVM, this.lpmSetService, this.groupingController, this);
        } else if (type.name().equals(StandardConfigurationComponentType.LPMSimilarityConfigurationComponent.name())) {
            return new LPMSimilarityChooserPanel(this);
        }
        throw new NotImplementedException("The creation for type " + type.name() + " is not implemented yet.");
    }

    @Override
    public LPMSimilaritySetupComponent create(LPMSimilarityConfigurationComponentType type) {
        switch (type) {
            case DataAttributeLPMSimilarityConfigurationComponent:
                return new DataAttributeLPMSimilaritySetupPanel(this.eventLogService);
            case MixedLPMSimilarityConfigurationComponent:
                return new MixedLPMSimilaritySetupPanel(this.dcVM, this);
            case ModelSimilarityLPMSimilarityConfigurationComponent:
                return new ModelSimilarityLPMSimilaritySetupPanel();
        }
        throw new NotImplementedException("No implementation for LPMSimilaritySetupComponent of type " + type);
    }
}
