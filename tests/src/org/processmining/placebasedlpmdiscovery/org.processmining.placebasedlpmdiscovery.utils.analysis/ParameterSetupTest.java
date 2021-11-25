package org.processmining.placebasedlpmdiscovery.analysis;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.processmining.placebasedlpmdiscovery.analysis.parametermanager.ParameterSetup;

import java.util.Arrays;

public class ParameterSetupTest {

    private String json1;
    private ParameterSetup object1;

    @Before
    public void init() {
        json1 = "{\"placeLimit\":[50,75,100]," +
                "\"proximity\":[5,7,10]," +
                "\"timeLimit\":100," +
                "\"minPlaces\":2," +
                "\"maxPlaces\":5," +
                "\"minTransitions\":3," +
                "\"maxTransitions\":7}";

        object1 = new ParameterSetup();
        object1.setMaxPlaces(5);
        object1.setMaxTransitions(7);
        object1.setMinPlaces(2);
        object1.setMinTransitions(3);
        object1.setTimeLimit(100L);
        object1.setPlaceLimit(Arrays.asList(50, 75, 100));
        object1.setProximity(Arrays.asList(5, 7, 10));
    }


    @Test
    public void test_serialize() {
        String resJson = object1.serialize();

        Assert.assertEquals(json1, resJson);
    }

    @Test
    public void test_getInstance() {
        ParameterSetup resObject = ParameterSetup.getInstance(json1);

        Assert.assertEquals(object1, resObject);
    }
}
