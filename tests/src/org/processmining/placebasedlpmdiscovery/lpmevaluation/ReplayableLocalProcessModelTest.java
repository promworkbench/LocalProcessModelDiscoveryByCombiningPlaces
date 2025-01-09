package src.org.processmining.placebasedlpmdiscovery.lpmevaluation;

import org.junit.Assert;
import org.junit.Test;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.ReplayableLocalProcessModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public class ReplayableLocalProcessModelTest {

    @Test
    public void givenSeqAbc_whenRevert_thenSeqCba() {
        // a is 1, b is 2, c is 3
        ReplayableLocalProcessModel replayableLocalProcessModel = new ReplayableLocalProcessModel();
        replayableLocalProcessModel.addConstraint("1", 0, new HashSet<>(Collections.singletonList(2)),
                new HashSet<>(Collections.singletonList(1)));
        replayableLocalProcessModel.addConstraint("2", 0, new HashSet<>(Collections.singletonList(3)),
                new HashSet<>(Collections.singletonList(2)));

        ReplayableLocalProcessModel actual = replayableLocalProcessModel.revert();

        ReplayableLocalProcessModel expected = new ReplayableLocalProcessModel();
        expected.addConstraint("1", 0, new HashSet<>(Collections.singletonList(1)),
                new HashSet<>(Collections.singletonList(2)));
        expected.addConstraint("2", 0, new HashSet<>(Collections.singletonList(2)),
                new HashSet<>(Collections.singletonList(3)));
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void givenSeqAOrBc_whenRevert_thenOrBcSeqA() {
        // a is 1, b is 2, c is 3
        ReplayableLocalProcessModel replayableLocalProcessModel = new ReplayableLocalProcessModel();
        replayableLocalProcessModel.addConstraint("1", 0, new HashSet<>(Arrays.asList(2, 3)),
                new HashSet<>(Collections.singletonList(1)));

        ReplayableLocalProcessModel actual = replayableLocalProcessModel.revert();

        ReplayableLocalProcessModel expected = new ReplayableLocalProcessModel();
        expected.addConstraint("1", 0, new HashSet<>(Collections.singletonList(1)),
                new HashSet<>(Arrays.asList(2, 3)));
        Assert.assertEquals(expected, actual);
    }
}
