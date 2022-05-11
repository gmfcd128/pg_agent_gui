package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import model.PGConfigDelta;
import model.TestPlan;

import java.util.*;

public class TestSession {
    private TestPlan testPlan;

    private Server server;
    private List<List<PGConfigDelta>> configDeltaList;

    public TestSession(TestPlan testPlan) {
        this.testPlan = testPlan;
        Map<PGConfigDelta, List<String>> configData = this.testPlan.getValues();
        configDeltaList = new ArrayList<>();
        for (Map.Entry<PGConfigDelta, List<String>> entry : configData.entrySet()) {
            List<PGConfigDelta> subList = new ArrayList<>();
            for (String value : entry.getValue()) {
                PGConfigDelta configDelta = null;
                try {
                    configDelta = (PGConfigDelta) entry.getKey().clone();
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
                configDelta.updateValue(value);
                subList.add(configDelta);
            }
            configDeltaList.add(subList);
        }


    }

    public Set<List<PGConfigDelta>> createCombinations() {
        Set<List<PGConfigDelta>> combinations = new HashSet<List<PGConfigDelta>>();
        Set<List<PGConfigDelta>> newCombinations;

        int index = 0;

        // extract each of the integers in the first list
        // and add each to ints as a new list
        for (PGConfigDelta i : configDeltaList.get(0)) {
            List<PGConfigDelta> newList = new ArrayList<PGConfigDelta>();
            newList.add(i);
            combinations.add(newList);
        }
        index++;
        while (index < configDeltaList.size()) {
            List<PGConfigDelta> nextList = configDeltaList.get(index);
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

    public TestPlan getTestPlan() {
        return this.testPlan;
    }
}
