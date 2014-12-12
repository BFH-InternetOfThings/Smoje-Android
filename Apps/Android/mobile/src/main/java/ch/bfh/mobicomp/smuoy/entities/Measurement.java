package ch.bfh.mobicomp.smuoy.entities;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

import static ch.bfh.mobicomp.smuoy.utils.Utils.*;

/**
 * Created by chris on 07.11.14.
 */
public class Measurement implements Serializable {
    private Date timestamp;
    private String name;
    private String valueString;
    private double valueDecimal;
    private String unit;
    private SensorType type;

    public Measurement(JSONObject json) {
        update(json);
    }

    public void update(JSONObject json) {
        timestamp = date(json, "timestamp", null);
        name = str(json, "name", null);
        valueString = str(json, "valueString", null);
        valueDecimal = dec(json, "valueFloat", 0);
        unit = str(json, "unit", "");
    }

    public SensorType getType() {
        return type;
    }

    public void setType(SensorType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getValueString() {
        return valueString;
    }

    public double getValueDecimal() {
        return valueDecimal;
    }

    public String getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        if (valueString != null && !"null".equalsIgnoreCase(valueString)) {
            return valueString;
        } else {
            return String.format("%1$.2f%2$s", valueDecimal, unit);
        }
    }
}
