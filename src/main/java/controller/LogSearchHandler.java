package controller;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import model.PGLogEntry;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class LogSearchHandler {
    private List<PGLogEntry> logEntries;

    private List<PGLogEntry> logEntriesFiltered;
    private Date filterTimeLower;
    private Date filterTimeUpper;
    private List<String> applicationNameContains;
    private List<String> sessionIdContains;
    private List<String> usernameContains;
    private List<String> databaseContains;
    private List<String> hostContains;


    public LogSearchHandler() {
        logEntries = new ArrayList<>();
        logEntriesFiltered = new ArrayList<>();
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
                    logEntriesFiltered.add(new PGLogEntry(tokens));
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

    public void setApplicationNameContains(List<String> appNames) {
        this.applicationNameContains = appNames;
    }

    public void setSessionIdContains(List<String> sessionIds) {
        this.sessionIdContains = sessionIds;
    }

    public void setUsernameContains(List<String> usernames) {
        this.usernameContains = usernames;
    }

    public void setDatabaseContains(List<String> databases) {
        this.databaseContains = databases;
    }

    public void setHostContains(List<String> hosts) {
        this.hostContains = hosts;
    }

    public void setFilterTimeUpper(Date date) {
        this.filterTimeUpper = date;
    }

    public SortedSet<String> getDistinctApplicationName() {
        SortedSet<String> result = new TreeSet<>();
        for (PGLogEntry logEntry : logEntriesFiltered) {
            result.add(logEntry.getApplication_name());
        }
        //result.remove("");
        return result;
    }

    public SortedSet<String> getDistinctSessionId() {
        SortedSet<String> result = new TreeSet<>();
        for (PGLogEntry logEntry : logEntriesFiltered) {
            result.add(logEntry.getSession_id());
        }
        //result.remove("");
        return result;
    }

    public SortedSet<String> getDistinctUsername() {
        SortedSet<String> result = new TreeSet<>();
        for (PGLogEntry logEntry : logEntriesFiltered) {
            result.add(logEntry.getUser_name());
        }
        //result.remove("");
        return result;
    }

    public SortedSet<String> getDistinctDatabaseName() {
        SortedSet<String> result = new TreeSet<>();
        for (PGLogEntry logEntry : logEntriesFiltered) {
            result.add(logEntry.getDatabase_name());
        }
        //result.remove("");
        return result;
    }

    public SortedSet<String> getDistinctHost() {
        SortedSet<String> result = new TreeSet<>();
        for (PGLogEntry logEntry : logEntriesFiltered) {
            result.add(logEntry.getConnection_from());
        }
        result.remove("");
        return result;
    }

    public List<PGLogEntry> calculateResult() {
        logEntriesFiltered = this.logEntries.stream().filter(p -> filterTimeLower == null || p.getLog_time().after(filterTimeLower))
                .filter(p -> filterTimeUpper == null || p.getLog_time().before(filterTimeUpper))
                .filter(p -> this.applicationNameContains.isEmpty() || this.applicationNameContains.contains(p.getDatabase_name()))
                .filter(p -> this.sessionIdContains.isEmpty() || this.sessionIdContains.contains(p.getSession_id()))
                .filter(p -> this.usernameContains.isEmpty() || this.usernameContains.contains(p.getUser_name()))
                .filter(p -> this.databaseContains.isEmpty() || this.databaseContains.contains(p.getDatabase_name()))
                .filter(p -> this.hostContains.isEmpty() || this.hostContains.contains(p.getConnection_from())).collect(Collectors.toList());
        return logEntriesFiltered;
    }

}

