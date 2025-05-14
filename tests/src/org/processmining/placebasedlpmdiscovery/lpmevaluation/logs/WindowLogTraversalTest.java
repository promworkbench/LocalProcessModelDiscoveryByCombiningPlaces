package src.org.processmining.placebasedlpmdiscovery.lpmevaluation.logs;

import org.deckfour.xes.model.XLog;
import org.junit.Assert;
import org.junit.Test;
import org.processmining.eventlogs.window.WindowLogIterator;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.WindowInfo;
import org.processmining.placebasedlpmdiscovery.model.logs.XLogWrapper;
import org.processmining.placebasedlpmdiscovery.model.logs.activities.ActivityCache;
import org.processmining.placebasedlpmdiscovery.utils.LogUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class WindowLogTraversalTest {

    @Test
    public void givenAbcdAnd3_whenHasNext_thenTrue() throws Exception {
        XLog log = LogUtils.readLogFromFile("data/test/logs/abcd.xes");
        WindowLogIterator wlt = new WindowLogIterator(new XLogWrapper(log), 3);

        Assert.assertTrue(wlt.hasNext());
    }

    @Test
    public void givenAbcdAnd3_whenNext_thenA() throws Exception {
        XLog log = LogUtils.readLogFromFile("data/test/logs/abcd.xes");
        WindowLogIterator wlt = new WindowLogIterator(new XLogWrapper(log), 3);

        WindowInfo expected = new WindowInfo(new ArrayList<>(Collections.singletonList(ActivityCache.getInstance()
                .getActivity("a"))), 1, 0, 0);

        Assert.assertEquals(expected, wlt.next());
    }

    @Test
    public void givenAbcdAnd3After4Next_whenNext_thenCd() throws Exception {
        XLog log = LogUtils.readLogFromFile("data/test/logs/abcd.xes");
        WindowLogIterator wlt = new WindowLogIterator(new XLogWrapper(log), 3);
        executeNextXTimes(wlt, 4);

        WindowInfo expected = new WindowInfo(new ArrayList<>(Arrays.asList(
                ActivityCache.getInstance().getActivity("c"), ActivityCache.getInstance().getActivity("d")
        )), 1, 2, 3);

        Assert.assertEquals(expected, wlt.next());
    }

    @Test
    public void givenAbcdAnd3After5Next_whenHasNext_thenTrue() throws Exception {
        XLog log = LogUtils.readLogFromFile("data/test/logs/abcd.xes");
        WindowLogIterator wlt = new WindowLogIterator(new XLogWrapper(log), 3);
        executeNextXTimes(wlt, 5);

        Assert.assertTrue(wlt.hasNext());
    }

    @Test
    public void givenAbcdAnd3After6Next_whenHasNext_thenFalse() throws Exception {
        XLog log = LogUtils.readLogFromFile("data/test/logs/abcd.xes");
        WindowLogIterator wlt = new WindowLogIterator(new XLogWrapper(log), 3);
        executeNextXTimes(wlt, 6);

        Assert.assertFalse(wlt.hasNext());
    }

    @Test
    public void givenAbcdAbceAnd3After6Next_whenHasNext_thenTrue() throws Exception {
        XLog log = LogUtils.readLogFromFile("data/test/logs/abcd-abce.xes");
        WindowLogIterator wlt = new WindowLogIterator(new XLogWrapper(log), 3);
        executeNextXTimes(wlt, 6);

        Assert.assertTrue(wlt.hasNext());
    }

    @Test
    public void givenAbcdAbceAnd3After6Next_whenNext_thenA() throws Exception {
        XLog log = LogUtils.readLogFromFile("data/test/logs/abcd-abce.xes");
        WindowLogIterator wlt = new WindowLogIterator(new XLogWrapper(log), 3);
        executeNextXTimes(wlt, 6);

        WindowInfo expected = new WindowInfo(new ArrayList<>(Collections.singletonList(
                ActivityCache.getInstance().getActivity("a"))), 1,0, 0);

        Assert.assertEquals(expected, wlt.next());
    }

    private void executeNextXTimes(WindowLogIterator wlt, int times) {
        while (times > 0) {
            wlt.next();
            times--;
        }
    }
}
