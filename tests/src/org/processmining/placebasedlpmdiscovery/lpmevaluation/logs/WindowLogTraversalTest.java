package src.org.processmining.placebasedlpmdiscovery.lpmevaluation.logs;

import org.deckfour.xes.model.XLog;
import org.junit.Assert;
import org.junit.Test;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.WindowInfo;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.WindowLogTraversal;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.enhanced.IntegerMappedLog;
import org.processmining.placebasedlpmdiscovery.utils.LogUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class WindowLogTraversalTest {

    @Test
    public void givenAbcdAnd3_whenHasNext_thenTrue() throws Exception {
        XLog log = LogUtils.readLogFromFile("data/test/logs/abcd.xes");
        WindowLogTraversal wlt = new WindowLogTraversal(new IntegerMappedLog(log), 3);

        Assert.assertTrue(wlt.hasNext());
    }

    @Test
    public void givenAbcdAnd3_whenNext_thenA() throws Exception {
        XLog log = LogUtils.readLogFromFile("data/test/logs/abcd.xes");
        WindowLogTraversal wlt = new WindowLogTraversal(new IntegerMappedLog(log), 3);

        WindowInfo actual = new WindowInfo(new ArrayList<>(Arrays.asList(0)), 1, 1, 1, 1);

        Assert.assertEquals(wlt.next(), actual);
    }
}
