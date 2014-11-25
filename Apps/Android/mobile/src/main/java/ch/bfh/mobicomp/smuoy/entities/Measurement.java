package ch.bfh.mobicomp.smuoy.entities;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

import static ch.bfh.mobicomp.smuoy.utils.Utils.date;
import static ch.bfh.mobicomp.smuoy.utils.Utils.str;

/**
 * Created by chris on 07.11.14.
 */
public class Measurement implements Serializable {
    public final String id;
    public final Date timestamp;

    public Measurement(JSONArray measurements) {
        String id = "";
        Date timestamp = null;
        try {
            JSONObject jsonObject = measurements.getJSONObject(0);
            id = str(jsonObject, "id", "");
            timestamp = date(jsonObject, "timestamp", null);
        } catch (Exception e) {
            Log.e("Measurement", "Can't get field 'id' or 'timestamp' from JSON", e);
        }
        this.id = id;
        this.timestamp = timestamp;
    }
}
