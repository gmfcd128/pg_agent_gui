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
import org.mockito.stubbing.Answer;

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

    String[][] testData;
    int index;
    boolean firstTime;

    @Before
    public void setUp() throws Exception {
        firstTime = true;
        testData = new String[][]{{"authentication_timeout", "60", "s", "integer", "1", "600","null"},
                {"array_nulls", "on", "null", "bool", "0", "0", "null"},
                {"archive_command", "(disabled)", "null", "string", "0", "0", "null"},
                {"autovacuum_analyze_scale_factor", "0.05", "null", "real", "0", "100", "null"}};
        index = 0;
        Mockito.when(resultSet.next()).thenAnswer((Answer) invocation -> {
            if (firstTime) {
                firstTime = false;
            } else {
                index++;
            }

            return index < 4;
        });
        Mockito.when(resultSet.getString("name")).thenAnswer((Answer<String>) invocation -> testData[index][0]);
        Mockito.when(resultSet.getString("setting")).thenAnswer((Answer<String>) invocation -> testData[index][1]);
        Mockito.when(resultSet.getString("unit")).thenAnswer((Answer<String>) invocation -> testData[index][2]);
        Mockito.when(resultSet.getString("vartype")).thenAnswer((Answer<String>) invocation -> testData[index][3]);
        Mockito.when(resultSet.getInt("min_val")).thenAnswer((Answer<Integer>) invocation -> Integer.parseInt(testData[index][4]));
        Mockito.when(resultSet.getString("max_val")).thenAnswer((Answer<String>) invocation -> testData[index][5]);
        //Mockito.when(resultSet.getArray("enumvals")).thenReturn(null);

        Mockito.when(statement.executeQuery("SELECT * FROM pg_settings WHERE (source != 'session' AND context != 'internal');"))
                .thenReturn(resultSet);
        Mockito.when(jdbcConnection.createStatement()).thenReturn(statement);
        //Mockito.when(jdbcConnection.createArrayOf("string",  new String[]{"always","on", "off"})).thenCallRealMethod();
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
        PGConfigDelta element1 = new PGConfigDelta("authentication_timeout", "60", "s", "integer");
        PGConfigDelta element2 = new PGConfigDelta("array_nulls", "off", null, "bool");
        PGConfigDelta element3 = new PGConfigDelta("archive_command", "(disabled)", null, "string");
        PGConfigDelta element4 = new PGConfigDelta("autovacuum_analyze_scale_factor", "0.05", null, "real");
        List<PGConfigDelta> localConfig = List.of(element1, element2, element3, element4);
        ConfigManager configManager = new ConfigManager();
        List<PGConfigDelta> result = configManager.compareLocalDifference(server, localConfig);
        assertEquals(1, result.size());
        assertEquals("array_nulls", result.get(0).getName());
        assertEquals("off", result.get(0).getValue());

    }

    @Test
    public void uploadToServer() {
    }
}