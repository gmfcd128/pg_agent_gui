package controller;

import model.PGConfigDelta;
import model.ValueType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class ConfigManagerTest {
    @Mock
    ResultSet resultSet;
    @Mock
    Statement statement;
    @Mock
    Connection jdbcConnection;

    @Mock
    Server server;

    @Before
    public void setUp() throws Exception {
        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        Mockito.when(resultSet.getString("name"))
                .thenReturn("authentication_timeout")
                .thenReturn("array_nulls")
                //.thenReturn("archive_mode")
                .thenReturn("archive_command")
                .thenReturn("autovacuum_analyze_scale_factor");
        Mockito.when(resultSet.getString("setting"))
                .thenReturn("60")
                .thenReturn("on")
                //.thenReturn("off")
                .thenReturn("(disabled)")
                .thenReturn("0.05");
        Mockito.when(resultSet.getString("unit"))
                .thenReturn("s")
                .thenReturn(null)
                //.thenReturn(null)
                .thenReturn(null)
                .thenReturn(null);
        Mockito.when(resultSet.getString("vartype"))
                .thenReturn("integer")
                .thenReturn("bool")
                //.thenReturn("enum")
                .thenReturn("string")
                .thenReturn("real");
        Mockito.when(resultSet.getInt("min_val"))
                .thenReturn(1)
                .thenReturn(0)
                //.thenReturn(0)
                .thenReturn(0)
                .thenReturn(0);
        Mockito.when(resultSet.getString("max_val"))
                .thenReturn("600")
                .thenReturn("0")
                //.thenReturn("0")
                .thenReturn("0")
                .thenReturn("100");
        Mockito.when(resultSet.getArray("enumvals"))
                .thenReturn(null)
                .thenReturn(null)
                //.thenReturn(jdbcConnection.createArrayOf("string",  new String[]{"always","on", "off"}))
                .thenReturn(null)
                .thenReturn(null);


        Mockito.when(statement.executeQuery("SELECT * FROM pg_settings WHERE (source != 'session' AND context != 'internal');")).thenReturn(resultSet);
        Mockito.when(jdbcConnection.createStatement()).thenReturn(statement);
        Mockito.when(server.getJdbcConnection()).thenReturn(jdbcConnection);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void downloadFromServer() {
        ConfigManager configManager = new ConfigManager();
        List<PGConfigDelta> configuration = configManager.downloadFromServer(server);
        assertEquals(4, configuration.size());
        assertEquals("authentication_timeout", configuration.get(0).getName());
        assertEquals("array_nulls", configuration.get(1).getName());
        assertEquals("archive_command", configuration.get(2).getName());
        assertEquals("autovacuum_analyze_scale_factor", configuration.get(3).getName());
        assertEquals("60", configuration.get(0).getValue());
        assertEquals("on", configuration.get(1).getValue());
        assertEquals("(disabled)", configuration.get(2).getValue());
        assertEquals("0.05", configuration.get(3).getValue());
        assertEquals(ValueType.INTEGER, configuration.get(0).getValueType());
        assertEquals(ValueType.BOOL, configuration.get(1).getValueType());
        assertEquals(ValueType.STRING, configuration.get(2).getValueType());
        assertEquals(ValueType.REAL, configuration.get(3).getValueType());
        assertEquals(1, configuration.get(0).getAllowedMin());
        assertEquals(0, configuration.get(3).getAllowedMin());
        assertEquals(600, configuration.get(0).getAllowedMax());
        assertEquals(100, configuration.get(3).getAllowedMax());

    }

    @Test
    public void compareLocalDifference() {
    }

    @Test
    public void uploadToServer() {
    }
}