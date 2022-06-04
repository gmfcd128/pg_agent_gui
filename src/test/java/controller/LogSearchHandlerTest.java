package controller;

import model.PGLogEntry;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class LogSearchHandlerTest {
    private LogSearchHandler logSearchHandler;
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();


    @Before
    public void setUp() throws Exception {
        logSearchHandler = new LogSearchHandler();
        temporaryFolder = new TemporaryFolder();
        temporaryFolder.create();
    }

    @After
    public void tearDown() throws Exception {
    }


    @Test
    public void loadFromFile() {
        Path resourceDirectory = Paths.get("src","test","resources", "pg_csv_log_files");
        String testResourceAbsolutePath = resourceDirectory.toFile().getAbsolutePath();
        File f = new File(testResourceAbsolutePath);
        FilenameFilter textFilter = new FilenameFilter() {
            @Override
            public boolean accept(java.io.File file, String s) {
                return s.toLowerCase().endsWith(".csv");
            }
        };
        File[] files = f.listFiles(textFilter);
        logSearchHandler = new LogSearchHandler();
        for (File file : files) {
            logSearchHandler.loadFromFile(file.getAbsolutePath());
        }

        List<PGLogEntry> result = logSearchHandler.calculateResult();

        assertEquals(19, result.size());
        SimpleDateFormat pgLogTimestampMillisecond = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
        try {
            // test date column read for some data
            Date expected;
            expected = pgLogTimestampMillisecond.parse("2022-06-01 01:24:10.966 GMT");
            assertEquals(expected, result.get(0).getLog_time());
            expected = pgLogTimestampMillisecond.parse("2022-06-01 01:24:10.967 GMT");
            assertEquals(expected, result.get(1).getLog_time());
            expected = pgLogTimestampMillisecond.parse("2022-06-01 01:24:10.967 GMT");
            assertEquals(expected, result.get(2).getLog_time());
            expected = pgLogTimestampMillisecond.parse("2022-06-01 01:24:10.969 GMT");
            assertEquals(expected, result.get(3).getLog_time());
            expected = pgLogTimestampMillisecond.parse("2022-06-01 01:24:10.972 GMT");
            assertEquals(expected, result.get(4).getLog_time());
            expected = pgLogTimestampMillisecond.parse("2022-06-01 01:24:10.981 GMT");
            assertEquals(expected, result.get(5).getLog_time());
            expected = pgLogTimestampMillisecond.parse("2022-06-01 01:26:49.941 GMT");
            assertEquals(expected, result.get(6).getLog_time());
            expected = pgLogTimestampMillisecond.parse("2022-06-01 01:29:13.211 GMT");
            assertEquals(expected, result.get(7).getLog_time());
            expected = pgLogTimestampMillisecond.parse("2022-06-01 01:29:16.250 GMT");
            assertEquals(expected, result.get(8).getLog_time());
            expected = pgLogTimestampMillisecond.parse("2022-06-01 01:29:16.251 GMT");
            assertEquals(expected, result.get(9).getLog_time());
            expected = pgLogTimestampMillisecond.parse("2022-06-01 01:29:16.252 GMT");
            assertEquals(expected, result.get(10).getLog_time());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


    }

    @Test
    public void setFilterTimeLower() {
    }

    @Test
    public void setApplicationNameContains() {
    }

    @Test
    public void setSessionIdContains() {
    }

    @Test
    public void setUsernameContains() {
    }

    @Test
    public void setDatabaseContains() {
    }

    @Test
    public void setHostContains() {
    }

    @Test
    public void setFilterTimeUpper() {
    }

    @Test
    public void getDistinctApplicationName() {
    }

    @Test
    public void getDistinctSessionId() {
    }

    @Test
    public void getDistinctUsername() {
    }

    @Test
    public void getDistinctDatabaseName() {
    }

    @Test
    public void getDistinctHost() {
    }

    @Test
    public void calculateResult() {
    }
}