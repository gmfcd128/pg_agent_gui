package model;

import java.io.Serializable;

public class PGConfigDelta implements Serializable, Cloneable{
    private String name;
    private String value;
    private String unit;
    private ValueType valueType;
    private String[] options;
    private int allowedMin;
    private long allowedMax;
    private boolean modified;

    /*@Override
    public boolean equals(Object obj) {
        return this.name.equals(((PGConfigDelta)obj).name) && this.value.equals(((PGConfigDelta)obj).value);
    }*/

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
        this.modified = false;
    }

    public int getAllowedMin() {
        return allowedMin;
    }

    public long getAllowedMax() {
        return allowedMax;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        if (valueType == ValueType.ENUM) {
            this.options = options;
        }
    }

    public ValueType getValueType() {
        return valueType;
    }

    public void setRange(int min, long max) {
        if (valueType == ValueType.INTEGER || valueType == ValueType.REAL) {
            this.allowedMin = min;
            this.allowedMax = max;
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

    public boolean isModified() {
        return this.modified;
    }

    public void updateValue(String value) {
        if (value.equals(this.value)) {
            return;
        }
        if (this.valueType == ValueType.STRING || this.valueType == ValueType.BOOL || this.valueType == ValueType.ENUM) {
            this.value = value;
        } else if (this.valueType == ValueType.INTEGER) {
            try {
                int test = Integer.parseInt(value);
                if (allowedMin <= test && test <= allowedMax) {
                    this.value = value;
                } else {
                    return;
                }
            } catch (Exception e) {
                return;
            }
        } else if (this.valueType == ValueType.REAL) {
            try {
                double test = Double.parseDouble(value);
                if (allowedMin <= test && test <= allowedMax) {
                    this.value = value;
                } else {
                    return;
                }
            } catch (Exception e) {
                return;
            }
        }
        modified = true;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        // TODO: Your custom clone logic
        return super.clone();
    }



}