package src.org.processmining.placebasedlpmdiscovery.replayer;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.exporting.importers.JsonImporter;
import org.processmining.placebasedlpmdiscovery.model.serializable.LPMResult;
import org.processmining.placebasedlpmdiscovery.replayer.Replayer;
import org.python.google.common.reflect.TypeToken;
import src.org.processmining.mockobjects.MockLPMs;
import src.org.processmining.mockobjects.MockPlaces;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class ReplayerTest {

    @Test
    public void givenSequencePlace_whenFindAllPaths_thenAllInputOutputTransitionPairs() {
        // set input
        Place p = MockPlaces.getSequencePlace_ab();

        // act
        Set<List<String>> actualPaths = Replayer.findAllPaths(10, new LocalProcessModel(p));

        // set expected result
        Set<List<String>> expectedPaths = new HashSet<>(Collections.singletonList(Arrays.asList("a", "b")));

        // test
        Assert.assertEquals(expectedPaths, actualPaths);
    }

    @Test
    public void givenSequencePlaceAndPathLimit1_whenFindAllPaths_thenAllInputOutputTransitionPairs() {
        // set input
        Place p = MockPlaces.getSequencePlace_ab();

        // act
        Set<List<String>> actualPaths = Replayer.findAllPaths(1, new LocalProcessModel(p));

        // set expected result
        Set<List<String>> expectedPaths = new HashSet<>();

        // test
        Assert.assertEquals(expectedPaths, actualPaths);
    }

    @Test
    public void givenSequence_whenFindAllPaths_thenOnePath() {
        // set input
        LocalProcessModel lpm = MockLPMs.getSequenceLPM_abc();

        // act
        Set<List<String>> actualPaths = Replayer.findAllPaths(10, lpm);

        // set expected result
        Set<List<String>> expectedPaths = new HashSet<>(Collections.singletonList(Arrays.asList("a", "b", "c")));

        // test
        Assert.assertEquals(expectedPaths, actualPaths);
    }

    @Test
    public void givenPredefinedLPMs_whenFindAllPaths_thenSameWithPredefinedLanguage() throws IOException {
        // read models
        JsonImporter<LPMResult> lpmsImporter = new JsonImporter<>();
        LPMResult lpmResult = lpmsImporter.read(
                LPMResult.class,
                Files.newInputStream(Paths.get(
                        new File("").getAbsolutePath(),
                        "/data/test/replayer/predefined/models.json")));

        // act
        Map<String, Set<List<String>>> actualPaths = new HashMap<>();
        for (LocalProcessModel lpm : lpmResult.getAllLPMs()) {
            actualPaths.put(lpm.getId(), Replayer.findAllPaths(10, lpm));
        }

        // read expected paths
        Gson gson = new Gson();
        Map<String, List<List<String>>> paths = gson.fromJson(
                new FileReader(Paths.get(
                        new File("").getAbsolutePath(),
                        "/data/test/replayer/predefined/paths.json").toFile()),
                new TypeToken<Map<String, List<List<String>>>>() {
                }.getType());
        Map<String, Set<List<String>>> expectedPaths = paths.keySet().stream().collect(Collectors.toMap(
                key -> key, key -> new HashSet<>(paths.get(key))));

        // test
        for (String id : expectedPaths.keySet()) {
            Assert.assertEquals(expectedPaths.get(id), actualPaths.get(id));
        }
    }
}
