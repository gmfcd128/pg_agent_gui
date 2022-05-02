package controller;

import model.PGConfigDelta;
import model.TestPlan;

import java.util.*;

public class TestSession {
    private TestPlan testPlan;
    private Set<List<PGConfigDelta>> configCombinations;
    private Server server;

    public TestSession(TestPlan testPlan) {
        this.testPlan = testPlan;
        Map<PGConfigDelta, List<String>> configData = this.testPlan.getValues();
        List<List<PGConfigDelta>> configDeltaList = new ArrayList<>();
        for (Map.Entry<PGConfigDelta, List<String>> entry : configData.entrySet()) {
            List<PGConfigDelta> subList = new ArrayList<>();
            for (String value : entry.getValue()) {
                PGConfigDelta configDelta = entry.getKey();
                configDelta.updateValue(value);
                subList.add(configDelta);
            }
            configDeltaList.add(subList);
        }

        this.configCombinations = createCombinations(configDeltaList);
        for (List<PGConfigDelta> combination : configCombinations) {
            for (int i = 1; i <= testPlan.getNumberOfRuns(); i++) {
                SQLTestRunner testRunner = new SQLTestRunner("",testPlan.getNumberOfThreads(), testPlan.getNumberOfRuns());

            }
        }
    }

    private Set<List<PGConfigDelta>> createCombinations(List<List<PGConfigDelta>> configDeltaLists) {
        Set<List<PGConfigDelta>> combinations = new HashSet<List<PGConfigDelta>>();
        Set<List<PGConfigDelta>> newCombinations;

        int index = 0;

        // extract each of the integers in the first list
        // and add each to ints as a new list
        for (PGConfigDelta i : configDeltaLists.get(0)) {
            List<PGConfigDelta> newList = new ArrayList<PGConfigDelta>();
            newList.add(i);
            combinations.add(newList);
        }
        index++;
        while (index < configDeltaLists.size()) {
            List<PGConfigDelta> nextList = configDeltaLists.get(index);
            newCombinations = new HashSet<List<PGConfigDelta>>();
            for (List<PGConfigDelta> first : combinations) {
                for (PGConfigDelta second : nextList) {
                    List<PGConfigDelta> newList = new ArrayList<PGConfigDelta>();
                    newList.addAll(first);
                    newList.add(second);
                    newCombinations.add(newList);
                }
            }
            combinations = newCombinations;
            index++;
        }
        return combinations;
    }
}
