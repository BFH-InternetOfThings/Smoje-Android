package ch.bfh.mobicomp.smuoy.entities;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static ch.bfh.mobicomp.smuoy.Utils.num;
import static ch.bfh.mobicomp.smuoy.Utils.str;

/**
 * Created by chris on 07.11.14.
 */
public class Sensor {
    public String id;
    public String name;
    public String description;
    public int delay; // in seconds
    public String status;
    public String type;
    public String typeDescription;
    public Measurement latestData;

    public Sensor(JSONObject json) {
        id = str(json, "id", "");
        name = str(json, "name", "");
        description = str(json, "description", "");
        delay = num(json, "delay", 60);
        status = str(json, "status", "");

        try {
            JSONObject typeJson = json.getJSONObject("type");
            type = str(typeJson, "name", "");
            typeDescription = str(typeJson, "description", "");

            JSONArray measurements = json.getJSONArray("measurements");
            switch (type) {
                case "location":
                    latestData = new GpsMeasurement(measurements);
                    break;
                case "wind":
                    latestData = new WindMeasurement(measurements);
                    break;
                case "image":
                    latestData = new ImageMeasurement(measurements);
                    break;
                default:
                    latestData = new SimpleMeasurement(measurements, this);
                    break;
            }
        } catch (JSONException e) {
            Log.d("Sensor", "Can't get field 'type' from JSON", e);
        }
    }
}
