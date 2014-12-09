package ch.bfh.mobicomp.smuoy.entities;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import static ch.bfh.mobicomp.smuoy.utils.Utils.num;
import static ch.bfh.mobicomp.smuoy.utils.Utils.str;

/**
 * Created by chris on 07.11.14.
 */
public class Sensor implements Serializable {
    public final String id;
    public final String name;
    public final String description;
    public final int delay; // in seconds
    public final String status;
    public final String type;
    public final Measurement latestData;

    public Sensor(JSONObject json) {
        id = str(json, "sensorId", "");
        name = str(json, "name", "");
        description = str(json, "description", "");
        delay = num(json, "delay", 60);
        status = str(json, "status", "");

        String type = "";
        Measurement latestData = null;

        try {
            type = str(json, "sensorType", "");

            JSONArray measurements = json.getJSONArray("measurements");
            switch (type.substring(0, 3)) {
                case "gps":
                    latestData = new GpsMeasurement(measurements);
                    break;
                case "direction/speed":
                    latestData = new WindMeasurement(measurements);
                    break;
                case "camera":
                    latestData = new ImageMeasurement(this, measurements);
                    break;
                default:
                    latestData = new SimpleMeasurement(this, measurements);
                    break;
            }
        } catch (JSONException e) {
            Log.d("Sensor", "Can't get field 'type' from JSON", e);
        }
        this.type = type;
        this.latestData = latestData;
    }
}
