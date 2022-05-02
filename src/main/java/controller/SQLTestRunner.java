package controller;

import javafx.concurrent.Task;
import model.LoginCredential;

import java.io.IOException;
import java.sql.Driver;
import java.time.Duration;

import us.abstracta.jmeter.javadsl.core.TestPlanStats;

import static us.abstracta.jmeter.javadsl.JmeterDsl.testPlan;
import static us.abstracta.jmeter.javadsl.jdbc.JdbcJmeterDsl.jdbcConnectionPool;
import static us.abstracta.jmeter.javadsl.JmeterDsl.threadGroup;
import static us.abstracta.jmeter.javadsl.jdbc.JdbcJmeterDsl.jdbcSampler;


public class SQLTestRunner extends Task<Void> {
    String query;
    int numberOfThreads;
    int numberOfRuns;
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
                                    "SELECT id FROM products WHERE name=?")
                                    .timeout(Duration.ofSeconds(60))
                    )

            ).run();
            if (stats.overall().errorsCount() > 0) {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
