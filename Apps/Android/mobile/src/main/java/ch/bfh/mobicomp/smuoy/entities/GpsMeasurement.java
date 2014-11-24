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
public class GpsMeasurement extends Measurement {
    public final double lat;
    public final double lon;

    public GpsMeasurement(JSONArray measurements) {
        super(measurements);
        double lat = 0;
        double lon = 0;
        try {
            for (int i = 0; i < measurements.length(); i++) {
                JSONObject json = measurements.getJSONObject(i);
                switch (str(json, "name", "")) {
                    case "lat":
                        lat = dec(json, "value", 0);
                        break;
                    case "lon":
                        lon = dec(json, "value", 0);
                        break;
                }
            }
        } catch (JSONException e) {
            Log.d("GpsMeasurement", "Can't get field 'name', 'lat' or 'lon' from JSON", e);
        }
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public String toString() {
        return String.format("%1$.2f°N %2$.2f°E", lat, lon);
    }
}
