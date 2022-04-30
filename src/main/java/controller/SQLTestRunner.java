package controller;

import javafx.concurrent.Task;
import model.LoginCredential;

import java.io.IOException;
import java.sql.Driver;
import java.time.Duration;
import static us.abstracta.jmeter.javadsl.JmeterDsl.*;

public class SQLTestRunner extends Task<Void> {
    private String query;
    private int numberOfThreads;
    private int numberOfRuns;
    private LoginCredential loginCredential;

    public SQLTestRunner(String query, int numberOfThreads, int numberOfRuns, LoginCredential loginCredential) {
        this.query = query;
        this.numberOfThreads = numberOfThreads;
        this.numberOfRuns = numberOfRuns;
        this.loginCredential = loginCredential;
    }


    @Override
    protected Void call() throws Exception {
        System.out.println("SQL test triggered.");
        try {
            TestPlanStats stats = testPlan(
                    jdbcConnectionPool("jdbcPool", Driver.class, "jdbc:postgresql://140.124.183.60:5432/raritan")
                            .user("ntutstudent")
                            .password("Lab438!"),
                    threadGroup(this.numberOfThreads, this.numberOfRuns,
                            jdbcSampler("SQL command", "jdbcPool",
                                    this.query)
                                    .timeout(Duration.ofSeconds(60))
                    )
            ).run();
            if (stats.overall().errorsCount() > 0) {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
