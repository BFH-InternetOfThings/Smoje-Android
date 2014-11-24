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
public class WindMeasurement extends Measurement {
    public final double speed; // km/h
    public final double direction; // °

    public WindMeasurement(JSONArray measurements) {
        super(measurements);
        double speed = 0;
        double direction = 0;
        try {
            for (int i = 0; i < measurements.length(); i++) {
                JSONObject json = measurements.getJSONObject(i);
                switch (str(json, "name", "")) {
                    case "speed":
                        speed = dec(json, "value", 0);
                        break;
                    case "direction":
                        direction = dec(json, "value", 0);
                        break;
                }
            }
        } catch (JSONException e) {
            Log.e("WindMeasurement", "Can't get field 'value' from JSON", e);
        }
        this.speed = speed;
        this.direction = direction;
    }

    @Override
    public String toString() {
        return String.format("%1$.2fkm/h %2$.2f°", speed, direction);
    }
}
