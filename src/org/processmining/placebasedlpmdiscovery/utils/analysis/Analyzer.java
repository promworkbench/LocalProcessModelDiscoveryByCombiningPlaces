package org.processmining.placebasedlpmdiscovery.utils.analysis;

import flanagan.analysis.Stat;
import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.loganalyzer.LogAnalyzer;
import org.processmining.placebasedlpmdiscovery.plugins.mining.PlaceBasedLPMDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.utils.ProjectProperties;
import org.processmining.placebasedlpmdiscovery.utils.analysis.statistics.*;

import java.util.UUID;

public class Analyzer {

    public static final int VERSION = 1;

    private final UUID id;
    private String writeDestination = "data/statistics/";

    // analyzers
    public LogAnalyzer logAnalyzer;
    public SingleExecutionAnalysis totalExecution;
    public SingleExecutionAnalysis lpmDiscoveryExecution;
    private SingleExecutionAnalysis currentWindowProcessingExecution;

    // statistics
    private final Statistics statistics;

    public Analyzer(XLog log) {
        this.id = UUID.randomUUID();

        this.statistics = new Statistics(this.id);
        this.statistics.getGeneralStatistics().setEventLogName(String.valueOf(log.getAttributes().get("concept:name")));

        this.logAnalyzer = new LogAnalyzer(log, this.statistics.getLogStatistics());
        this.totalExecution = new SingleExecutionAnalysis();
        this.lpmDiscoveryExecution = new SingleExecutionAnalysis();
    }

    public void changeWriteDestination(String writeDestination) {
        this.writeDestination = writeDestination;
    }

    public void write() {
        boolean rewrite = false;
        if (ProjectProperties.getIntProperty(ProjectProperties.ANALYSIS_WRITE_VERSION_KEY) != VERSION) {
            ProjectProperties.updateIntegerProperty(ProjectProperties.ANALYSIS_WRITE_VERSION_KEY, VERSION);
            rewrite = true;
        }
        this.getStatistics().write(writeDestination, rewrite);
    }

    public void setStatisticsForParameters(PlaceBasedLPMDiscoveryParameters parameters) {
        this.getStatistics().getParameterStatistics().setProximity(parameters.getLpmCombinationParameters().getLpmProximity());
        this.getStatistics().getParameterStatistics().setCountPlacesLimit(parameters.getPlaceChooserParameters().getPlaceLimit());
        this.getStatistics().getParameterStatistics().setPlaceDiscoveryAlgorithmId(parameters.getPlaceDiscoveryAlgorithmId());

        this.getStatistics().getGeneralStatistics().setProximity(parameters.getLpmCombinationParameters().getLpmProximity());
    }

    public void logCountPlacesUsed(int count) {
        this.getStatistics().getGeneralStatistics().setCountPlacesUsed(count);
    }

    public void logAllLpmDiscovered(int count) {
        this.getStatistics().getGeneralStatistics().setLpmDiscovered(count);
    }

    public void logLpmReturned(int count) {
        this.getStatistics().getGeneralStatistics().setLpmReturned(count);
    }

    public void startWindow() {
        if (this.currentWindowProcessingExecution != null) {
            throw new IllegalStateException("New window started before the previous has ended");
        }
        this.currentWindowProcessingExecution = new SingleExecutionAnalysis();
        this.currentWindowProcessingExecution.start();
    }

    public void stopWindow() {
        this.currentWindowProcessingExecution.stop();
        this.statistics.getFpGrowthStatistics().logWindowProcessed(this.currentWindowProcessingExecution.getDuration().toMillis());
        this.currentWindowProcessingExecution = null;
    }

    public void close() {
        this.getStatistics().getGeneralStatistics().setTotalDuration(this.totalExecution.getDuration());
        this.getStatistics().getGeneralStatistics().setLpmDiscoveryDuration(this.lpmDiscoveryExecution.getDuration());
    }

    public Statistics getStatistics() {
        return this.statistics;
    }
}


