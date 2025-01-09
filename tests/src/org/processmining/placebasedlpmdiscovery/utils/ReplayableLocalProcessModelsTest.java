package src.org.processmining.placebasedlpmdiscovery.utils;

import org.junit.Assert;
import org.junit.Test;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.ReplayableLocalProcessModel;
import org.processmining.placebasedlpmdiscovery.utils.ReplayableLocalProcessModels;

import java.util.Collections;
import java.util.HashSet;

public class ReplayableLocalProcessModelsTest {

    @Test
    public void givenSeqAbcAndSeqAdc_whenJoin_thenSeqAAndBdSeqC() {
        // a is 1, b is 2, c is 3, d is 4
        ReplayableLocalProcessModel lpm1 = new ReplayableLocalProcessModel();
        lpm1.addConstraint("1", 0, new HashSet<>(Collections.singletonList(2)),
                new HashSet<>(Collections.singletonList(1)));
        lpm1.addConstraint("2", 0, new HashSet<>(Collections.singletonList(3)),
                new HashSet<>(Collections.singletonList(2)));
        ReplayableLocalProcessModel lpm2 = new ReplayableLocalProcessModel();
        lpm2.addConstraint("3", 0, new HashSet<>(Collections.singletonList(4)),
                new HashSet<>(Collections.singletonList(1)));
        lpm2.addConstraint("4", 0, new HashSet<>(Collections.singletonList(3)),
                new HashSet<>(Collections.singletonList(4)));

        ReplayableLocalProcessModel actual = ReplayableLocalProcessModels.join(lpm1, lpm2);

        ReplayableLocalProcessModel expected = new ReplayableLocalProcessModel();
        expected.addConstraint("1", 0, new HashSet<>(Collections.singletonList(2)),
                new HashSet<>(Collections.singletonList(1)));
        expected.addConstraint("2", 0, new HashSet<>(Collections.singletonList(3)),
                new HashSet<>(Collections.singletonList(2)));
        expected.addConstraint("3", 0, new HashSet<>(Collections.singletonList(4)),
                new HashSet<>(Collections.singletonList(1)));
        expected.addConstraint("4", 0, new HashSet<>(Collections.singletonList(3)),
                new HashSet<>(Collections.singletonList(4)));
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void givenSeqAbcAndSeqAbd_whenJoin_thenSeqAbAndCd() {
        // a is 1, b is 2, c is 3, d is 4
        ReplayableLocalProcessModel lpm1 = new ReplayableLocalProcessModel();
        lpm1.addConstraint("1", 0, new HashSet<>(Collections.singletonList(2)),
                new HashSet<>(Collections.singletonList(1)));
        lpm1.addConstraint("2", 0, new HashSet<>(Collections.singletonList(3)),
                new HashSet<>(Collections.singletonList(2)));
        ReplayableLocalProcessModel lpm2 = new ReplayableLocalProcessModel();
        lpm2.addConstraint("1", 0, new HashSet<>(Collections.singletonList(2)),
                new HashSet<>(Collections.singletonList(1)));
        lpm2.addConstraint("3", 0, new HashSet<>(Collections.singletonList(2)),
                new HashSet<>(Collections.singletonList(4)));

        ReplayableLocalProcessModel actual = ReplayableLocalProcessModels.join(lpm1, lpm2);

        ReplayableLocalProcessModel expected = new ReplayableLocalProcessModel();
        expected.addConstraint("1", 0, new HashSet<>(Collections.singletonList(2)),
                new HashSet<>(Collections.singletonList(1)));
        expected.addConstraint("2", 0, new HashSet<>(Collections.singletonList(3)),
                new HashSet<>(Collections.singletonList(2)));
        expected.addConstraint("3", 0, new HashSet<>(Collections.singletonList(2)),
                new HashSet<>(Collections.singletonList(4)));
        Assert.assertEquals(expected, actual);
    }
}
