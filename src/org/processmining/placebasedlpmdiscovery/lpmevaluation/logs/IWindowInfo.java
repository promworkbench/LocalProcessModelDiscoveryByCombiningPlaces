package org.processmining.placebasedlpmdiscovery.lpmevaluation.logs;

import java.util.ArrayList;

public interface IWindowInfo {
    String getParentSequenceId();

    ArrayList<Integer> getWindow();

    int getStartPos();

    int getEndPos();
}
