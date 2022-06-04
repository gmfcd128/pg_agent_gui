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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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