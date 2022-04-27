package model;

public class PGConfigDelta {
    private String name;
    private String value;
    private String unit;
    private ValueType valueType;
    private String[] options;
    private double allowedMin;
    private double allowedMax;

    public PGConfigDelta(String name, String value, String unit, String valueType) {
        this.name = name;
        this.value = value;
        this.unit = unit;
        if (valueType.equals("string")) {
            this.valueType = ValueType.STRING;
        } else if (valueType.equals("bool")) {
            this.valueType = ValueType.BOOL;
            this.options = new String[]{"on", "off"};
        } else if (valueType.equals("enum")) {
            this.valueType = ValueType.ENUM;
        } else if (valueType.equals("integer")) {
            this.valueType = ValueType.INTEGER;
        } else if (valueType.equals("real")) {
            this.valueType = ValueType.REAL;
        }
    }

    public void setRange(double min, double max) {
        if (valueType == ValueType.INTEGER || valueType == ValueType.REAL) {
            this.allowedMin = min;
            this.allowedMax = max;
        }
    }

    public void setOptions(String[] options) {
        if (valueType == ValueType.ENUM) {
            this.options = options;
        }
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    public void updateValue(String value) {
        if (this.valueType == ValueType.STRING) {
            this.value = value;
        } else if (this.valueType == ValueType.INTEGER) {
            try {
                int test = Integer.parseInt(value);
                if (allowedMin < test && test < allowedMax) {
                    this.value = value;
                }
            } catch (Exception e) {
                return;
            }
        } else if (this.valueType == ValueType.REAL) {
            try {
                double test = Double.parseDouble(value);
                if (allowedMin < test && test < allowedMax) {
                    this.value = value;
                }
            } catch (Exception e) {
                return;
            }
        }
    }

}

enum ValueType {BOOL, ENUM, INTEGER, REAL, STRING}