package org.processmining.placebasedlpmdiscovery.utils.analysis.statistics;

import org.processmining.placebasedlpmdiscovery.utils.analysis.Analyzer;

import java.util.UUID;

public class Statistics implements IStatistics {

    private final UUID id;

    private final PlaceStatistics placeStatistics;
    private final FPGrowthStatistics fpGrowthStatistics;
    private final GeneralStatistics generalStatistics;
    private final ParameterStatistics parameterStatistics;
    private final LogStatistics logStatistics;

    public Statistics(UUID id) {
        this.id = id;
        this.fpGrowthStatistics = new FPGrowthStatistics(this.id);
        this.placeStatistics = new PlaceStatistics(this.id);
        this.generalStatistics = new GeneralStatistics(this.id);
        this.parameterStatistics = new ParameterStatistics(this.id);
        this.logStatistics = new LogStatistics(this.id);
    }

    @Override
    public void write(String filename, boolean rewrite) {
        String suffix = "-v" + Analyzer.VERSION;
        fpGrowthStatistics.write(filename + "analysis-fp-growth-statistics" + suffix, rewrite);
        placeStatistics.write(filename+ "analysis-place-statistics" + suffix, rewrite);
        generalStatistics.write(filename+ "analysis-general-statistics" + suffix, rewrite);
        parameterStatistics.write(filename+ "analysis-parameter-statistics" + suffix, rewrite);
        logStatistics.write(filename+ "analysis-log-statistics" + suffix, rewrite);
    }

    public PlaceStatistics getPlaceStatistics() {
        return placeStatistics;
    }

    public FPGrowthStatistics getFpGrowthStatistics() {
        return fpGrowthStatistics;
    }

    public GeneralStatistics getGeneralStatistics() {
        return generalStatistics;
    }

    public ParameterStatistics getParameterStatistics() {
        return parameterStatistics;
    }

    public LogStatistics getLogStatistics() {
        return logStatistics;
    }
}
