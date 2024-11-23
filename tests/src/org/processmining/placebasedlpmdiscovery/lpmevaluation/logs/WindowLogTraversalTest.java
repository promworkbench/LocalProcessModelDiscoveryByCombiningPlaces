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

        WindowInfo actual = new WindowInfo(new ArrayList<>(Collections.singletonList(0)), 1, 1, 0, 0);

        Assert.assertEquals(wlt.next(), actual);
    }

    @Test
    public void givenAbcdAnd3After4Next_whenNext_thenCd() throws Exception {
        XLog log = LogUtils.readLogFromFile("data/test/logs/abcd.xes");
        WindowLogTraversal wlt = new WindowLogTraversal(new IntegerMappedLog(log), 3);
        executeNextXTimes(wlt, 4);

        WindowInfo actual = new WindowInfo(new ArrayList<>(Arrays.asList(2, 3)), 1, 1, 2, 3);

        Assert.assertEquals(wlt.next(), actual);
    }

    @Test
    public void givenAbcdAnd3After5Next_whenHasNext_thenTrue() throws Exception {
        XLog log = LogUtils.readLogFromFile("data/test/logs/abcd.xes");
        WindowLogTraversal wlt = new WindowLogTraversal(new IntegerMappedLog(log), 3);
        executeNextXTimes(wlt, 5);

        Assert.assertTrue(wlt.hasNext());
    }

    @Test
    public void givenAbcdAnd3After6Next_whenHasNext_thenFalse() throws Exception {
        XLog log = LogUtils.readLogFromFile("data/test/logs/abcd.xes");
        WindowLogTraversal wlt = new WindowLogTraversal(new IntegerMappedLog(log), 3);
        executeNextXTimes(wlt, 6);

        Assert.assertFalse(wlt.hasNext());
    }

    @Test
    public void givenAbcdAbceAnd3After6Next_whenHasNext_thenTrue() throws Exception {
        XLog log = LogUtils.readLogFromFile("data/test/logs/abcd-abce.xes");
        WindowLogTraversal wlt = new WindowLogTraversal(new IntegerMappedLog(log), 3);
        executeNextXTimes(wlt, 6);

        Assert.assertTrue(wlt.hasNext());
    }

    @Test
    public void givenAbcdAbceAnd3After6Next_whenNext_thenA() throws Exception {
        XLog log = LogUtils.readLogFromFile("data/test/logs/abcd-abce.xes");
        WindowLogTraversal wlt = new WindowLogTraversal(new IntegerMappedLog(log), 3);
        executeNextXTimes(wlt, 6);

        WindowInfo actual = new WindowInfo(new ArrayList<>(Collections.singletonList(0)), 1, 2, 0, 0);

        Assert.assertEquals(wlt.next(), actual);
    }

    private void executeNextXTimes(WindowLogTraversal wlt, int times) {
        while (times > 0) {
            wlt.next();
            times--;
        }
    }
}
