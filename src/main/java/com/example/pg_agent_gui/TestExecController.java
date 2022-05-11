package com.example.pg_agent_gui;

import controller.SQLTestRunner;
import controller.Server;
import controller.TestSession;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import model.PGConfigDelta;
import model.TestPlan;
import model.TestResult;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestExecController {
    private Stage stage;
    private TestPlan testPlan;
    private Server server;
    private TestSession testSession;
    private int totalTestCount;
    private int succeededTest;
    private int failedTest;

    @FXML
    private ProgressBar progressBar;

    public TestExecController(Stage stage, TestPlan testPlan, Server server) {
        this.testPlan = testPlan;
        this.testSession = new TestSession(testPlan);
        this.succeededTest = 0;
        this.failedTest = 0;
        try {
            execTest();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "讀取測試資料時發生錯誤", ButtonType.OK);
            alert.showAndWait();
            stage.close();
        }

    }

    public void execTest() throws IOException {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        Set<List<PGConfigDelta>> configCombinations = testSession.createCombinations();
        totalTestCount = configCombinations.size() * testPlan.getNumberOfRuns() * testPlan.getSQLCommands().size();
        for (List<PGConfigDelta> combination : configCombinations) {
            for (int i = 1; i <= testPlan.getNumberOfRuns(); i++) {
                Map<String, String> sqlToRun = testPlan.getSQLCommands();
                for (int j = 0; j < sqlToRun.size(); j++) {
                    SQLTestRunner testRunner = new SQLTestRunner("","",testPlan.getNumberOfThreads(), testPlan.getNumberOfRuns());
                    testRunner.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent event) {
                            TestResult result = testRunner.getValue();
                            onTestExit(result);
                        }
                    });
                    testRunner.setOnFailed(new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent event) {

                        }
                    });
                    singleThreadExecutor.submit(testRunner);
                }

            }
        }
    }

    public void onTestExit(TestResult testResult) {
        if (testResult.isSuccess()) {
            succeededTest += 1;
        } else {
            failedTest += 1;
        }
        progressBar.progressProperty().set((double) totalTestCount / (succeededTest + failedTest));
    }

}
