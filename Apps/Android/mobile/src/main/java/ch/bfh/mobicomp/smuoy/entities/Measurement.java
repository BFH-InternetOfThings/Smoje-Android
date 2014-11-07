package ch.bfh.mobicomp.smuoy.entities;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static ch.bfh.mobicomp.smuoy.Utils.str;

/**
 * Created by chris on 07.11.14.
 */
public class Measurement {
    public String id;
    public Date timestamp;

    protected JSONObject jsonObject;

    public Measurement(JSONArray measurements) {
        try {
            jsonObject = measurements.getJSONObject(0);
            id = str(jsonObject, "id", "");
            timestamp = SimpleDateFormat.getInstance().parse(str(jsonObject, "timestamp", null));
        } catch (Exception e) {
            Log.e("Measurement", "Can't get field 'id' or 'timestamp' from JSON", e);
        }
    }
}
