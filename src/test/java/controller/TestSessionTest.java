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
                assertTrue(checkListContent(combination, List.of(new PGConfigDelta("test1", "01", null, "string"), new PGConfigDelta("test2", "01", null, "string")))
                || checkListContent(combination, List.of(new PGConfigDelta("test1", "01", null, "string"), new PGConfigDelta("test2", "02", null, "string")))
                || checkListContent(combination, List.of(new PGConfigDelta("test1", "02", null, "string"), new PGConfigDelta("test2", "01", null, "string")))
                || checkListContent(combination, List.of(new PGConfigDelta("test1", "02", null, "string"), new PGConfigDelta("test2", "02", null, "string"))));

         }
    }

    public boolean checkListContent(List<PGConfigDelta> src, List<PGConfigDelta> target) {
        for(int i = 0; i < src.size(); i++) {
            boolean found = false;
            for (int j = 0; j < target.size(); j++) {
                if (src.get(i).getName().equals(target.get(j).getName()) && src.get(i).getValue().equals(target.get(j).getValue())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }

}
