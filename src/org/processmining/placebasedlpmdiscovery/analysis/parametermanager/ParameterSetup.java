package org.processmining.placebasedlpmdiscovery.analysis.parametermanager;

import com.google.gson.Gson;

import java.io.*;
import java.util.List;
import java.util.Objects;

public class ParameterSetup {
    private List<Integer> placeLimit;
    private List<Integer> proximity;
    private List<Integer> cardinality;
    private Long timeLimit;
    private int minPlaces;
    private int maxPlaces;
    private int minTransitions;
    private int maxTransitions;

    public String serialize() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static ParameterSetup getInstance(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, ParameterSetup.class);
    }

    public static ParameterSetup getInstanceFromFile(String filePath) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        return gson.fromJson(sb.toString(), ParameterSetup.class);
    }

    public void setPlaceLimit(List<Integer> placeLimit) {
        this.placeLimit = placeLimit;
    }

    public void setProximity(List<Integer> proximity) {
        this.proximity = proximity;
    }

    public void setTimeLimit(Long timeLimit) {
        this.timeLimit = timeLimit;
    }

    public void setMinPlaces(int minPlaces) {
        this.minPlaces = minPlaces;
    }

    public void setMaxPlaces(int maxPlaces) {
        this.maxPlaces = maxPlaces;
    }

    public void setMinTransitions(int minTransitions) {
        this.minTransitions = minTransitions;
    }

    public void setMaxTransitions(int maxTransitions) {
        this.maxTransitions = maxTransitions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParameterSetup that = (ParameterSetup) o;
        return minPlaces == that.minPlaces && maxPlaces == that.maxPlaces && minTransitions == that.minTransitions && maxTransitions == that.maxTransitions && Objects.equals(placeLimit, that.placeLimit) && Objects.equals(proximity, that.proximity) && Objects.equals(timeLimit, that.timeLimit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(placeLimit, proximity, timeLimit, minPlaces, maxPlaces, minTransitions, maxTransitions);
    }

    public List<Integer> getPlaceLimit() {
        return placeLimit;
    }

    public List<Integer> getProximity() {
        return proximity;
    }

    public List<Integer> getCardinality() {
        return cardinality;
    }

    public Long getTimeLimit() {
        return timeLimit;
    }

    public int getMinPlaces() {
        return minPlaces;
    }

    public int getMaxPlaces() {
        return maxPlaces;
    }

    public int getMinTransitions() {
        return minTransitions;
    }

    public int getMaxTransitions() {
        return maxTransitions;
    }
}
