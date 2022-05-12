package model;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class TestPlanTest {
    TestPlan testPlan;
    FileWriter fileWriter;

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        File f1 = testFolder.newFile("sql1.txt");
        fileWriter = new FileWriter(f1);
        fileWriter.write("file1 SQL");
        fileWriter.close();
        File f2 = testFolder.newFile("f2.jpg");
        File f3 = testFolder.newFile("sql2.txt");
        fileWriter = new FileWriter(f3);
        fileWriter.write("file2 SQL");
        fileWriter.close();
        File f4 = testFolder.newFile("sql4.mp3");
        testPlan = new TestPlan("test", 1, 1);
        testPlan.setPayloadDirectory(testFolder.getRoot().getAbsolutePath());
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getSQLCommands() {
        try {
            Map<String, String> result = testPlan.getSQLCommands();
            assertEquals(2, result.size());
            assertEquals("file1 SQL", result.get("sql1.txt"));
            assertEquals("file2 SQL", result.get("sql2.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}