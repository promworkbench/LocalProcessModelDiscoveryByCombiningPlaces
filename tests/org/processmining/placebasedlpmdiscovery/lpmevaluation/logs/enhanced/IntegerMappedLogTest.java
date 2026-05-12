package org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.enhanced;

import org.junit.Assert;
import org.junit.Test;
import org.processmining.placebasedlpmdiscovery.model.logs.XLogWrapper;

import java.util.Arrays;
import java.util.Collections;

public class IntegerMappedLogTest {

    @Test
    public void givenEmptyLog_whenGetEventCount_thenZero() {
        XLogWrapper wrapper = XLogWrapper.fromListOfTracesAsListStrings(Collections.emptyList());
        IntegerMappedLog log = new IntegerMappedLog(wrapper.getOriginalLog());
        Assert.assertEquals(0, log.getEventCount());
    }

    @Test
    public void givenSingleTraceWithThreeEvents_whenGetEventCount_thenThree() {
        XLogWrapper wrapper = XLogWrapper.fromListOfTracesAsListStrings(
                Collections.singletonList(Arrays.asList("a", "b", "c")));
        IntegerMappedLog log = new IntegerMappedLog(wrapper.getOriginalLog());
        Assert.assertEquals(3, log.getEventCount());
    }

    @Test
    public void givenTwoDifferentTraces_whenGetEventCount_thenSumOfBothTraces() {
        XLogWrapper wrapper = XLogWrapper.fromListOfTracesAsListStrings(
                Arrays.asList(Arrays.asList("a", "b", "c"), Arrays.asList("d", "e")));
        IntegerMappedLog log = new IntegerMappedLog(wrapper.getOriginalLog());
        Assert.assertEquals(5, log.getEventCount());
    }

    @Test
    public void givenTwoIdenticalTraces_whenGetEventCount_thenVariantCountMultiplied() {
        // identical traces collapse into one variant with count=2, so 3 events * 2 = 6
        XLogWrapper wrapper = XLogWrapper.fromListOfTracesAsListStrings(
                Arrays.asList(Arrays.asList("a", "b", "c"), Arrays.asList("a", "b", "c")));
        IntegerMappedLog log = new IntegerMappedLog(wrapper.getOriginalLog());
        Assert.assertEquals(6, log.getEventCount());
    }
}
