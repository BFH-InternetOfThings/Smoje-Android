package ch.bfh.mobicomp.smuoy.entities;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static ch.bfh.mobicomp.smuoy.Utils.dec;
import static ch.bfh.mobicomp.smuoy.Utils.str;

/**
 * Created by chris on 07.11.14.
 */
public class SimpleMeasurement extends Measurement {
    public double value;
    public String unit;
    public Sensor sensor;

    public SimpleMeasurement(JSONArray measurements, Sensor sensor) {
        super(measurements);
        try {
            JSONObject json = measurements.getJSONObject(0);
            value = dec(json, "value", 0);
            unit = str(json, "unit", "");
            this.sensor = sensor;
        } catch (JSONException e) {
            Log.e("SimpleMeasurement", "Can't get field 'value' from JSON", e);
        }
    }

    @Override
    public String toString() {
        return String.format("%1$.2f%2$s", value, unit);
    }
}
