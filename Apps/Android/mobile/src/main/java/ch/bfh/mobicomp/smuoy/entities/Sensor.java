package ch.bfh.mobicomp.smuoy.entities;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import static ch.bfh.mobicomp.smuoy.MeasurementService.measurementService;
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

    public Sensor(Smuoy smuoy, JSONObject json) {
        id = str(json, "sensorId", "");
        name = str(json, "name", "");
        description = str(json, "description", "");
        delay = num(json, "delay", 60);
        status = str(json, "status", "");

        String type = "";
        Measurement latestData = null;

        this.type = str(json, "sensorType", "");

        try {
            measurementService.registerSensor(smuoy, this, json.getJSONArray("measurements"));
        } catch (JSONException e) {
            Log.d("Sensor", "Can't get field 'type' from JSON", e);
        }
    }
}
