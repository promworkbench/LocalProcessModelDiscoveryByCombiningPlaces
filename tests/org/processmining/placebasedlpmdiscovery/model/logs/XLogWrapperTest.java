package org.processmining.placebasedlpmdiscovery.model.logs;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

public class XLogWrapperTest {

    @Test
    public void givenEmptyLog_whenGetEventCount_thenZero() {
        XLogWrapper log = XLogWrapper.fromListOfTracesAsListStrings(Collections.emptyList());
        Assert.assertEquals(0, log.getEventCount());
    }

    @Test
    public void givenSingleTraceWithThreeEvents_whenGetEventCount_thenThree() {
        XLogWrapper log = XLogWrapper.fromListOfTracesAsListStrings(
                Collections.singletonList(Arrays.asList("a", "b", "c")));
        Assert.assertEquals(3, log.getEventCount());
    }

    @Test
    public void givenTwoTraces_whenGetEventCount_thenSumOfBothTraces() {
        XLogWrapper log = XLogWrapper.fromListOfTracesAsListStrings(
                Arrays.asList(Arrays.asList("a", "b", "c"), Arrays.asList("d", "e")));
        Assert.assertEquals(5, log.getEventCount());
    }

    @Test
    public void givenTwoIdenticalTraces_whenGetEventCount_thenCountedTwice() {
        XLogWrapper log = XLogWrapper.fromListOfTracesAsListStrings(
                Arrays.asList(Arrays.asList("a", "b"), Arrays.asList("a", "b")));
        Assert.assertEquals(4, log.getEventCount());
    }
}
