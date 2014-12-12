package ch.bfh.mobicomp.smuoy.entities;

import ch.bfh.mobicomp.smuoy.cards.CardUpdater;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

import static ch.bfh.mobicomp.smuoy.utils.Utils.*;

/**
 * Represents a measurement of a specific sensor from a specific smuoy
 */
public class Measurement implements Serializable {
    public final String smuoyId, sensorId;

    private Date timestamp;
    private String name;
    private String valueString;
    private double valueDecimal;
    private String unit;
    private String type;

    private CardUpdater updater;

    public Measurement(String smuoyId, Sensor sensor, JSONObject json) {
        this.smuoyId = smuoyId;
        this.sensorId = sensor.id;
        this.type = sensor.name;
        update(json);
    }

    public void update(JSONObject json) {
        timestamp = date(json, "timestamp", null);
        name = str(json, "name", null);
        valueString = str(json, "valueString", null);
        valueDecimal = dec(json, "valueFloat", 0);
        unit = str(json, "unit", "");
        if (updater != null) {
            updater.update(this);
        }
    }

    public void setUpdater(CardUpdater updater) {
        this.updater = updater;
        { // TODO: only if loaded - timestamp != null - as soon as timestamp works
            updater.update(this);
        }
    }

    public String getType() {
        return type;
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
