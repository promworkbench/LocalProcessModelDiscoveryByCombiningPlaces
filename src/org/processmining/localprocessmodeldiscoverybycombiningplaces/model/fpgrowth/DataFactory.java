package org.processmining.localprocessmodeldiscoverybycombiningplaces.model.fpgrowth;

import java.util.Optional;

public interface DataFactory<INPUT, DATA> {

    Optional<DATA> create(INPUT input, int inputStartPosition, int inputEndPosition, DATA data, int dataPosition);

    DATA create();
}
