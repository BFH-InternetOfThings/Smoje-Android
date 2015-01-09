package ch.bfh.mobicomp.smuoy.entities;

import org.apache.commons.lang3.math.NumberUtils;
import org.json.JSONObject;

import java.io.Serializable;

import static ch.bfh.mobicomp.smuoy.utils.Utils.date;
import static ch.bfh.mobicomp.smuoy.utils.Utils.str;

/**
 * Represents a measurement of a specific sensor from a specific smuoy
 */
public class Measurement implements Serializable {
    public final String smuoyId;
    public final Sensor sensor;

    private long timestamp;
    private String value;

    public Measurement(String smuoyId, Sensor sensor, JSONObject json) {
        this.smuoyId = smuoyId;
        this.sensor = sensor;
        update(json);
    }

    public void update(JSONObject json) {
        timestamp = date(json, "timestamp", System.currentTimeMillis());
        value = str(json, "value", null);
        sensor.update(this);
    }

    public String getString() {
        return value;
    }

    public double getValue() {
        if (NumberUtils.isNumber(value)) {
            return Double.parseDouble(value);
        }
        return 0;
    }

    /**
     * Returns the next execution time as milliseconds from 1970-01-01
     */
    public long getNextExecutionTime() {
        return timestamp + (sensor.delay * 1000);
    }

    @Override
    public String toString() {
        if (NumberUtils.isNumber(value)) {
            return String.format("%1$.2f%2$s", getValue(), sensor.unit);
        }
        return value;
    }
}
