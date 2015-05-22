package ch.bfh.mobicomp.smuoy.utils;

import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

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
            Log.d("Utils", "Can't get field '" + field + "' from JSON: " + json, e);
        }
        return fallback;
    }

    public static long date(JSONObject json, String field, long fallback) {
        try {
            return json.getLong(field) * 1000;
        } catch (Exception e) {
            Log.d("Utils", "Can't get field '" + field + "' from JSON: " + json, e);
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

    private final static String[] DIRECTIONS = {"N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE", "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW"};

    public static String direction(double degrees) {
        double deg = (degrees % 360) + 360;

        for (int i = 0; ; i++) {
            if (deg < (11.25 + i * 22.5)) {
                return DIRECTIONS[i % 16];
            }
        }
    }

    public static String request(HttpUriRequest request) throws IOException {
        HttpResponse response = new DefaultHttpClient().execute(request);
        StatusLine statusLine = response.getStatusLine();
        if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            out.close();
            return out.toString();
        } else {
            // Closes the connection.
            response.getEntity().getContent().close();
            throw new IOException(statusLine.getReasonPhrase());
        }
    }
}
