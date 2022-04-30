package controller;

import model.PGConfigDelta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {

    public List<PGConfigDelta> downloadFromServer(Server server) {
        Connection jdbcConnection = server.getJdbcConnection();
        ArrayList<PGConfigDelta> result = new ArrayList<>();
        Statement st;
        try {
            st = jdbcConnection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM pg_settings WHERE (source != 'session' AND context != 'internal');");
            while (rs.next()) {
                //System.out.println(rs.getString("name") + ", " + rs.getString("source"));
                String name = rs.getString("name");
                String setting = rs.getString("setting");
                String unit = rs.getString("unit");
                String varType = rs.getString("vartype");
                PGConfigDelta configDelta = new PGConfigDelta(name, setting, unit, varType);
                if (varType.equals("integer") || varType.equals("real")) {
                    int minValue = rs.getInt("min_val");
                    long maxValue = Double.valueOf(rs.getString("max_val")).longValue();
                    configDelta.setRange(minValue, maxValue);
                }
                if (varType.equals("enum")) {
                    Array a = rs.getArray("enumvals");
                    String[] options = (String[])a.getArray();
                    configDelta.setOptions(options);
                }
                result.add(configDelta);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<PGConfigDelta> compareLocalDifference(Server server, List<PGConfigDelta> config) {
        List<PGConfigDelta> serverConfig = downloadFromServer(server);
        List<PGConfigDelta> difference = new ArrayList<>();
        for (PGConfigDelta configDelta : config) {
            for (PGConfigDelta serverConfigDelta : serverConfig) {
                if (serverConfigDelta.getName().equals(configDelta.getName()) && !serverConfigDelta.getValue().equals(configDelta.getValue())) {
                    difference.add(configDelta);
                }
            }
        }
        return difference;
    }

    public void uploadToServer(Server server, List<PGConfigDelta> localConfig) throws PGErrorException {
        List<PGConfigDelta> localDifference = compareLocalDifference(server, localConfig);
        for (PGConfigDelta configDelta : localDifference) {
            server.applyPGConfigDelta(configDelta);
        }
        server.restartPostgres();
    }
}
