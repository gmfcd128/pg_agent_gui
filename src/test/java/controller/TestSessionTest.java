package controller;

import model.PGConfigDelta;
import model.TestPlan;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class TestSessionTest {
    TestPlan testPlan;
    TestSession testSession;

    @Before
    public void setUp() throws Exception {
        testPlan = new TestPlan("test", 1, 1);
        HashMap<PGConfigDelta, List<String>> configDeltas = new HashMap<>();
        configDeltas.put(new PGConfigDelta("test1", "01", null, "string"), List.of("01", "02"));
        configDeltas.put(new PGConfigDelta("test2", "01", null, "string"), List.of("01", "02"));
        testPlan.setValues(configDeltas);
        testSession = new TestSession(testPlan);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void createCombinations() {
        Set<List<PGConfigDelta>> result = testSession.createCombinations();
        assertEquals(4, result.size());
        for (List<PGConfigDelta> combination : result) {
                assertTrue(listEqualsIgnoreOrder(combination, List.of(new PGConfigDelta("test1", "01", null, "string"), new PGConfigDelta("test2", "01", null, "string")))
                || listEqualsIgnoreOrder(combination, List.of(new PGConfigDelta("test1", "01", null, "string"), new PGConfigDelta("test2", "02", null, "string")))
                || listEqualsIgnoreOrder(combination, List.of(new PGConfigDelta("test1", "02", null, "string"), new PGConfigDelta("test2", "01", null, "string")))
                || listEqualsIgnoreOrder(combination, List.of(new PGConfigDelta("test1", "02", null, "string"), new PGConfigDelta("test2", "02", null, "string"))));

         }
        //assertTrue(result.contains());
        //assertTrue(result.contains(List.of(new PGConfigDelta("test1", "01", null, "string"), new PGConfigDelta("test2", "02", null, "string"))));
        //assertTrue(result.contains(List.of(new PGConfigDelta("test1", "02", null, "string"), new PGConfigDelta("test2", "01", null, "string"))));
        //assertTrue(result.contains(List.of(new PGConfigDelta("test1", "02", null, "string"), new PGConfigDelta("test2", "02", null, "string"))));
    }

    public static <T> boolean listEqualsIgnoreOrder(List<T> list1, List<T> list2) {
        return new HashSet<>(list1).equals(new HashSet<>(list2));
    }

}
