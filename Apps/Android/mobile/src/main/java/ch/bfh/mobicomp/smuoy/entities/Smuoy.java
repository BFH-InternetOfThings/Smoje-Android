package ch.bfh.mobicomp.smuoy.entities;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import static ch.bfh.mobicomp.smuoy.Utils.str;

/**
 * Created by chris on 07.11.14.
 */
public class Smuoy {
    public String id;
    public String name;
    public String description;
    public List<Sensor> sensors = new LinkedList<>();

    public Smuoy(JSONObject json) {
        id = str(json, "id", "");
        name = str(json, "name", "");
        description = str(json, "description", "");

        try {
            JSONArray ja = json.getJSONArray("sensors");
            for (int i=0; i<ja.length(); i++){
                sensors.add(new Sensor(ja.getJSONObject(i)));
            }
        } catch (JSONException e) {
            Log.d("Smuoy", "Can't get JSONArray 'sensors'", e);
        }
    }
}
