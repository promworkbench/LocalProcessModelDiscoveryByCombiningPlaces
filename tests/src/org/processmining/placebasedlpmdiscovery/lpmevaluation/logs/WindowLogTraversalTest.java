package src.org.processmining.placebasedlpmdiscovery.lpmevaluation.logs;

import org.deckfour.xes.model.XLog;
import org.junit.Assert;
import org.junit.Test;
import org.processmining.eventlogs.window.WindowBasedEventLog;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.IWindowInfo;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.WindowInfo;
import org.processmining.placebasedlpmdiscovery.model.logs.EventLog;
import org.processmining.placebasedlpmdiscovery.model.logs.XLogWrapper;
import org.processmining.placebasedlpmdiscovery.model.logs.activities.ActivityCache;
import org.processmining.placebasedlpmdiscovery.model.logs.tracevariants.ActivityBasedTotallyOrderedEventLogTraceVariant;
import org.processmining.placebasedlpmdiscovery.model.logs.tracevariants.extractors.ActivityBasedTotallyOrderedEventLogTraceVariantExtractor;
import org.processmining.placebasedlpmdiscovery.utils.LogUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

public class WindowLogTraversalTest {

    @Test
    public void givenAbcdAnd3_whenHasNext_thenTrue() throws Exception {
        XLog log = LogUtils.readLogFromFile("data/test/logs/abcd.xes");
        WindowBasedEventLog wel = WindowBasedEventLog.getInstance(new XLogWrapper(log), 3);

        Assert.assertTrue(wel.iterator().hasNext());
    }

    @Test
    public void givenAbcdAnd3_whenNext_thenA() throws Exception {
        XLog log = LogUtils.readLogFromFile("data/test/logs/abcd.xes");
        EventLog eventLog = new XLogWrapper(log);
        ActivityBasedTotallyOrderedEventLogTraceVariantExtractor tvExtractor =
                new ActivityBasedTotallyOrderedEventLogTraceVariantExtractor("concept:name");
        WindowBasedEventLog wel = WindowBasedEventLog.getInstance(eventLog, 3);

        WindowInfo expected = new WindowInfo(new ArrayList<>(Collections.singletonList(ActivityCache.getInstance()
                .getActivity("a"))), 1, 0, 0, tvExtractor.extract(eventLog).iterator().next());

        Assert.assertEquals(expected, wel.iterator().next());
    }

    @Test
    public void givenAbcdAnd3After4Next_whenNext_thenCd() throws Exception {
        XLog log = LogUtils.readLogFromFile("data/test/logs/abcd.xes");
        EventLog eventLog = new XLogWrapper(log);
        ActivityBasedTotallyOrderedEventLogTraceVariantExtractor tvExtractor =
                new ActivityBasedTotallyOrderedEventLogTraceVariantExtractor("concept:name");
        WindowBasedEventLog wel = WindowBasedEventLog.getInstance(eventLog, 3);
        Iterator<IWindowInfo> wlt = wel.iterator();
        executeNextXTimes(wlt, 4);

        WindowInfo expected = new WindowInfo(new ArrayList<>(Arrays.asList(
                ActivityCache.getInstance().getActivity("c"), ActivityCache.getInstance().getActivity("d")
        )), 1, 2, 3, tvExtractor.extract(eventLog).iterator().next());

        Assert.assertEquals(expected, wlt.next());
    }

    @Test
    public void givenAbcdAnd3After5Next_whenHasNext_thenTrue() throws Exception {
        XLog log = LogUtils.readLogFromFile("data/test/logs/abcd.xes");
        WindowBasedEventLog wel = WindowBasedEventLog.getInstance(new XLogWrapper(log), 3);
        Iterator<IWindowInfo> wlt = wel.iterator();
        executeNextXTimes(wlt, 5);

        Assert.assertTrue(wlt.hasNext());
    }

    @Test
    public void givenAbcdAnd3After6Next_whenHasNext_thenFalse() throws Exception {
        XLog log = LogUtils.readLogFromFile("data/test/logs/abcd.xes");
        WindowBasedEventLog wel = WindowBasedEventLog.getInstance(new XLogWrapper(log), 3);
        Iterator<IWindowInfo> wlt = wel.iterator();
        executeNextXTimes(wlt, 6);

        Assert.assertFalse(wlt.hasNext());
    }

    @Test
    public void givenAbcdAbceAnd3After6Next_whenHasNext_thenTrue() throws Exception {
        XLog log = LogUtils.readLogFromFile("data/test/logs/abcd-abce.xes");
        WindowBasedEventLog wel = WindowBasedEventLog.getInstance(new XLogWrapper(log), 3);
        Iterator<IWindowInfo> wlt = wel.iterator();
        executeNextXTimes(wlt, 6);

        Assert.assertTrue(wlt.hasNext());
    }

    @Test
    public void givenAbcdAbceAnd3After6Next_whenNext_thenA() throws Exception {
        XLog log = LogUtils.readLogFromFile("data/test/logs/abcd-abce.xes");
        EventLog eventLog = new XLogWrapper(log);
        ActivityBasedTotallyOrderedEventLogTraceVariantExtractor tvExtractor =
                new ActivityBasedTotallyOrderedEventLogTraceVariantExtractor("concept:name");
        Iterator<ActivityBasedTotallyOrderedEventLogTraceVariant> it = tvExtractor.extract(eventLog).iterator();
        it.next();
        WindowBasedEventLog wel = WindowBasedEventLog.getInstance(eventLog, 3);
        Iterator<IWindowInfo> wlt = wel.iterator();
        executeNextXTimes(wlt, 6);

        WindowInfo expected = new WindowInfo(new ArrayList<>(Collections.singletonList(
                ActivityCache.getInstance().getActivity("a"))), 1,0, 0, it.next());

        Assert.assertEquals(expected, wlt.next());
    }

    private void executeNextXTimes(Iterator<IWindowInfo> wlt, int times) {
        while (times > 0) {
            wlt.next();
            times--;
        }
    }
}
