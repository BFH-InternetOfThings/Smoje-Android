package ch.bfh.mobicomp.smuoy.entities;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static ch.bfh.mobicomp.smuoy.utils.Utils.dec;
import static ch.bfh.mobicomp.smuoy.utils.Utils.str;

/**
 * Created by chris on 07.11.14.
 */
public class SimpleMeasurement extends Measurement {
    public final double value;
    public final String unit;
    public final Sensor sensor;

    public SimpleMeasurement(Sensor sensor, JSONArray measurements) {
        super(measurements);
        double value = 0;
        String unit = "";
        try {
            JSONObject json = measurements.getJSONObject(0);
            value = dec(json, "valueFloat", 0);
            unit = str(json, "unit", "");
        } catch (JSONException e) {
            Log.e("SimpleMeasurement", "Can't get field 'value' from JSON", e);
        }
        this.value = value;
        this.unit = unit;
        this.sensor = sensor;
    }

    @Override
    public String toString() {
        return String.format("%1$.2f%2$s", value, unit);
    }
}
