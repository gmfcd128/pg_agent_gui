package controller;

import javafx.concurrent.Task;
import model.LoginCredential;

import java.io.IOException;
import java.sql.Driver;
import java.time.Duration;

import model.TestResult;
import org.apache.xpath.operations.Bool;
import us.abstracta.jmeter.javadsl.core.TestPlanStats;

import static us.abstracta.jmeter.javadsl.JmeterDsl.testPlan;
import static us.abstracta.jmeter.javadsl.jdbc.JdbcJmeterDsl.jdbcConnectionPool;
import static us.abstracta.jmeter.javadsl.JmeterDsl.threadGroup;
import static us.abstracta.jmeter.javadsl.jdbc.JdbcJmeterDsl.jdbcSampler;


public class SQLTestRunner extends Task<Boolean> {
    private String name;
    private String query;
    private int numberOfThreads;
    private int numberOfRuns;
    private LoginCredential loginCredential;
    TestPlanStats stats;

    public SQLTestRunner(String name, String query, int numberOfThreads, int numberOfRuns) {
        this.query = query;
        this.numberOfThreads = numberOfThreads;
        this.numberOfRuns = numberOfRuns;
        this.loginCredential = loginCredential;
    }

    @Override
    protected Boolean call() throws Exception {
        TestResult returnVal;
        System.out.println("MOCK");
        Thread.sleep(1000);

        /*try {
            TestPlanStats stats = testPlan(
                    jdbcConnectionPool("jdbcPool", Driver.class, "jdbc:postgresql://" + loginCredential.getIp() + ":5432/" + loginCredential.getDatabase())
                            .user(loginCredential.getPostgresUsername())
                            .password(loginCredential.getPostgresPassword()),
                    threadGroup(this.numberOfThreads, this.numberOfRuns,
                            jdbcSampler("SQL command", "jdbcPool",
                                    this.query)
                                    .timeout(Duration.ofSeconds(100))
                    )
            ).run();
            if (stats.overall().errorsCount() > 0) {
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        */
        return true;
    }

}
