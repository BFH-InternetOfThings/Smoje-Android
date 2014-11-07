package ch.bfh.mobicomp.smuoy;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chris on 07.11.14.
 */
public class Utils {
    public static String str(JSONObject json, String field, String fallback) {
        try {
            Object o = json.get(field);
            if (o != null) {
                return o.toString();
            }
        } catch (JSONException e) {
            Log.d("Utils", "Can't get field '" + field + "' from JSON", e);
        }
        return fallback;
    }

    public static int num(JSONObject json, String field, int fallback) {
        try {
            Object o = json.get(field);
            if (o != null) {
                return Integer.parseInt(o.toString());
            }
        } catch (Exception e) {
            Log.d("Utils", "Can't get field '" + field + "' from JSON", e);
        }
        return fallback;
    }
    public static double dec(JSONObject json, String field, double fallback) {
        try {
            Object o = json.get(field);
            if (o != null) {
                return Double.parseDouble(o.toString());
            }
        } catch (Exception e) {
            Log.d("Utils", "Can't get field '" + field + "' from JSON", e);
        }
        return fallback;
    }
}
