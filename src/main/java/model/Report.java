package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Report {
    private LocalDateTime startTime;
    private StringBuilder stringBuilder;
    private boolean buildingTable;

    private List<TestResult> testResults;

    public Report() {
        this.testResults = new ArrayList<>();
        this.buildingTable = false;
        this.startTime = java.time.LocalDateTime.now();
        this.stringBuilder = new StringBuilder("<html lang=\"en\">\n" +
                "\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Test report</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            padding: 20px;\n" +
                "            background: #eaeaea;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n");
        this.stringBuilder.append("<body>\n" +
                "    <h1>PostgreSQL測試報告</h1>\n" +
                "    <p>測試開始時間:" + this.startTime.toString() + "</p>\n");
    }

    private void closeTable() {
        this.stringBuilder.append("    </table>\n" +
                "    <br>\n");
    }

    private void appendTitle(String title) {
        if (this.buildingTable) {
            this.closeTable();
            this.buildingTable = false;
        }
        this.stringBuilder.append("    <h2>" + title + "</h2>\n");
    }

    private void appendTableHead() {
        this.stringBuilder.append(
                "    <table border=\"1\">\n" +
                        "        <tbody>\n" +
                        "            <tr>\n" +
                        "                <th>Setting</th>\n" +
                        "                <th>Sample No.</th>\n" +
                        "                <th>SQL total time(ms)</th>\n" +
                        "            </tr>\n");
        this.buildingTable = true;
    }

    private void appendTestResult(List<PGConfigDelta> combi, int sampleNumber, double sqlTime) {
        if (!this.buildingTable) {
            this.appendTableHead();
        }
        this.stringBuilder.append("            <tr>\n" +
                "                <td>");
        for (int i = 0; i < combi.size(); i++) {
            this.stringBuilder.append(combi.get(i).getName() + "=" + combi.get(i).getValue());
            if (i < combi.size() - 1) {
                this.stringBuilder.append("<br>");
            }
        }
        this.stringBuilder.append("</td>\n");

        this.stringBuilder.append("           <td>" + sampleNumber + "</td>\n");

        if (sqlTime == -1) {
            this.stringBuilder.append("            <td>(FAILED)</td>\n" +
                    "        </tr>\n");
        } else {
            this.stringBuilder.append("            <td>" + sqlTime + "</td>\n" +
                    "        </tr>\n");
        }

    }

    private void closeReport() {
        this.stringBuilder.append("</body>\n" +
                "\n" +
                "</html>");
    }

    public void addTestResult(TestResult testResult) {
        this.testResults.add(testResult);
    }

    public String generateHTML() {
        int sampleNumber = 1;
        for (int i = 0; i < testResults.size(); i++) {
            if (i == 0 || !testResults.get(i).getName().equals(testResults.get(i - 1).getName())) {
                appendTitle("SQL Test " + testResults.get(i).getName());
                sampleNumber = 1;
            }
            appendTestResult(testResults.get(i).getConfiguration(), sampleNumber, testResults.get(i).getTotalTime());
            sampleNumber++;
        }
        this.closeReport();
        return this.stringBuilder.toString();
    }
}
