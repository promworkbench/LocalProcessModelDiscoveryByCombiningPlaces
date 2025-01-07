package org.processmining.placebasedlpmdiscovery.lpmevaluation.logs;

import org.processmining.placebasedlpmdiscovery.model.logs.activities.Activity;

import java.util.List;

public interface IWindowInfo {

    List<Activity> getWindow();

    int getStartPos();

    int getEndPos();

    boolean hasPreviousWindow();
}
