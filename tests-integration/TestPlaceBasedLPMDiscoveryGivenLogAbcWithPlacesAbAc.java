import org.assertj.core.api.Assertions;
import org.deckfour.xes.model.XLog;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.LPMDiscovery;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.PlaceBasedLPMDiscovery;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.logs.XLogWrapper;
import org.processmining.placebasedlpmdiscovery.prom.PlacesProvider;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TestPlaceBasedLPMDiscoveryGivenLogAbcWithPlacesAbAc {

    private static PlacesProvider placesProvider;
    private static XLog eventLog;

    @BeforeClass
    public static void setup() {
        Set<Place> places = new HashSet<>();
        places.add(Place.from("a | b"));
        places.add(Place.from("a | c"));
        placesProvider = PlacesProvider.fromSet(places);

        XLogWrapper logWrapper = XLogWrapper.fromListOfTracesAsListStrings(Collections.singletonList(
                Arrays.asList("a", "b", "c")
        ));
        eventLog = logWrapper.getOriginalLog();
    }

    @Test
    public void givenDefault_whenFrom_thenLPMsAbAc() {
        // given
        LPMDiscovery lpmDiscovery = new PlaceBasedLPMDiscovery(placesProvider);

        // when
        LPMDiscoveryResult result = lpmDiscovery.from(eventLog);

        // then
        Assert.assertEquals(1, result.getAllLPMs().size());
        Assertions.assertThat(result.getAllLPMs())
                .extracting(LocalProcessModel::getShortString)
                .containsExactlyInAnyOrder("(a | b)(a | c)"); // LPMs need to contain at least two places
    }

    @Test
    public void givenDefaultWithPlaceLimit_whenFrom_thenLPMsEmpty() {
        // given
        LPMDiscovery lpmDiscovery = new PlaceBasedLPMDiscovery(placesProvider, 1);

        // when
        LPMDiscoveryResult result = lpmDiscovery.from(eventLog);

        // then
        Assert.assertTrue(result.getAllLPMs().isEmpty()); // LPMs need to contain at least two places
    }
}
