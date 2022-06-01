package controller;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import model.PGLogEntry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class LogSearchHandler {
    private List<PGLogEntry> logEntries;
    private Date filterTimeLower;
    private Date filterTimeUpper;
    private List<String> applicationNameContains;
    private List<String> sessionIdContains;
    private List<String> usernameContains;
    private List<String> databaseContains;
    private List<String> hostContains;


    public LogSearchHandler() {
        reset();
    }

    public void reset() {
        logEntries = new ArrayList<>();
        applicationNameContains = new ArrayList<>();
        sessionIdContains = new ArrayList<>();
        usernameContains = new ArrayList<>();
        databaseContains = new ArrayList<>();
        hostContains = new ArrayList<>();
    }

    public void loadFromFile(String path) {
        Path pathToFile = Paths.get(path);
        try {
            String fileContent = new String(Files.readAllBytes(pathToFile), Charset.defaultCharset());
            try (CSVReader reader = new CSVReader(new StringReader(fileContent))) {
                String[] tokens;
                while ((tokens = reader.readNext()) != null) {
                    logEntries.add(new PGLogEntry(tokens));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (CsvValidationException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setFilterTimeLower(Date date) {
        this.filterTimeLower = date;
    }

    public void setFilterTimeUpper(Date date) {
        this.filterTimeUpper = date;
    }

    public SortedSet<String> getDistinctApplicationName() {
        SortedSet<String> result = new TreeSet<>();
        for (PGLogEntry logEntry : logEntries) {
            result.add(logEntry.getApplication_name());
        }
        result.remove("");
        return result;
    }

    public SortedSet<String> getDistinctSessionId() {
        SortedSet<String> result = new TreeSet<>();
        for (PGLogEntry logEntry : logEntries) {
            result.add(logEntry.getSession_id());
        }
        result.remove("");
        return result;
    }

    public SortedSet<String> getDistinctUsername() {
        SortedSet<String> result = new TreeSet<>();
        for (PGLogEntry logEntry : logEntries) {
            result.add(logEntry.getUser_name());
        }
        result.remove("");
        return result;
    }

    public SortedSet<String> getDistinctDatabaseName() {
        SortedSet<String> result = new TreeSet<>();
        for (PGLogEntry logEntry : logEntries) {
            result.add(logEntry.getDatabase_name());
        }
        result.remove("");
        return result;
    }

    public SortedSet<String> getDistinctHost() {
        SortedSet<String> result = new TreeSet<>();
        for (PGLogEntry logEntry : logEntries) {
            result.add(logEntry.getConnection_from());
        }
        result.remove("");
        return result;
    }

}

