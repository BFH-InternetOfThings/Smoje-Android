package ch.bfh.mobicomp.smuoy.entities;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;

import static ch.bfh.mobicomp.smuoy.Utils.date;
import static ch.bfh.mobicomp.smuoy.Utils.str;

/**
 * Created by chris on 07.11.14.
 */
public class Measurement {
    public final String id;
    public final Date timestamp;

    protected JSONObject jsonObject;

    public Measurement(JSONArray measurements) {
        String id = "";
        Date timestamp = null;
        try {
            jsonObject = measurements.getJSONObject(0);
            id = str(jsonObject, "id", "");
            timestamp = date(jsonObject, "timestamp", null);
        } catch (Exception e) {
            Log.e("Measurement", "Can't get field 'id' or 'timestamp' from JSON", e);
        }
        this.id = id;
        this.timestamp = timestamp;
    }
}
