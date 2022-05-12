package com.example.pg_agent_gui;

import controller.SQLTestRunner;
import controller.Server;
import controller.TestSession;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.PGConfigDelta;
import model.Report;
import model.TestPlan;
import model.TestResult;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestExecController {
    private Stage stage;
    private TestPlan testPlan;
    private Server server;
    private TestSession testSession;

    private Report report;
    private int totalTestCount;
    private int succeededTest;
    private int failedTest;

    private ExecutorService singleThreadExecutor;

    @FXML
    private Label elapsedTestsLabel;

    @FXML
    private Label failedTestsLabel;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Button stopButton;

    @FXML
    private Label succeededTestsLabel;

    @FXML
    private Label totalTestsLabel;


    public TestExecController(Stage stage, TestPlan testPlan, Server server) {
        this.server = server;
        singleThreadExecutor = Executors.newSingleThreadExecutor();
        this.testPlan = testPlan;
        this.testSession = new TestSession(testPlan);
        this.totalTestCount = 0;
        this.succeededTest = 0;
        this.failedTest = 0;
        report = new Report();


    }

    public void initialize() {
        try {
            execTest();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "讀取測試資料時發生錯誤", ButtonType.OK);
            alert.showAndWait();
            stage.close();
        }
    }

    public void execTest() throws IOException {
        Set<List<PGConfigDelta>> configCombinations = testSession.createCombinations();
        totalTestCount = configCombinations.size() * testPlan.getNumberOfRuns() * testPlan.getSQLCommands().size();
        totalTestsLabel.setText(String.valueOf(totalTestCount));
        Map<String, String> sqlToRun = testPlan.getSQLCommands();
        for (Map.Entry<String, String> entry : sqlToRun.entrySet()) {
            for (List<PGConfigDelta> combination : configCombinations) {
                for (int i = 1; i <= testPlan.getNumberOfRuns(); i++) {
                    SQLTestRunner testRunner = new SQLTestRunner("", "", testPlan.getNumberOfThreads(), testPlan.getNumberOfRuns());
                    testRunner.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent event) {
                            TestResult result = new TestResult(entry.getKey(), combination, server.getTotalQueryTime(), testRunner.getValue());
                            onTestExit(result);
                        }
                    });
                    testRunner.setOnFailed(new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent event) {
                            TestResult result = new TestResult(entry.getKey(), combination, server.getTotalQueryTime(), false);
                            onTestExit(result);
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
        progressBar.progressProperty().set((double) (succeededTest + failedTest) / totalTestCount);
        elapsedTestsLabel.setText(String.valueOf(succeededTest + failedTest));
        succeededTestsLabel.setText(String.valueOf(succeededTest));
        failedTestsLabel.setText(String.valueOf(failedTest));
        this.report.addTestResult(testResult);
        if (succeededTest + failedTest == totalTestCount) {
            FileChooser fileChooser = new FileChooser();

            //Set extension filter
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("HTML web page (*.html)", "*.html");
            fileChooser.getExtensionFilters().add(extFilter);

            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                testSession.makeReport(report, file);
            }
        }
    }

    @FXML
    void onStopButtonClicked(MouseEvent event) {
        singleThreadExecutor.shutdownNow();
        stage.close();
    }

}
