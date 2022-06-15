package controller;

import model.PGLogEntry;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.SortedSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LogSearchHandlerTest {
    private LogSearchHandler logSearchHandler;
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();


    @Before
    public void setUp() throws Exception {
        logSearchHandler = new LogSearchHandler();
        temporaryFolder = new TemporaryFolder();
        temporaryFolder.create();

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
    }

    @After
    public void tearDown() throws Exception {
    }


    @Test
    public void loadFromFile() {
        List<PGLogEntry> result = logSearchHandler.calculateResult();

        assertEquals(19, result.size());
        SimpleDateFormat pgLogTimestampMillisecond = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
        SimpleDateFormat pgLogTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        try {
            // test date column read for some data
            assertEquals(pgLogTimestampMillisecond.parse("2022-06-01 01:24:10.966 GMT"), result.get(0).getLog_time());
            assertEquals(pgLogTimestamp.parse("2022-06-01 01:24:10 GMT"), result.get(0).getSession_start_time());
            assertEquals(pgLogTimestampMillisecond.parse("2022-06-01 01:24:10.967 GMT"), result.get(1).getLog_time());
            assertEquals(pgLogTimestamp.parse("2022-06-01 01:24:10 GMT"), result.get(1).getSession_start_time());
            assertEquals(pgLogTimestampMillisecond.parse("2022-06-01 01:24:10.967 GMT"), result.get(2).getLog_time());
            assertEquals(pgLogTimestamp.parse("2022-06-01 01:24:10 GMT"), result.get(2).getSession_start_time());
            assertEquals(pgLogTimestampMillisecond.parse("2022-06-01 01:24:10.969 GMT"), result.get(3).getLog_time());
            assertEquals(pgLogTimestamp.parse("2022-06-01 01:24:10 GMT"), result.get(3).getSession_start_time());
            assertEquals(pgLogTimestampMillisecond.parse("2022-06-01 01:24:10.972 GMT"), result.get(4).getLog_time());
            assertEquals(pgLogTimestamp.parse("2022-06-01 01:24:10 GMT"), result.get(4).getSession_start_time());
            assertEquals(pgLogTimestampMillisecond.parse("2022-06-01 01:24:10.981 GMT"), result.get(5).getLog_time());
            assertEquals(pgLogTimestamp.parse("2022-06-01 01:24:10 GMT"), result.get(5).getSession_start_time());
            assertEquals(pgLogTimestampMillisecond.parse("2022-06-01 01:26:49.941 GMT"), result.get(6).getLog_time());
            assertEquals(pgLogTimestamp.parse("2022-06-01 01:26:49 GMT"), result.get(6).getSession_start_time());
            assertEquals(pgLogTimestampMillisecond.parse("2022-06-01 01:29:13.211 GMT"), result.get(7).getLog_time());
            assertEquals(pgLogTimestamp.parse("2022-06-01 01:26:57 GMT"), result.get(7).getSession_start_time());
            assertEquals(pgLogTimestampMillisecond.parse("2022-06-01 01:29:16.250 GMT"), result.get(8).getLog_time());
            assertEquals(pgLogTimestamp.parse("2022-06-01 01:24:10 GMT"), result.get(8).getSession_start_time());

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // test some other columns
        assertEquals("jlin", result.get(6).getUser_name());
        assertEquals("jlin", result.get(6).getDatabase_name());
        assertEquals(Integer.valueOf(3704), result.get(6).getProcess_id());
        assertEquals("[local]", result.get(6).getConnection_from());
        assertEquals("6296c059.e78", result.get(6).getSession_id());
        assertEquals(Long.valueOf(1), result.get(6).getSession_line_num());
        assertEquals("startup", result.get(6).getCommand_tag());
        assertEquals("FATAL", result.get(6).getError_severity());
        assertEquals("3D000", result.get(6).getSql_state_code());
        assertEquals("database \"jlin\" does not exist", result.get(6).getMessage());
        assertEquals("client backend", result.get(6).getBackend_type());
    }

    @Test
    public void setFilterTimeLower() {
        SimpleDateFormat pgLogTimestampMillisecond = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
        try {
            logSearchHandler.setFilterTimeLower(pgLogTimestampMillisecond.parse("2022-06-01 01:29:16.350 GMT"));
            assertEquals(6, logSearchHandler.calculateResult().size());
            logSearchHandler.setFilterTimeLower(pgLogTimestampMillisecond.parse("2023-06-01 01:29:16.350 GMT"));
            assertEquals(0, logSearchHandler.calculateResult().size());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void setApplicationNameContains() {
        logSearchHandler.setApplicationNameContains(List.of("psql"));
        List<PGLogEntry> result = logSearchHandler.calculateResult();
        assertEquals(1, result.size());
    }

    @Test
    public void setSessionIdContains() {
        logSearchHandler.setSessionIdContains(List.of("6296c0ec.ed3"));
        List<PGLogEntry> result = logSearchHandler.calculateResult();
        assertEquals(5, result.size());
        for (int i = 0; i < 5; i++) {
            assertEquals(Integer.valueOf(3795), result.get(i).getProcess_id());
            assertEquals(Long.valueOf(i + 1), result.get(i).getSession_line_num());
            assertEquals("postmaster", result.get(i).getBackend_type());
        }
    }

    @Test
    public void setUsernameContains() {
        logSearchHandler.setUsernameContains(List.of("jlin"));
        List<PGLogEntry> result = logSearchHandler.calculateResult();
        assertEquals(2, result.size());
        assertEquals("jlin", result.get(1).getUser_name());
        assertEquals("raritan", result.get(1).getDatabase_name());
        assertEquals(Integer.valueOf(3715), result.get(1).getProcess_id());
        assertEquals("[local]", result.get(1).getConnection_from());
        assertEquals("6296c061.e83", result.get(1).getSession_id());
        assertEquals(Long.valueOf(1), result.get(1).getSession_line_num());
        assertEquals("idle", result.get(1).getCommand_tag());
        assertEquals("ERROR", result.get(1).getError_severity());
        assertEquals("42601", result.get(1).getSql_state_code());
        assertEquals("syntax error at or near \"exir\"", result.get(1).getMessage());
        assertEquals(Integer.valueOf(1), result.get(1).getQuery_pos());
        assertEquals("psql", result.get(1).getApplication_name());
        assertEquals("client backend", result.get(1).getBackend_type());
    }

    @Test
    public void setDatabaseContains() {
        logSearchHandler.setDatabaseContains(List.of("jlin"));
        List<PGLogEntry> result = logSearchHandler.calculateResult();
        assertEquals(1, result.size());
    }

    @Test
    public void setHostContains() {
    }

    @Test
    public void setFilterTimeUpper() {
        SimpleDateFormat pgLogTimestampMillisecond = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
        try {
            logSearchHandler.setFilterTimeUpper(pgLogTimestampMillisecond.parse("2022-06-01 01:24:10.967 GMT"));
            assertEquals(1, logSearchHandler.calculateResult().size());
            logSearchHandler.setFilterTimeUpper(pgLogTimestampMillisecond.parse("2022-06-01 01:24:10.966 GMT"));
            assertEquals(0, logSearchHandler.calculateResult().size());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getDistinctApplicationName() {

    }

    @Test
    public void getDistinctSessionId() {
        SortedSet<String> result = logSearchHandler.getDistinctSessionId();
        assertEquals(7, result.size());
        assertTrue(result.contains("6296bfba.dc3"));
        assertTrue(result.contains("6296bfba.dc7"));
        assertTrue(result.contains("6296bfba.dc8"));
        assertTrue(result.contains("6296c059.e78"));
        assertTrue(result.contains("6296c061.e83"));
        assertTrue(result.contains("6296c0ec.ed3"));
        assertTrue(result.contains("6296c0ec.ed6"));
    }

    @Test
    public void getDistinctUsername() {
    }

    @Test
    public void getDistinctDatabaseName() {
        SortedSet<String> result = logSearchHandler.getDistinctDatabaseName();
        assertTrue(result.contains("jlin"));
        assertTrue(result.contains("raritan"));
        assertTrue(result.contains(""));

    }

    @Test
    public void getDistinctHost() {
    }

    @Test
    public void calculateResult() {
    }
}